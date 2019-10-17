package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.mapper.OpTemplateMapper;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.repository.OpTemplateHasOperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.OpTemplateRepository;
import com.eurodyn.qlack.fuse.aaa.repository.OperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Service class for OpTemplate
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class OpTemplateService {


  // Repositories
  private final OpTemplateRepository opTemplateRepository;
  private final OpTemplateHasOperationRepository opTemplateHasOperationRepository;
  private final OperationRepository operationRepository;
  private final ResourceRepository resourceRepository;
  // Mappers
  private final OpTemplateMapper opTemplateMapper;

  public OpTemplateService(
      OpTemplateRepository opTemplateRepository,
      OpTemplateHasOperationRepository opTemplateHasOperationRepository,
      OperationRepository operationRepository, ResourceRepository resourceRepository,
      OpTemplateMapper opTemplateMapper) {
    this.opTemplateRepository = opTemplateRepository;
    this.opTemplateHasOperationRepository = opTemplateHasOperationRepository;
    this.operationRepository = operationRepository;
    this.resourceRepository = resourceRepository;
    this.opTemplateMapper = opTemplateMapper;
  }

  /**
   * Creates a Template
   *
   * @param templateDTO the {@link OpTemplateDTO} object
   * @return the template id
   */
  public String createTemplate(OpTemplateDTO templateDTO) {
    OpTemplate template = opTemplateMapper.mapToEntity(templateDTO);
    opTemplateRepository.save(template);

    return template.getId();
  }

  /**
   * Deletes template by its id
   *
   * @param templateID the templateID
   */
  public void deleteTemplateByID(String templateID) {
    opTemplateRepository.delete(opTemplateRepository.fetchById(templateID));
  }

  /**
   * Deletes Template by its name
   *
   * @param templateName the templateName
   */
  public void deleteTemplateByName(String templateName) {
    opTemplateRepository.delete(opTemplateRepository.findByName(templateName));
  }

  /**
   * Retrieves a template by its id
   *
   * @param templateID the templateID
   * @return the {@link OpTemplateDTO} by its id
   */
  public OpTemplateDTO getTemplateByID(String templateID) {
    return opTemplateMapper.mapToDTO(opTemplateRepository.fetchById(templateID));
  }

  /**
   * Retrieves the Template by its name
   *
   * @param templateName the templateName
   * @return the {@link OpTemplateDTO} by its name
   */
  public OpTemplateDTO getTemplateByName(String templateName) {
    return opTemplateMapper.mapToDTO(opTemplateRepository.findByName(templateName));
  }

  /**
   * Adds operation
   *
   * @param templateID the templateID
   * @param operationName the operationName
   * @param isDeny the isDeny checking value whether is denied or not
   */
  public void addOperation(String templateID, String operationName,
      boolean isDeny) {
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndOperationName(templateID, operationName);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
      opTemplateHasOperationRepository.save(commonAddOperation(templateID, operationName, isDeny));
    }
  }

  /**
   * Adds Operation
   *
   * @param templateID the templateID
   * @param operationName the operationName
   * @param resourceID the resourceID
   * @param isDeny whether is denied or not
   */
  public void addOperation(String templateID, String operationName,
      String resourceID, boolean isDeny) {
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(
            templateID, resourceID, operationName);
    if (tho != null) {
      tho.setDeny(isDeny);
    } else {
      tho = commonAddOperation(templateID, operationName, isDeny);
      Resource resource = resourceRepository.fetchById(resourceID);
      resource.addOpTemplateHasOperation(tho);
      opTemplateHasOperationRepository.save(tho);
    }
  }

  /**
   * Adds common operation
   *
   * @param templateID the templateID
   * @param operationName the operationName
   * @param isDeny whether is denied or not
   * @return a {@link OpTemplateHasOperation} object
   */
  private OpTemplateHasOperation commonAddOperation(String templateID, String operationName,
      boolean isDeny) {
    OpTemplate template = opTemplateRepository.fetchById(templateID);
    Operation operation = operationRepository.findByName(operationName);
    OpTemplateHasOperation tho = new OpTemplateHasOperation();
    tho.setDeny(isDeny);
    template.addOpTemplateHasOperation(tho);
    operation.addOpTemplateHasOperation(tho);
    return tho;
  }

  /**
   * Removes operation
   *
   * @param templateID the templateID
   * @param operationName the operationName
   */
  public void removeOperation(String templateID, String operationName) {
    OpTemplateHasOperation tho = opTemplateHasOperationRepository.findByTemplateIdAndOperationName(
        templateID, operationName);
    opTemplateHasOperationRepository.delete(tho);
  }

  /**
   * Removes Operation provided by its templateID, the operationName and the resourceID
   *
   * @param templateID the templateID
   * @param operationName the operationName
   * @param resourceID the resourceID
   */
  public void removeOperation(String templateID, String operationName,
      String resourceID) {
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(templateID, operationName, resourceID);
    opTemplateHasOperationRepository.delete(tho);
  }

  /**
   * Checks the access that operation template
   *
   * @param templateID the templateID
   * @param operationName the operationName
   * @return a {@link Boolean} type that checks the access of operation
   */
  public Boolean getOperationAccess(String templateID, String operationName) {
    Boolean retVal = null;

    OpTemplateHasOperation tho = opTemplateHasOperationRepository.findByTemplateIdAndOperationName(
        templateID, operationName);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  /**
   * Checks the access of operation template
   *
   * @param templateID the templateID
   * @param operationName the operationName
   * @param resourceID the resourceID
   * @return a {@link Boolean} value that checks the access of operation template
   */
  public Boolean getOperationAccess(String templateID, String operationName,
      String resourceID) {
    Boolean retVal = null;

    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(
            templateID, resourceID, operationName);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

  /**
   * Checks if any update happens to Template
   *
   * @param templateDTO the {@link OpTemplateDTO} object
   * @return a {@link Boolean} type that checks if any update happens to Template
   */
  public boolean updateTemplate(OpTemplateDTO templateDTO) {
    boolean retVal = false;
    OpTemplate template = opTemplateRepository.fetchById(templateDTO.getId());
    if (template != null) {
      template.setDescription(templateDTO.getDescription());
      template.setName(templateDTO.getName());
      retVal = true;
    }

    return retVal;
  }
}