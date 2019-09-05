package com.eurodyn.qlack.fuse.cm.mappers;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BinChunkDTOMapperTest {

  private BinChunkDTOMapper binChunkDTOMapper;
  private VersionBin versionBin;
  private List<VersionBin> versionBinList;
  private InitTestValues initTestValues;
  private BinChunkDTO binChunkDTO;
  private List<BinChunkDTO> binChunkDTOList;

  @Before
  public void init(){
    binChunkDTOMapper = new BinChunkDTOMapperImpl();
    initTestValues = new InitTestValues();
    versionBin = initTestValues.createVersionBin();
    versionBinList = initTestValues.createVersionBinList();
    binChunkDTO = initTestValues.createBinChunkDTO();
    binChunkDTOList = initTestValues.createBinChunkDTOList();
  }

  @Test
  public void mapToDTOTest() {
    versionBin.setVersion(null);
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO(versionBin);
    assertNotNull(binChunkDTO);
  }

  @Test
  public void mapToDTOSetContentTest() {
    versionBin.getVersion().setId(null);
    versionBin.setBinContent(new byte[1024]);
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO(versionBin);
    assertNotNull(binChunkDTO);
  }


  @Test
  public void mapToDTONullTest() {
    versionBin = null;
    BinChunkDTO binChunkDTO = binChunkDTOMapper.mapToDTO(versionBin);
    assertNull(binChunkDTO);
  }

  @Test
  public void mapToDTOListTest() {
    List<BinChunkDTO> binChunkDTOList = binChunkDTOMapper.mapToDTO(versionBinList);
    assertNotNull(binChunkDTOList);
  }

  @Test
  public void mapToDTOListNullTest() {
    versionBinList = null;
    List<BinChunkDTO> binChunkDTOList = binChunkDTOMapper.mapToDTO(versionBinList);
    assertNull(binChunkDTOList);
  }

  @Test
  public void mapToEntityTest() {
    VersionBin result = binChunkDTOMapper.mapToEntity(binChunkDTO);
    assertNotNull(result);
  }

  @Test
  public void mapToEntityNullContentTest() {
    binChunkDTO.setBinContent(null);
    VersionBin result = binChunkDTOMapper.mapToEntity(binChunkDTO);
    assertNotNull(result);
  }

  @Test
  public void mapToEntityNullTest() {
    binChunkDTO = null;
    VersionBin result = binChunkDTOMapper.mapToEntity(binChunkDTO);
    assertNull(result);
  }

  @Test
  public void mapToEntityListTest() {
    List<VersionBin> result = binChunkDTOMapper.mapToEntity(binChunkDTOList);
    assertNotNull(result);
  }

  @Test
  public void mapToEntityListNullTest() {
    binChunkDTOList = null;
    List<VersionBin> result = binChunkDTOMapper.mapToEntity(binChunkDTOList);
    assertNull(result);
  }
}
