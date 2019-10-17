package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
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
public class OpTemplateServiceTest {

  @InjectMocks
  OpTemplateService opTemplateService;

  // Repositories
  private OpTemplateRepository opTemplateRepository = mock(OpTemplateRepository.class);
  private OpTemplateHasOperationRepository opTemplateHasOperationRepository = mock(
      OpTemplateHasOperationRepository.class);
  private OperationRepository operationRepository = mock(OperationRepository.class);
  private ResourceRepository resourceRepository = mock(ResourceRepository.class);

  @Spy
  private OpTemplateMapper opTemplateMapper;

  private InitTestValues initTestValues;

  private OpTemplate template;
  private OpTemplateDTO templateDTO;

  private Operation operation;
  private OpTemplateHasOperation opTemplateHasOperation;
  private Resource resource;

  @Before
  public void init() {
    opTemplateService = new OpTemplateService(opTemplateRepository,
        opTemplateHasOperationRepository, operationRepository,
        resourceRepository, opTemplateMapper);
    initTestValues = new InitTestValues();
    template = initTestValues.createOpTemplate();
    templateDTO = initTestValues.createOpTemplateDTO();
    operation = initTestValues.createOperation();
    opTemplateHasOperation = new OpTemplateHasOperation();
    resource = new Resource();
  }

  @Test
  public void testCreateTemplate() {
    when(opTemplateMapper.mapToEntity(templateDTO)).thenReturn(template);
    String createdTemplate = opTemplateService.createTemplate(templateDTO);
    assertEquals(createdTemplate, templateDTO.getId());
    verify(opTemplateRepository, times(1)).save(template);
  }

  @Test
  public void testDeleteTemplateByID() {
    OpTemplate template2 = initTestValues.createOpTemplate();
    when(opTemplateRepository.fetchById(template.getId())).thenReturn(template2);
    opTemplateService.deleteTemplateByID(template.getId());
    verify(opTemplateRepository, times(1)).delete(template2);
  }

  @Test
  public void testDeleteTemplateByName() {
    OpTemplate template2 = initTestValues.createOpTemplate();
    when(opTemplateRepository.findByName(template.getName())).thenReturn(template2);
    opTemplateService.deleteTemplateByName(template.getName());
    verify(opTemplateRepository, times(1)).delete(template2);
  }

  @Test
  public void testGetTemplateByID() {
    when(opTemplateRepository.fetchById(template.getId())).thenReturn(template);
    when(opTemplateMapper.mapToDTO(template)).thenReturn(templateDTO);
    OpTemplateDTO foundTemplate = opTemplateService.getTemplateByID(template.getId());
    assertEquals(foundTemplate, templateDTO);
  }

  @Test
  public void testGetTemplateByName() {
    when(opTemplateRepository.findByName(template.getName())).thenReturn(template);
    when(opTemplateMapper.mapToDTO(template)).thenReturn(templateDTO);
    OpTemplateDTO foundTemplate = opTemplateService.getTemplateByName(template.getName());
    assertEquals(foundTemplate, templateDTO);
  }

  @Test
  public void testAddOperationHavingOperation() {
    when(opTemplateHasOperationRepository.findByTemplateIdAndOperationName(template.getId(),
        operation.getName())).thenReturn(opTemplateHasOperation);
    opTemplateService.addOperation(template.getId(), operation.getName(), true);
    verify(opTemplateHasOperationRepository, times(0)).save(opTemplateHasOperation);
    assertTrue(opTemplateHasOperation.isDeny());
  }


  //Tested method creates new Object...
  @Test
  public void testAddOperationNotHavingOperation() {
    when(opTemplateHasOperationRepository
        .findByTemplateIdAndOperationName(template.getId(), operation.getName())).thenReturn(null);
    when(operationRepository.findByName(operation.getName())).thenReturn(operation);
    when(opTemplateRepository.fetchById(template.getId())).thenReturn(template);
    opTemplateService.addOperation(template.getId(), operation.getName(), true);

    verify(opTemplateHasOperationRepository, times(1)).save(any());
  }

  @Test
  public void testAddOperationWithResourceHavingOperation() {

    when(opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(template.getId(), resource.getId(),
            operation.getName())).thenReturn(opTemplateHasOperation);
    opTemplateService.addOperation(template.getId(), operation.getName(), resource.getId(), true);
    verify(opTemplateHasOperationRepository, times(0)).save(opTemplateHasOperation);
    assertTrue(opTemplateHasOperation.isDeny());
  }


  //Tested method creates new Object...
  @Test
  public void testAddOperationWithResourceNotHavingOperation() {
    when(opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(template.getId(), resource.getId(),
            operation.getName())).thenReturn(null);
    when(operationRepository.findByName(operation.getName())).thenReturn(operation);
    when(opTemplateRepository.fetchById(template.getId())).thenReturn(template);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);

    opTemplateService.addOperation(template.getId(), operation.getName(), resource.getId(), true);
    verify(opTemplateHasOperationRepository, times(1)).save(any());
  }

  @Test
  public void testRemoveOperation() {
    OpTemplateHasOperation opTemplateHasOperation2 = new OpTemplateHasOperation();
    when(opTemplateHasOperationRepository.findByTemplateIdAndOperationName(template.getId(),
        operation.getName())).thenReturn(opTemplateHasOperation2);

    opTemplateService.removeOperation(template.getId(), operation.getName());

    verify(opTemplateHasOperationRepository, times(1)).delete(opTemplateHasOperation2);
  }

  @Test
  public void testRemoveOperationWithResource() {
    OpTemplateHasOperation opTemplateHasOperation2 = new OpTemplateHasOperation();
    when(opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(template.getId(), resource.getId(),
            operation.getName())).thenReturn(opTemplateHasOperation2);

    opTemplateService.removeOperation(template.getId(), resource.getId(), operation.getName());
    verify(opTemplateHasOperationRepository, times(1)).delete(opTemplateHasOperation2);
  }

  @Test
  public void testGetOperationAccess() {
    opTemplateHasOperation.setDeny(true);
    when(opTemplateHasOperationRepository.findByTemplateIdAndOperationName(template.getId(),
        operation.getName())).thenReturn(opTemplateHasOperation);
    Boolean operationAccess = opTemplateService
        .getOperationAccess(template.getId(), operation.getName());
    assertEquals(operationAccess, opTemplateHasOperation.isDeny());
  }

  @Test
  public void testGetOperationAccessWithResource() {
    opTemplateHasOperation.setDeny(true);
    when(opTemplateHasOperationRepository
        .findByTemplateIdAndResourceIdAndOperationName(template.getId(), operation.getName(),
            resource.getId())).thenReturn(opTemplateHasOperation);
    Boolean operationAccess = opTemplateService
        .getOperationAccess(template.getId(), resource.getId(), operation.getName());
    assertEquals(operationAccess, opTemplateHasOperation.isDeny());
  }

  @Test
  public void testUpdateTemplate() {
    templateDTO.setDescription("Updated description");
    templateDTO.setName("Updated name");
    when(opTemplateRepository.fetchById(templateDTO.getId())).thenReturn(template);
    boolean updated = opTemplateService.updateTemplate(templateDTO);
    assertEquals(templateDTO.getDescription(), template.getDescription());
    assertEquals(templateDTO.getName(), template.getName());
    assertTrue(updated);
  }
}
