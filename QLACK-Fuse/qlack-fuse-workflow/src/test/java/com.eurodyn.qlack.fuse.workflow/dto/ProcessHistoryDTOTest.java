package com.eurodyn.qlack.fuse.workflow.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProcessHistoryDTOTest {

  private ProcessHistoryDTO processHistoryDTO;

  @Before
  public void init() {
    processHistoryDTO = new ProcessHistoryDTO();
  }

  @Test
  public void idTest() {
    String id = "id";
    processHistoryDTO.setId(id);
    assertEquals(id, processHistoryDTO.getId());
  }

  @Test
  public void deploymentIdTest() {
    String deploymentId = "deploymentId";
    processHistoryDTO.setDeploymentId(deploymentId);
    assertEquals(deploymentId, processHistoryDTO.getDeploymentId());
  }

  @Test
  public void versionTest() {
    Integer version = 10;
    processHistoryDTO.setVersion(version);
    assertEquals(version, processHistoryDTO.getVersion());
  }

  @Test
  public void deleteReasonTest() {
    String deleteReason = "deleteReason";
    processHistoryDTO.setDeleteReason(deleteReason);
    assertEquals(deleteReason, processHistoryDTO.getDeleteReason());
  }

  @Test
  public void nameTest() {
    String name = "name";
    processHistoryDTO.setName(name);
    assertEquals(name, processHistoryDTO.getName());
  }

  @Test
  public void descriptionTest() {
    String description = "description";
    processHistoryDTO.setDescription(description);
    assertEquals(description, processHistoryDTO.getDescription());
  }

  @Test
  public void dataTest() {
    byte[] data = "data".getBytes();
    processHistoryDTO.setData(data);
    assertEquals(data, processHistoryDTO.getData());
  }
}
