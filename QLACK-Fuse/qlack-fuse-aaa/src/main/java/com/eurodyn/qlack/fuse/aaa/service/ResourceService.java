package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.mapper.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class that is used to configure {@link Resource} model.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class ResourceService {

  // Repositories
  private final ResourceRepository resourceRepository;
  private final ResourceMapper resourceMapper;

  public ResourceService(ResourceRepository resourceRepository,
    ResourceMapper resourceMapper) {
    this.resourceRepository = resourceRepository;
    this.resourceMapper = resourceMapper;
  }

  /**
   * Creates a {@link Resource} object and retrieves the relative id
   *
   * @param resourceDTO the {@link ResourceDTO} object
   * @return the id of resource
   */
  public String createResource(ResourceDTO resourceDTO) {
    Resource resource = resourceMapper.mapToEntity(resourceDTO);
    resourceRepository.save(resource);

    return resource.getId();
  }

  /**
   * Updates a {@link Resource} entity
   *
   * @param resourceDTO the {@link ResourceDTO} object
   */
  public void updateResource(ResourceDTO resourceDTO) {
    Resource resource = resourceRepository.fetchById(resourceDTO.getId());
    resourceMapper.mapToExistingEntity(resourceDTO, resource);
  }

  /**
   * Deletes a {@link Resource} entity
   *
   * @param resourceID the resource Id
   */
  public void deleteResource(String resourceID) {
    resourceRepository.delete(resourceRepository.fetchById(resourceID));
  }

  /**
   * Deletes a {@link Collection} of resourceIds
   *
   * @param resourceIDs the resourceIds
   */
  public void deleteResources(Collection<String> resourceIDs) {
    for (String resourceID : resourceIDs) {
      resourceRepository.delete(resourceRepository.fetchById(resourceID));
    }
  }

  /**
   * Deletes a @{@link Resource} by given object id
   *
   * @param objectID the objectId
   */
  public void deleteResourceByObjectId(String objectID) {
    resourceRepository.delete(resourceRepository.findByObjectId(objectID));
  }

  /**
   * Deletes resources by a collection of objectIDS
   *
   * @param objectIDs the objectIDs
   */
  public void deleteResourcesByObjectIds(Collection<String> objectIDs) {
    for (String objectID : objectIDs) {
      resourceRepository.delete(resourceRepository.findByObjectId(objectID));
    }
  }

  /**
   * Retrieves the resource id
   *
   * @param resourceID the resource Id
   * @return the {@link ResourceDTO} by its id
   */
  public ResourceDTO getResourceById(String resourceID) {
    return resourceMapper.mapToDTO(resourceRepository.fetchById(resourceID));
  }

  /**
   * Retrieves the {@link Resource} by given objectID
   *
   * @param objectID the specified objectID
   * @return the according {@link ResourceDTO} by its objectID
   */
  public ResourceDTO getResourceByObjectId(String objectID) {
    return resourceMapper.mapToDTO(resourceRepository.findByObjectId(objectID));
  }

}
