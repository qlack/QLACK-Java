package com.eurodyn.qlack.fuse.fileupload.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileNotCompletedException;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileNotFoundException;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileUploadException;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.model.DBFilePK;
import com.eurodyn.qlack.fuse.fileupload.model.QDBFile;
import com.eurodyn.qlack.fuse.fileupload.repository.DBFileRepository;
import com.eurodyn.qlack.fuse.fileupload.service.FileUpload;
import com.eurodyn.qlack.util.av.api.dto.VirusScanDTO;
import com.eurodyn.qlack.util.av.api.exception.VirusFoundException;
import com.eurodyn.qlack.util.av.api.exception.VirusScanException;
import com.eurodyn.qlack.util.av.api.service.AvService;
import com.querydsl.core.types.Predicate;

import lombok.extern.java.Log;

@Log
@Service
public class FileUploadImpl implements FileUpload {

  private DBFileRepository dbFileRepository;
  private QDBFile qdbFile = QDBFile.dBFile;
  @Value("${qlack.fuse.fileupload.cleanupEnabled:false}")
  private boolean cleanupEnabled;
  @Value("${qlack.fuse.fileupload.virusScanEnabled:false}")
  private boolean isVirusScanEnabled;
  @Value("${qlack.fuse.fileupload.cleanupThreshold:300000}")
  private long cleanupThreshold;

  private Optional<AvService> clamAvService;

  private final String SECURITY_RISK_MESSAGE = "The file you are trying to upload was flagged as malicious. "
    + "Please review the file.";

  @Autowired
  public FileUploadImpl(DBFileRepository dbFileRepository, Optional<AvService> clamAvService) {
    this.dbFileRepository = dbFileRepository;
    this.clamAvService = clamAvService;
  }

  /**
   * Given a file ID it reconstructs the complete file that was uploaded together with it metadata.
   *
   * @param fileID The File ID to reconstruct.
   * @param includeBinary Whether to include the binary content of the file or not.
   */
  private DBFileDTO getByID(String fileID, boolean includeBinary) {
    // Find all chunks of the requested file.
    List<DBFile> results = dbFileRepository.findAll().stream()
                                           .sorted(Comparator.comparing(f -> f.getDbFilePK().getChunkOrder()))
                                           .filter(f -> f.getDbFilePK().getId().equals(fileID)).collect(
        Collectors.toList());

    // Check if any chunk for the requested file has been found.
    if (results.isEmpty()) {
      throw new QFileNotFoundException();
    }

    // Get a random chunk to obtain information for the underlying file
    // (i.e. all chunks contain replicated information about the file from
    // which they were decomposed).
    DBFile randomChunk = results.get(0);

    // Prepare the return value.
    DBFileDTO dto = new DBFileDTO();

    // Check if all expected chunks of this file are available. This check
    // is performed only when the caller has requested the binary
    // representation of the file in order not to return a corrupted file.
    if (includeBinary) {
      long startTime = System.currentTimeMillis();
      if (randomChunk.getExpectedChunks() != results.size()) {
        throw new QFileNotCompletedException();
      }
      // Assemble the original file out of its chunks.
      try {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(
          (int) randomChunk.getFileSize());
        for (DBFile f : results) {
          bOut.write(f.getChunkData());
        }
        dto.setFileData(bOut.toByteArray());

      } catch (IOException e) {
        log.log(Level.SEVERE, "Could not reassemble file " + fileID, e);
        throw new QFileUploadException("Could not reassemble file "
                                         + fileID);
      }
      dto.setReassemblyTime(System.currentTimeMillis() - startTime);
    } else {
      dto.setReassemblyTime(-1);
    }

    // Further compose the return value.
    dto.setFilename(randomChunk.getFileName());
    dto.setId(fileID);
    dto.setReceivedChunks(results.size());
    dto.setTotalChunks(randomChunk.getExpectedChunks());
    dto.setUploadedAt(randomChunk.getUploadedAt());
    dto.setUploadedBy(randomChunk.getUploadedBy());
    dto.setTotalSize(randomChunk.getFileSize());

    return dto;
  }

  public DBFileDTO getByIDForConsole(String fileID) {
    return getByID(fileID, true);
  }

  public DBFileDTO getByIDAndChunk(String fileID, long chunkIndex) {

    DBFileDTO dto = new DBFileDTO();

    Predicate predicate = qdbFile.dbFilePK.id.eq(fileID)
                                             .and(qdbFile.dbFilePK.chunkOrder
                                                    .in(Arrays.asList(chunkIndex, chunkIndex + 1)));
    List<DBFile> results = dbFileRepository
      .findAll(predicate, Sort.by("id.chunkOrder").ascending());
    // Check if any chunk for the requested file has been found.
    if (results.isEmpty()) {
      throw new QFileNotFoundException();
    }

    // Get the respective to the chunkIndex chunk  to obtain information for the
    // underlying file, from which the chunk has been decomposed
    DBFile currentChunk = results.get(0);

    // Retrieve the file info from the specific
    dto.setId(fileID);
    dto.setFilename(currentChunk.getFileName());
    dto.setTotalChunks(currentChunk.getExpectedChunks());
    dto.setUploadedAt(currentChunk.getUploadedAt());
    dto.setUploadedBy(currentChunk.getUploadedBy());
    dto.setTotalSize(currentChunk.getFileSize());
    dto.setFileData(currentChunk.getChunkData());
    dto.setChunkNumber(currentChunk.getDbFilePK().getChunkOrder());
    dto.setHasMoreChunks(results.size() == 2);

    return dto;
  }

  /**
   * Checks if a chunk has already been uploaded
   *
   * @param id The file id
   * @param chunkNumber The chunk order number
   *
   * @return true if chunk has already been uploaded, false if not
   */
  public boolean checkChunk(String id, Long chunkNumber) {
    return dbFileRepository.getChunk(id, chunkNumber) != null;
  }

  /**
   * Uploads a chunk/file.
   *
   * @param dbFileDTO the file
   *
   * @return true if file has been uploaded, false if not
   */
  public boolean upload(DBFileDTO dbFileDTO) {
    boolean chunkExists = false;

    // Check if this chunk has already been uploaded, so that we can support
    // updating existing chunks.
    DBFile file = dbFileRepository.getChunk(dbFileDTO.getId(), dbFileDTO.getChunkNumber());
    if (file != null) {
      chunkExists = true;
    } else {
      file = new DBFile(new DBFilePK(dbFileDTO.getId(), dbFileDTO.getChunkNumber()));
    }
    file.setExpectedChunks(dbFileDTO.getTotalChunks());
    file.setFileName(dbFileDTO.getFilename());
    if (dbFileDTO.getTotalSize() == 0) {
      file.setFileSize(dbFileDTO.getFileData().length);
    } else {
      file.setFileSize(dbFileDTO.getTotalSize());
    }
    file.setUploadedAt(System.currentTimeMillis());
    file.setUploadedBy(dbFileDTO.getUploadedBy());

      if (isVirusScanEnabled) {
          log.log(Level.INFO, "Attempting to enable file virus scanning..");
          VirusScanDTO result = null;
          try {
              result = clamAvService.map(cs -> cs.virusScan(dbFileDTO.getFileData())).orElse(null);
          } catch (VirusScanException e) {
              log.log(Level.WARNING, e.getMessage());
          }

          if (result != null && !result.isVirusFree()) {
              throw new VirusFoundException(SECURITY_RISK_MESSAGE);
          } else {
              if (!clamAvService.isPresent()) {
                  log.log(Level.WARNING, "Virus scanning is not enabled. No implementation provided.");
              }
          }
      }

      file.setChunkData(dbFileDTO.getFileData());
      file.setChunkSize(dbFileDTO.getFileData().length);
      dbFileRepository.save(file);
      return chunkExists;
  }

  public void deleteByID(String fileID) {
    dbFileRepository.deleteById(fileID);
  }

  public void deleteByIDForConsole(String fileID) {
    dbFileRepository.deleteById(fileID);
  }

  public DBFileDTO getByID(String fileID) {
    return getByID(fileID, true);
  }

  public List<DBFileDTO> listFiles(boolean includeBinaryContent) {
    List<DBFileDTO> list = new ArrayList<>();

    // First find all unique IDs for file chunks.
    List<String> chunks = dbFileRepository.findAll(Sort.by("UploadedAt"))
                                          .stream()
                                          .map(DBFile::getDbFilePK)
                                          .map(DBFilePK::getId)
                                          .collect(Collectors.toList());

    for (String id : chunks) {
      list.add(getByID(id, includeBinaryContent));
    }
    return list;
  }

  /**
   * Cleans up file-chunks which have been uploaded but never reclaimed/deleted. This method uses
   * the {@link Scheduled} annotation and user defined properties to configure the method execution,
   * the execution interval and the EPOCH before which all files get deleted (default: greater than 5 minutes =
   * 300000 ms)
   */
  @Transactional
  @Scheduled(fixedDelayString = "${qlack.fuse.fileupload.cleanupInterval:300000}")
  public void cleanupExpired() {
    if (cleanupEnabled) {
      long deleteBefore = System.currentTimeMillis() - cleanupThreshold;
      List<DBFile> files = dbFileRepository.findAll().stream()
                                           .filter(dbFile -> dbFile.getUploadedAt() < deleteBefore)
                                           .collect(Collectors.toList());
      if (!files.isEmpty()) {
        dbFileRepository.deleteAll(files);
        log.info("Uploaded files database cleanup has been performed.");
      }
    }
  }

  /**
   * @param deleteBefore The EPOCH before which all files get deleted. Cleans up file-chunks which
   * have been uploaded but never reclaimed/deleted. This method uses a user defined property to
   * configure the method execution
   */
  @Transactional
  public void cleanupExpired(long deleteBefore) {
    if (cleanupEnabled) {
      List<DBFile> files = dbFileRepository.findAll().stream()
                                           .filter(dbFile -> dbFile.getUploadedAt() < deleteBefore)
                                           .collect(Collectors.toList());
      if (!files.isEmpty()) {
        dbFileRepository.deleteAll(files);
        log.info("Uploaded files database cleanup has been performed.");
      }
    }
  }

  public List<DBFileDTO> listFilesForConsole(boolean includeBinary) {
    List<DBFileDTO> list = new ArrayList<>();

    // First find all unique IDs for file chunks.
    Set<String> chunks = dbFileRepository.findAll(Sort.by("uploadedAt")).stream()
                                         .map(DBFile::getDbFilePK)
                                         .map(DBFilePK::getId)
                                         .collect(Collectors.toSet());
    for (String id : chunks) {
      list.add(getByID(id, includeBinary));
    }

    return list;
  }
}
