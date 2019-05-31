package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceTest {

    @InjectMocks
    private ResourceService resourceService;

    private InitTestValues initTestValues;
    private ResourceRepository resourceRepository = mock(ResourceRepository.class);
    private ResourceDTO resourceDTO;
    private Resource resource;


    @Spy
    private ResourceMapper resourceMapper;

    @Before
    public void init() {
        resourceService = new ResourceService(resourceRepository, resourceMapper);
        initTestValues = new InitTestValues();
        resourceDTO = initTestValues.createResourceDTO();
        resource = initTestValues.createResource();
    }

    @Test
    public void testCreateResource() {
        when(resourceMapper.mapToEntity(resourceDTO)).thenReturn(resource);
        String resourceId = resourceService.createResource(resourceDTO);
        assertEquals(resourceDTO.getId(), resourceId);
        verify(resourceRepository, times(1)).save(resource);
    }

    @Test
    public void testUpdateResource() {
        when(resourceRepository.fetchById(resourceDTO.getId())).thenReturn(resource);
        resourceService.updateResource(resourceDTO);
        assertEquals(resourceDTO.getId(), resourceDTO.getId());
        verify(resourceMapper, times(1)).mapToExistingEntity(resourceDTO, resource);
    }

    @Test
    public void testDeleteResource() {
        Resource resource2 = initTestValues.createResource();
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource2);
        resourceService.deleteResource(resource.getId());
        verify(resourceRepository, times(1)).delete(resource2);
    }

    @Test
    public void testDeleteResources() {
        List<Resource> resources = initTestValues.createResources();
        List<Resource> resources2 = initTestValues.createResources();

        Collection<String> ids = new ArrayList<>();

        for (int i = 0; i < resources.size(); i++) {
            ids.add(resources.get(i).getId());
            when(resourceRepository.fetchById(resources.get(i).getId())).thenReturn(resources2.get(i));
        }
        resourceService.deleteResources(ids);
        verify(resourceRepository, times(ids.size())).delete(any());
    }

    @Test
    public void testDeleteResourceByObjectId() {
        Resource resource2 = initTestValues.createResource();
        when(resourceRepository.findByObjectId(resource.getObjectId())).thenReturn(resource2);
        resourceService.deleteResourceByObjectId(resource.getObjectId());
        verify(resourceRepository, times(1)).delete(resource2);
    }

    @Test
    public void testDeleteResourcesByObjectId() {
        List<Resource> resources = initTestValues.createResources();
        List<Resource> resources2 = initTestValues.createResources();

        Collection<String> ids = new ArrayList<>();

        for (int i = 0; i < resources.size(); i++) {
            ids.add(resources.get(i).getId());
            when(resourceRepository.findByObjectId(resources.get(i).getObjectId())).thenReturn(resources2.get(i));
        }
        resourceService.deleteResourcesByObjectIds(ids);
        verify(resourceRepository, times(ids.size())).delete(any());
    }

    @Test
    public void testGetResource() {
        when(resourceRepository.fetchById(resourceDTO.getId())).thenReturn(resource);
        when(resourceMapper.mapToDTO(resource)).thenReturn(resourceDTO);
        ResourceDTO foundResourceDTO = resourceService.getResourceById(resourceDTO.getId());
        assertEquals(resourceDTO, foundResourceDTO);
    }

    @Test
    public void testGetResourceByObjectId() {
        when(resourceRepository.findByObjectId(resourceDTO.getObjectId())).thenReturn(resource);
        when(resourceMapper.mapToDTO(resource)).thenReturn(resourceDTO);
        ResourceDTO foundResourceDTO = resourceService.getResourceByObjectId(resourceDTO.getObjectId());
        assertEquals(resourceDTO, foundResourceDTO);
    }
}