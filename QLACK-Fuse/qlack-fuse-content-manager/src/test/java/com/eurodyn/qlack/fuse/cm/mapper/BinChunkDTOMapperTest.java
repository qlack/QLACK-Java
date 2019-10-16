package com.eurodyn.qlack.fuse.cm.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BinChunkDTOMapperTest {

  private BinChunkDTOMapper binChunkDTOMapper;
  private VersionBin versionBin;
  private List<VersionBin> versionBinList;
  private BinChunkDTO binChunkDTO;
  private List<BinChunkDTO> binChunkDTOList;

  @Before
  public void init() {
    binChunkDTOMapper = new BinChunkDTOMapperImpl();
    InitTestValues initTestValues = new InitTestValues();
    versionBin = initTestValues.createVersionBin();
    versionBinList = initTestValues.createVersionBinList();
    binChunkDTO = initTestValues.createBinChunkDTO();
    binChunkDTOList = initTestValues.createBinChunkDTOList();
  }

  @Test
  public void mapToDTOTest() {
    versionBin.setVersion(null);
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO(versionBin);
    assertEquals(versionBin.getId(), binChunkDTO.getId());
  }

  @Test
  public void mapToDTONullContentTest() {
    versionBin.setBinContent(null);
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO(versionBin);
    assertEquals(versionBin.getId(), binChunkDTO.getId());
  }

  @Test
  public void mapToDTOSetContentTest() {
    versionBin.getVersion().setId(null);
    versionBin.setBinContent(new byte[1024]);
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO(versionBin);
    assertEquals(versionBin.getId(), binChunkDTO.getId());
  }


  @Test
  public void mapToDTONullTest() {
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO((VersionBin) null);
    assertNull(binChunkDTO);
  }

  @Test
  public void mapToDTOListTest() {
    List<BinChunkDTO> binChunkDTOList = binChunkDTOMapper.mapToDTO(versionBinList);
    assertEquals(versionBinList.size(), binChunkDTOList.size());
  }

  @Test
  public void mapToDTOListNullTest() {
    List<BinChunkDTO> binChunkDTOList = binChunkDTOMapper.mapToDTO((List<VersionBin>) null);
    assertNull(binChunkDTOList);
  }

  @Test
  public void mapToEntityTest() {
    VersionBin result = binChunkDTOMapper.mapToEntity(binChunkDTO);
    assertEquals(binChunkDTO.getId(), result.getId());
  }

  @Test
  public void mapToEntityNullContentTest() {
    binChunkDTO.setBinContent(null);
    VersionBin result = binChunkDTOMapper.mapToEntity(binChunkDTO);
    assertEquals(binChunkDTO.getId(), result.getId());
    assertEquals(binChunkDTO.getBinContent(), result.getBinContent());
  }

  @Test
  public void mapToEntityNullTest() {
    binChunkDTO = null;
    VersionBin result = binChunkDTOMapper.mapToEntity((BinChunkDTO) null);
    assertNull(result);
  }

  @Test
  public void mapToEntityListTest() {
    List<VersionBin> result = binChunkDTOMapper.mapToEntity(binChunkDTOList);
    assertEquals(binChunkDTOList.size(), result.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    binChunkDTOList = null;
    List<VersionBin> result = binChunkDTOMapper.mapToEntity((List<BinChunkDTO>) null);
    assertNull(result);
  }
}
