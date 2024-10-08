package com.eurodyn.qlack.fuse.cm.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VersionAttributeMapperImplTest {

  @InjectMocks
  private VersionAttributeMapperImpl versionAttributeMapper;

  private InitTestValues initTestValues;

  private VersionAttribute versionAttribute;

  private VersionAttributeDTO versionAttributeDTO;

  private List<VersionAttribute> versionAttributeList;

  private List<VersionAttributeDTO> versionAttributeDTOS;


  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    versionAttribute = new VersionAttribute();
    versionAttributeDTO = new VersionAttributeDTO();
    versionAttributeList = new ArrayList<>();
    versionAttributeDTOS = new ArrayList<>();
  }

  @Test
  public void mapToDTONullTest() {
    versionAttributeDTO = versionAttributeMapper
      .mapToDTO((VersionAttribute) null);
    assertNull(versionAttributeDTO);
  }

  @Test
  public void mapToDTOTest() {
    versionAttributeDTO = versionAttributeMapper.mapToDTO(versionAttribute);
    assertEquals(versionAttributeDTO.getName(), versionAttribute.getName());
  }

  @Test
  public void mapToDTOListNullTest() {
    versionAttributeDTOS = versionAttributeMapper
      .mapToDTO((List<VersionAttribute>) null);
    assertNull(versionAttributeDTOS);
  }

  @Test
  public void mapListToListDTOTest() {
    versionAttributeDTOS = versionAttributeMapper
      .mapToDTO(versionAttributeList);
    versionAttributeList.add(versionAttribute);
    assertNotNull(versionAttributeList);
    versionAttributeDTO = versionAttributeMapper.mapToDTO(versionAttribute);
    versionAttributeDTOS.add(versionAttributeDTO);
    assertNotNull(versionAttributeDTOS);

  }

  @Test
  public void mapToEntityNullTest() {
    versionAttribute = versionAttributeMapper
      .mapToEntity((VersionAttributeDTO) null);
    assertNull(versionAttribute);
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(versionAttributeList = versionAttributeMapper
      .mapToEntity((List<VersionAttributeDTO>) null));
  }

  @Test
  public void mapToEntityTest() {
    versionAttribute = versionAttributeMapper.mapToEntity(versionAttributeDTO);
    assertEquals(versionAttribute.getName(), versionAttributeDTO.getName());
  }

  @Test
  public void mapEntityListToDTOListTest() {
    versionAttributeDTOS.add(versionAttributeDTO);
    assertNotNull(versionAttributeDTOS);
    versionAttributeList = versionAttributeMapper
      .mapToEntity(versionAttributeDTOS);
    versionAttributeList.add(versionAttribute);
    assertNotNull(versionAttributeList);
  }

  @Test
  public void mapListEntityToDTOListTest() {
    versionAttributeList.add(versionAttribute);
    assertNotNull(versionAttributeList);
    versionAttributeDTOS = versionAttributeMapper
      .mapToDTO(versionAttributeList);
    versionAttributeDTOS.add(versionAttributeDTO);
    assertNotNull(versionAttributeList);
  }

  @Test
  public void versionAttributeVersionIdTest() {
    Version version = initTestValues.createVersion();
    version.setId("id");
    versionAttribute.setVersion(version);
    versionAttributeDTO = versionAttributeMapper.mapToDTO(versionAttribute);
    assertEquals(versionAttribute.getVersion().getId(),
      versionAttributeDTO.getVersionId());
  }

  @Test
  public void versionAttributeVersionIdNullTest() {
    Version version = initTestValues.createVersion();
    version.setId(null);
    versionAttribute.setVersion(version);
    versionAttributeDTO = versionAttributeMapper.mapToDTO(versionAttribute);
    assertNull(version.getId());
  }

  @Test
  public void versionAttributeNullTest() {
    versionAttributeDTO = versionAttributeMapper
      .mapToDTO((VersionAttribute) null);
    assertNull(versionAttributeDTO);
  }

}
