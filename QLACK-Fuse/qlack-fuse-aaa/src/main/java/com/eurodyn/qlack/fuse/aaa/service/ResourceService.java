package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class ResourceService {

  // Repositories
  private final ResourceRepository resourceRepository;
  private final ResourceMapper resourceMapper;

  public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
    this.resourceRepository = resourceRepository;
    this.resourceMapper = resourceMapper;
  }

  public String createResource(ResourceDTO resourceDTO) {
    Resource resource = resourceMapper.mapToEntity(resourceDTO);
    resourceRepository.save(resource);

    return resource.getId();
  }

  public void updateResource(ResourceDTO resourceDTO) {
    Resource resource = resourceRepository.fetchById(resourceDTO.getId());
    resourceMapper.mapToExistingEntity(resourceDTO, resource);
  }

  public void deleteResource(String resourceID) {
    resourceRepository.delete(resourceRepository.fetchById(resourceID));
  }

  public void deleteResources(Collection<String> resourceIDs) {
    for (String resourceID : resourceIDs) {
      resourceRepository.delete(resourceRepository.fetchById(resourceID));
    }
  }

  public void deleteResourceByObjectId(String objectID) {
    resourceRepository.delete(resourceRepository.findByObjectId(objectID));
  }

  public void deleteResourcesByObjectIds(Collection<String> objectIDs) {
    for (String objectID : objectIDs) {
      resourceRepository.delete(resourceRepository.findByObjectId(objectID));
    }
  }

  public ResourceDTO getResourceById(String resourceID) {
    return resourceMapper.mapToDTO(resourceRepository.fetchById(resourceID));
  }

  public ResourceDTO getResourceByObjectId(String objectID) {
    return resourceMapper.mapToDTO(resourceRepository.findByObjectId(objectID));
  }

}
