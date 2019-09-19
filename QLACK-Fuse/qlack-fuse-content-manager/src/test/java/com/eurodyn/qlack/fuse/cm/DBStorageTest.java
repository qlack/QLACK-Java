package com.eurodyn.qlack.fuse.cm;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.mappers.BinChunkDTOMapper;
import com.eurodyn.qlack.fuse.cm.model.QVersionBin;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import com.eurodyn.qlack.fuse.cm.repository.VersionBinRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionRepository;
import com.eurodyn.qlack.fuse.cm.storage.DBStorage;
import com.querydsl.core.types.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DBStorageTest {

  @InjectMocks private DBStorage dbStorage;

  @Mock private VersionRepository versionRepository;
  @Mock private VersionBinRepository versionBinRepository;
  @Mock private Version version;
  @Mock private BinChunkDTOMapper mapper;
  @Mock private BinChunkDTO binChunkDTO;

  private List<VersionBin> versionBinList;
  private QVersionBin qVersionBin = QVersionBin.versionBin;
  private int chunkIndex = 0;
  private VersionBin versionBin;

  @Before
  public void init(){
    InitTestValues initTestValues = new InitTestValues();
    dbStorage = new DBStorage(versionRepository, versionBinRepository);
    ReflectionTestUtils.setField(dbStorage, "chunkSize", 123);
    versionBinList = initTestValues.createVersionBinList();
    version = initTestValues.createVersion();
    versionBin = initTestValues.createVersionBin();
    ReflectionTestUtils.setField(dbStorage, "mapper", mapper);
  }

  @Test
  public void setVersionContentTest(){
    dbStorage.setVersionContent("versionId", new byte[1024]);
    verify(versionRepository, times(1)).fetchById(any());
  }

  @Test
  public void getVersionContentTest() throws IOException {
    when(versionRepository.fetchById("versionId")).thenReturn(version);
    when(versionBinRepository.findByVersionOrderByChunkIndex(version)).thenReturn(versionBinList);
    dbStorage.getVersionContent("versionId");
    verify(versionRepository, times(1)).fetchById(any());
    verify(versionBinRepository, times(1)).findByVersionOrderByChunkIndex(version);
  }

  @Test
  public void getBinChunkEmptyBinsTest() {
    when(versionRepository.fetchById("versionId")).thenReturn(version);

    dbStorage.getBinChunk("versionId", chunkIndex);
    verify(versionRepository, times(1)).fetchById(any());
  }

  @Test
  public void getBinChunkTest() {
    Predicate predicate = qVersionBin.version.eq(version)
        .and(qVersionBin.chunkIndex.in(Arrays.asList(chunkIndex, chunkIndex + 1)));
    when(versionRepository.fetchById("versionId")).thenReturn(version);
    when(versionBinRepository.findAll(predicate, Sort.by("chunkIndex").descending())).thenReturn(versionBinList);
    when(mapper.mapToDTO(versionBinList.get(0))).thenReturn(binChunkDTO);

    dbStorage.getBinChunk("versionId", chunkIndex);
    dbStorage.deleteVersionBinaries("versionId");
    verify(versionRepository, times(1)).fetchById(any());
  }

  @Test
  public void getBinChunkBinsSizeTest() {
    versionBinList.add(versionBin);
    Predicate predicate = qVersionBin.version.eq(version)
        .and(qVersionBin.chunkIndex.in(Arrays.asList(chunkIndex, chunkIndex + 1)));
    when(versionRepository.fetchById("versionId")).thenReturn(version);
    when(versionBinRepository.findAll(predicate, Sort.by("chunkIndex").descending())).thenReturn(versionBinList);
    when(mapper.mapToDTO(versionBinList.get(0))).thenReturn(binChunkDTO);

    dbStorage.getBinChunk("versionId", chunkIndex);
    verify(versionRepository, times(1)).fetchById(any());
  }
}
