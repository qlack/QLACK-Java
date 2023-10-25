package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResourcesMapperImpTest {

  @InjectMocks
  private ResourceMapperImpl resourceMapper;

  private InitTestValues initTestValues;

  private Resource resource;

  private ResourceDTO resourceDTO;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    resource = initTestValues.createResource();
    resourceDTO = initTestValues.createResourceDTO();
  }

  @Test
  public void mapToDTONullTest() {
    ResourceDTO resourceDTO = resourceMapper.mapToDTO((Resource) null);
    assertNull(resourceDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    List<ResourceDTO> resourceDTOList = resourceMapper
      .mapToDTO((List<Resource>) null);
    assertNull(resourceDTOList);
  }

  @Test
  public void mapToDTOListTest() {
    List<Resource> resourceList = new ArrayList<>();
    resourceList.add(initTestValues.createResource());
    List<ResourceDTO> resourceDTOS = resourceMapper.mapToDTO(resourceList);
    assertEquals(resourceList.size(), resourceDTOS.size());
  }

  @Test
  public void mapToEntityNullTest() {
    Resource resource = resourceMapper.mapToEntity((ResourceDTO) null);
    assertNull(resource);
  }


  @Test
  public void mapToEntityListTest() {
    List<ResourceDTO> resourceDTOS = new ArrayList<>();
    resourceDTOS.add(initTestValues.createResourceDTO());
    List<Resource> resourceList = resourceMapper.mapToEntity(resourceDTOS);
    assertEquals(resourceDTOS.size(), resourceList.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(resourceMapper.mapToEntity((List<ResourceDTO>) null));
    List<Resource> resourceList = resourceMapper
      .mapToEntity((List<ResourceDTO>) null);
    assertNull(resourceList);

  }


  @Test
  public void mapToExistingEntitySetDTOTest() {
    ResourceDTO resourceDTO = initTestValues.createResourceDTO();
    resourceDTO.setDescription(null);
    Resource resource = initTestValues.createResource();
    resource.setDescription(null);
    resourceMapper.mapToExistingEntity(resourceDTO, resource);
    assertNull(resourceDTO.getDescription());
  }

  @Test
  public void mapToExistingEntitySetDTONullTest() {
    ResourceDTO resourceDTO = initTestValues.createResourceDTO();
    resourceDTO.setDescription(null);
    resourceMapper.mapToExistingEntity(null, resource);
    assertNull(resourceDTO.getDescription());
  }
}
