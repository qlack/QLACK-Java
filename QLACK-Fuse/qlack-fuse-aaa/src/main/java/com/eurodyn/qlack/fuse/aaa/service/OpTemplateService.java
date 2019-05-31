package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.OpTemplateMapper;
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

  public String createTemplate(OpTemplateDTO templateDTO) {
    OpTemplate template = opTemplateMapper.mapToEntity(templateDTO);
    opTemplateRepository.save(template);

    return template.getId();
  }

  public void deleteTemplateByID(String templateID) {
    opTemplateRepository.delete(opTemplateRepository.fetchById(templateID));
  }

  public void deleteTemplateByName(String templateName) {
    opTemplateRepository.delete(opTemplateRepository.findByName(templateName));
  }

  public OpTemplateDTO getTemplateByID(String templateID) {
    return opTemplateMapper.mapToDTO(opTemplateRepository.fetchById(templateID));
  }

  public OpTemplateDTO getTemplateByName(String templateName) {
    return opTemplateMapper.mapToDTO(opTemplateRepository.findByName(templateName));
  }

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

    private OpTemplateHasOperation commonAddOperation(String templateID, String operationName, boolean isDeny) {
        OpTemplate template = opTemplateRepository.fetchById(templateID);
        Operation operation = operationRepository.findByName(operationName);
        OpTemplateHasOperation tho = new OpTemplateHasOperation();
        tho.setDeny(isDeny);
        template.addOpTemplateHasOperation(tho);
        operation.addOpTemplateHasOperation(tho);
        return tho;
  }

  public void removeOperation(String templateID, String operationName) {
    OpTemplateHasOperation tho = opTemplateHasOperationRepository.findByTemplateIdAndOperationName(
        templateID, operationName);
    opTemplateHasOperationRepository.delete(tho);
  }

  public void removeOperation(String templateID, String operationName,
      String resourceID) {
    OpTemplateHasOperation tho = opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(templateID, operationName, resourceID);
    opTemplateHasOperationRepository.delete(tho);
  }

  public Boolean getOperationAccess(String templateID, String operationName) {
    Boolean retVal = null;

    OpTemplateHasOperation tho = opTemplateHasOperationRepository.findByTemplateIdAndOperationName(
        templateID, operationName);
    if (tho != null) {
      retVal = tho.isDeny();
    }

    return retVal;
  }

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