package com.eurodyn.qlack.fuse.cm.storage;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.exception.QStorageException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FSStorage implements StorageEngine {

  private static final Logger LOGGER = Logger.getLogger(FSStorage.class.getName());
  // The number of available buckets in powers of 10. Make sure you do not
  // change this value on a running system as buckets will be misaligned.
  @Value("${qlack.fuse.cm.buckets:10}")
  private int numberOfBuckets;

  @Value("${qlack.fuse.cm.chunkSize:4096000}")
  private int chunkSize = 4096000;

  // The root of the filesystem
  @Value("${qlack.fuse.cm.rootFS:/tmp}")
  private String rootFS;

  /**
   * Converts a uuid to a file-system path using a bucketing algorithm.
   */
  private String bucketise(String uuid) {
    return StringUtils.join(new String[]{rootFS, String.valueOf(uuid.hashCode())
        .substring(0, (int) Math.log10(numberOfBuckets)), uuid + ".bin"}, File.separator);
  }

  @Override
  public void setVersionContent(String versionID, byte[] content) {
    File f = new File(bucketise(versionID));
    try {
      Files.createDirectories(f.getParentFile().toPath());
      Files.write(f.toPath(), content);
      LOGGER.log(Level.FINEST, "Created file: {0}.", f.getAbsolutePath());
    } catch (IOException ex) {
      throw new QStorageException("Could not persist file into " + f.getAbsolutePath(), ex);
    }
  }

  @Override
  public byte[] getVersionContent(String versionID) {
    String fileLocation = bucketise(versionID);

    try {
      LOGGER.log(Level.FINEST, "Reading from file: {0}.", fileLocation);
      return Files.readAllBytes(new File(fileLocation).toPath());
    } catch (IOException ex) {
      throw new QStorageException("Could not read file from " + fileLocation, ex);
    }
  }

  // Note that chunkIndex is ignored on filesystem-based repositories. Chunks
  // are simply appended to the end of the existing file (or a new one is
  // created) independently of their index/order.
  @Override
  public String setBinChunk(String versionID, byte[] content, int chunkIndex) {
    File f = new File(bucketise(versionID));
    try {
      Files.createDirectories(f.getParentFile().toPath());
      boolean created = f.createNewFile();
      if (Boolean.FALSE.equals(created)) {
        LOGGER.severe("Filename already exists");
      }
      Files.write(f.toPath(), content, StandardOpenOption.APPEND);
    } catch (IOException ex) {
      throw new QStorageException("Could not persist file into " + f.getAbsolutePath(), ex);
    }

    return f.getAbsolutePath();
  }

  @Override
  public BinChunkDTO getBinChunk(String versionID, int chunkIndex) {
    BinChunkDTO retVal = new BinChunkDTO();
    String fileLocation = bucketise(versionID);
    File file = new File(fileLocation);

    try (RandomAccessFile fileStore = new RandomAccessFile(file, "r")) {
      LOGGER.log(Level.FINEST, "Reading from file: {0}.", fileLocation);
      long startingPosition = (long) chunkIndex * chunkSize;

      fileStore.seek(startingPosition);
      byte[] bb = new byte[chunkSize];
      fileStore.read(bb);

      retVal.setHasMoreChunks(file.length() > ((chunkIndex + 1) * chunkSize));

      if (!retVal.isHasMoreChunks()) {
        int trimPosition = (int) (file.length() - (chunkIndex * chunkSize));
        retVal.setBinContent(Arrays.copyOf(bb, trimPosition));
      } else {
        retVal.setBinContent(bb);
      }
    } catch (IOException ex) {
      throw new QStorageException("Could not read file from " + fileLocation, ex);
    }

    retVal.setChunkIndex(chunkIndex);
    retVal.setVersionID(versionID);

    return retVal;
  }

  @Override
  public void deleteVersionBinaries(String versionID) {
    String fileLocation = bucketise(versionID);
    try {
      Files.deleteIfExists(new File(fileLocation).toPath());
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, MessageFormat.format(
          "Could not delete file {0}.", fileLocation), e);
    }
  }

}
