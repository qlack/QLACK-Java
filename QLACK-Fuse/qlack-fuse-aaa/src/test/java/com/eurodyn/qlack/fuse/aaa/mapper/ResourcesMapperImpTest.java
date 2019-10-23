package com.eurodyn.qlack.fuse.aaa.mapper;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ResourcesMapperImpTest {

    @InjectMocks
    private ResourceMapperImpl resourceMapper;

    private InitTestValues initTestValues;

    private Resource resource;

    private ResourceDTO resourceDTO;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        resource = initTestValues.createResource();
        resourceDTO = initTestValues.createResourceDTO();
    }

    @Test
    public void mapToDTONullTest(){
        ResourceDTO resourceDTO = resourceMapper.mapToDTO((Resource) null);
        assertEquals(null,resourceDTO);
    }

    @Test
    public void mapToDTOListNullTest() {
        List<ResourceDTO> resourceDTOList = resourceMapper.mapToDTO((List<Resource>) null);
        assertEquals(null, resourceDTOList);
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
        assertEquals(null, resource);
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
        assertEquals(null, resourceMapper.mapToEntity((List<ResourceDTO>) null));
        List<Resource> resourceList = resourceMapper.mapToEntity((List<ResourceDTO>) null);
        assertEquals(null, resourceList);

    }


    @Test
    public void mapToExistingEntitySetDTOTest(){
        ResourceDTO resourceDTO = initTestValues.createResourceDTO();
        resourceDTO.setDescription(null);
        Resource resource = initTestValues.createResource();
        resource.setDescription(null);
        resourceMapper.mapToExistingEntity(resourceDTO,resource);
    }

    @Test
    public void mapToExistingEntitySetDTONullTest(){
        ResourceDTO resourceDTO = initTestValues.createResourceDTO();
        resourceDTO.setDescription(null);
        resourceMapper.mapToExistingEntity(null,resource);
        assertEquals(null,resourceDTO.getDescription());
    }
}
