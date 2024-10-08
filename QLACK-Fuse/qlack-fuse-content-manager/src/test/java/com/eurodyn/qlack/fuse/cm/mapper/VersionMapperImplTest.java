package com.eurodyn.qlack.fuse.cm.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VersionMapperImplTest {

  @InjectMocks
  private VersionMapperImpl versionMapper;

  private Version version;

  private List<Version> versions;

  private VersionDTO versionDTO;

  private List<VersionDTO> versionDTOS;

  private InitTestValues initTestValues;

  private VersionAttribute versionAttribute;

  private VersionAttributeDTO versionAttributeDTO;

  private Set<VersionAttributeDTO> versionAttributeDTOs;

  private List<VersionAttribute> versionAttributeList;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    version = initTestValues.createVersion();
    versions = initTestValues.createVersions();
    versionDTO = initTestValues.createVersionDTO();
    versionDTOS = initTestValues.createVersionsDTO();
    versionAttributeDTOs = initTestValues.createAttributeDTOS();
    versionAttribute = new VersionAttribute();
    versionAttributeDTO = new VersionAttributeDTO();
    versionAttributeList = new ArrayList<>();


  }

  @Test
  public void mapToDTONullTest() {
    versionDTO = versionMapper.mapToDTO((Version) null);
    assertNull(versionDTO);
  }

  @Test
  public void mapToDTOTest() {
    versionDTO = versionMapper.mapToDTO(version);
    assertEquals(versionDTO.getName(), version.getName());
  }


  @Test
  public void mapToDTOListNullTest() {
    versionDTOS = versionMapper.mapToDTO((List<Version>) null);
    assertNull(versionDTOS);

  }

  @Test
  public void mapListToListDTOTest() {
    versionDTOS = versionMapper.mapToDTO(versions);
    assertEquals(versionDTOS.size(), versions.size());
  }

  @Test
  public void mapToEntityNullTest() {
    version = versionMapper.mapToEntity((VersionDTO) null);
    assertNull(version);
  }

  @Test
  public void mapToEntityListNullTest() {
    versions = versionMapper.mapToEntity((List<VersionDTO>) null);
    assertNull(versions);
  }

  @Test
  public void mapToEntityTest() {
    version = versionMapper.mapToEntity(versionDTO);
    assertEquals(version.getFilename(), versionDTO.getFilename());
  }

  @Test
  public void versionAttributeListToVersionAttributeDTOSetTest() {
    assertNull(
      versionMapper.versionAttributeListToVersionAttributeDTOSet(null));
  }

  @Test
  public void versionAttributeDTOSetToVersionAttributeListTest() {
    assertNull(
      versionMapper.versionAttributeDTOSetToVersionAttributeList(null));
  }

  @Test
  public void versionAttributeDTOToVersionAttributeNullTest() {
    assertNull(versionMapper.versionAttributeDTOToVersionAttribute(null));
  }


  @Test
  public void versionAttributeToVersionAttributeDTONullTest() {
    assertNull(versionMapper.versionAttributeToVersionAttributeDTO(null));
  }

  @Test
  public void versionAttributeToVersionAttributeDTOTest() {
    versionAttribute.setName("name");
    VersionAttributeDTO versionAttributeDTO = versionMapper
      .versionAttributeToVersionAttributeDTO(versionAttribute);
    versionAttributeDTO.setName(versionAttribute.getName());
    assertNotNull(versionAttributeDTO.getName());

  }

  @Test
  public void versionAttributeDTOToVersionAttributeTest() {
    versionAttributeDTO.setName("name");
    VersionAttribute versionAttribute = versionMapper
      .versionAttributeDTOToVersionAttribute(versionAttributeDTO);
    versionAttribute.setName(versionAttributeDTO.getName());
    assertNotNull(versionAttribute.getName());

  }

  @Test
  public void mapEntityListToDTOList() {
    versions = versionMapper.mapToEntity(versionDTOS);
    assertEquals(versions.size(), versionDTOS.size());
  }

  @Test
  public void attributeSetToListTest() {
    List<VersionAttribute> list = versionMapper
      .versionAttributeDTOSetToVersionAttributeList(versionAttributeDTOs);
    list.add(versionAttribute);
    assertNotNull(list);
  }

  @Test
  public void attributeListToSetTest() {
    versionAttributeList.add(versionAttribute);
    assertNotNull(versionAttributeList);
    Set<VersionAttributeDTO> set = versionMapper
      .versionAttributeListToVersionAttributeDTOSet(versionAttributeList);
    set.add(versionAttributeDTO);
    assertNotNull(set);
  }


}
