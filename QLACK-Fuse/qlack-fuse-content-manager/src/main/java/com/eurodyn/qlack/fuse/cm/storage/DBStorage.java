package com.eurodyn.qlack.fuse.cm.storage;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.mapper.BinChunkDTOMapper;
import com.eurodyn.qlack.fuse.cm.model.QVersionBin;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import com.eurodyn.qlack.fuse.cm.repository.VersionBinRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionRepository;
import com.querydsl.core.types.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class DBStorage implements StorageEngine {

  QVersionBin qVersionBin = QVersionBin.versionBin;

  private VersionRepository versionRepository;
  private VersionBinRepository versionBinRepository;
  @Autowired
  private BinChunkDTOMapper mapper;
  @Value("${qlack.fuse.cm.chunkSize:4096000}")
  private int chunkSize;

  public DBStorage(VersionRepository versionRepository,
    VersionBinRepository versionBinRepository) {
    this.versionRepository = versionRepository;
    this.versionBinRepository = versionBinRepository;
  }


  private String persistBinChunk(Version version, byte[] content,
    int chunkIndex) {

    VersionBin versionBin = new VersionBin();
    versionBin.setBinContent(content);
    versionBin.setChunkIndex(chunkIndex);
    versionBin.setId(UUID.randomUUID().toString());
    versionBin.setVersion(version);

    versionBinRepository.save(versionBin);

    return versionBin.getId();
  }

  @Override
  public void setVersionContent(String versionID, byte[] content) {
    Version version = versionRepository.fetchById(versionID);

    int start = 0;
    for (int i = 0; i < (content.length / chunkSize) + 1; i++) {
      if (start + chunkSize > content.length) {
        byte[] newChunk = new byte[content.length - start];
        System.arraycopy(content, start, newChunk, 0, content.length - start);
        persistBinChunk(version, newChunk, i);
      } else {
        byte[] newChunk = new byte[chunkSize];
        System.arraycopy(content, start, newChunk, 0, chunkSize);
        persistBinChunk(version, newChunk, i);
      }
      start += chunkSize;
    }
  }

  @Override
  public byte[] getVersionContent(String versionID) throws IOException {
    Version version = versionRepository.fetchById(versionID);

    List<VersionBin> versionBins = versionBinRepository
      .findByVersionOrderByChunkIndex(version);
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    for (VersionBin vb : versionBins) {
      bOut.write(vb.getBinContent());
    }

    return bOut.toByteArray();
  }

  @Override
  public String setBinChunk(String versionID, byte[] content, int chunkIndex) {
    Version version = versionRepository.fetchById(versionID);
    return persistBinChunk(version, content, chunkIndex);
  }

  @Override
  public BinChunkDTO getBinChunk(String versionID, int chunkIndex) {
    BinChunkDTO binChunkDTO = null;
    Version version = versionRepository.fetchById(versionID);
    Predicate predicate = qVersionBin.version.eq(version)
      .and(
        qVersionBin.chunkIndex.in(Arrays.asList(chunkIndex, chunkIndex + 1)));
    List<VersionBin> versionBins = versionBinRepository
      .findAll(predicate, Sort.by("chunkIndex").descending());

    if (!versionBins.isEmpty()) {
      binChunkDTO = mapper.mapToDTO(versionBins.get(0));
      binChunkDTO.setHasMoreChunks(versionBins.size() == 2);
    }

    return binChunkDTO;
  }

  @Override
  public void deleteVersionBinaries(String versionID) {
    //In DB_STORAGE VersionBinaries are removed automatically by Hibernate's orphan removal
  }
}
