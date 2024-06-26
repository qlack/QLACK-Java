package com.eurodyn.qlack.fuse.cm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.enums.RelativesType;
import com.eurodyn.qlack.fuse.cm.exception.QAncestorFolderLockException;
import com.eurodyn.qlack.fuse.cm.exception.QDescendantNodeLockException;
import com.eurodyn.qlack.fuse.cm.exception.QNodeLockException;
import com.eurodyn.qlack.fuse.cm.exception.QSelectedNodeLockException;
import com.eurodyn.qlack.fuse.cm.mapper.NodeMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class ConcurrencyControlServiceTest {

  @InjectMocks
  private ConcurrencyControlService concurrencyControlService;

  @Spy
  private NodeMapper nodeMapper;

  @Mock
  private NodeRepository nodeRepository;

  private InitTestValues initTestValues;
  private Node node;
  private Node parent;
  private Node child;
  private Node file;
  private FolderDTO nodeDTO;
  private FolderDTO parentDTO;
  private FolderDTO childDTO;
  private FileDTO fileDTO;
  private String userId;
  private ArrayList<Node> allNodes;
  private String LOCK_TOKEN;

  @BeforeEach
  public void init() {
    concurrencyControlService = new ConcurrencyControlService(nodeMapper,
      nodeRepository);

    initTestValues = new InitTestValues();
    node = initTestValues.createNode(null);
    parent = initTestValues.createNode("16874d37-1c90-4767-8e23-f78da332e89c");
    child = initTestValues.createNode("25874d37-cl92-1111-9e13-f99da113e89d");

    fileDTO = initTestValues.createFileDTO();
    file = initTestValues.createNode(fileDTO.getId());
    file.setType(NodeType.FILE);

    node.setParent(parent);
    child.setParent(node);
    node.getChildren().add(child);
    parent.getChildren().add(node);

    parentDTO = initTestValues
      .createFolderDTO("16874d37-1c90-4767-8e23-f78da332e89c");
    nodeDTO = initTestValues.createFolderDTO(null);
    childDTO = initTestValues
      .createFolderDTO("25874d37-cl92-1111-9e13-f99da113e89d");

    userId = node.getAttribute(CMConstants.ATTR_CREATED_BY).getValue();

    allNodes = new ArrayList<>();
    allNodes.add(parent);
    allNodes.add(node);
    allNodes.add(child);

    LOCK_TOKEN = "12345";

  }

  @Test
  public void testLockNotLockableNode() {
    assertThrows(QNodeLockException.class, () -> {
      when(nodeRepository.fetchById(node.getId())).thenReturn(node);
      node.setAttribute(CMConstants.LOCKABLE, "false");
      concurrencyControlService.lock(node.getId(), "token123", false, userId);
    });
  }

  @Test
  public void testLockNodeConflict() {
    assertThrows(QSelectedNodeLockException.class, () -> {
      node.setLockToken("lockToken");
      when(nodeRepository.fetchById(node.getId())).thenReturn(node);
      when(nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false))
              .thenReturn(nodeDTO);
      concurrencyControlService.lock(node.getId(), "token123", false, userId);
    });
  }

  @Test
  public void testLockNodeWithoutAncestorsAndChildren() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);

    node.setParent(null);
    node.setChildren(new ArrayList<>());

    concurrencyControlService.lock(node.getId(), "token123", false, userId);
    verify(nodeRepository, times(1)).save(node);
    assertEquals(userId,
      node.getAttribute(CMConstants.ATTR_LOCKED_BY).getValue());
    assertNotNull(node.getAttribute(CMConstants.ATTR_LOCKED_ON).getValue());
  }

  @Test
  public void testLockNodeWithParentConflict() {
    assertThrows(QAncestorFolderLockException.class, () -> {
      parent.setLockToken("lockToken");

      when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
      when(nodeRepository.fetchById(node.getId())).thenReturn(node);
      when(nodeMapper.mapToFolderDTO(parent, RelativesType.LAZY, false))
              .thenReturn(parentDTO);
      concurrencyControlService.lock(node.getId(), "token123", false, userId);
    });
  }

  @Test
  public void testLockNodeWithChildConflict() {
    assertThrows(QDescendantNodeLockException.class, () -> {
      parent.setLockToken("token123");
      child.setLockToken(LOCK_TOKEN);

      node.getChildren().add(child);

      when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
      when(nodeRepository.fetchById(node.getId())).thenReturn(node);
      when(nodeMapper.mapToFolderDTO(child, RelativesType.LAZY, false))
              .thenReturn(childDTO);
      concurrencyControlService.lock(node.getId(), "token123", false, userId);
    });
  }

  @Test
  public void testLock() {
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    concurrencyControlService.lock(node.getId(), "token123", true, userId);

    verify(nodeRepository, times(1)).save(node);
    assertEquals(userId,
      node.getAttribute(CMConstants.ATTR_LOCKED_BY).getValue());
    assertNotNull(node.getAttribute(CMConstants.ATTR_LOCKED_ON).getValue());

    node.getChildren().forEach(n -> {
      verify(nodeRepository, times(1)).save(n);
      assertEquals(userId,
        n.getAttribute(CMConstants.ATTR_LOCKED_BY).getValue());
      assertNotNull(n.getAttribute(CMConstants.ATTR_LOCKED_ON).getValue());
    });
  }

  @Test
  public void testUnlockWithoutConflict() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    node.setAttribute(CMConstants.ATTR_LOCKED_ON, anyString());
    node.setAttribute(CMConstants.ATTR_LOCKED_BY, userId);

    concurrencyControlService
      .unlock(node.getId(), node.getLockToken(), false, userId);

    assertNull(node.getLockToken());
    assertNull(node.getAttribute(CMConstants.ATTR_LOCKED_ON));
    assertNull(node.getAttribute(CMConstants.ATTR_LOCKED_BY));
    verify(nodeRepository, times(1)).saveAndFlush(node);
  }

  @Test
  public void testUnlockWithConflict() {
    assertThrows(QSelectedNodeLockException.class, () -> {
      when(nodeRepository.fetchById(node.getId())).thenReturn(node);
      when(nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false))
              .thenReturn(nodeDTO);
      node.setAttribute(CMConstants.ATTR_LOCKED_ON, anyString());
      node.setAttribute(CMConstants.ATTR_LOCKED_BY, userId);
      node.setLockToken(LOCK_TOKEN);

      concurrencyControlService.unlock(node.getId(), "438972", false, userId);

      verify(nodeRepository, times(0)).save(node);
    });
  }

  @Test
  public void testUnlockOverrideLock() {
    String NEW_LOCK_TOKEN = "222222";
    node.setLockToken(LOCK_TOKEN);
    node.setAttribute(CMConstants.ATTR_LOCKED_BY, userId);
    node.setAttribute(CMConstants.ATTR_LOCKED_ON,
      String.valueOf(Calendar.getInstance().getTimeInMillis()));
    node.setAttribute(CMConstants.ATTR_LOCKED_BY, userId);
    node.setParent(null);
    node.setChildren(new ArrayList<>());

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);

    concurrencyControlService
      .unlock(node.getId(), NEW_LOCK_TOKEN, true, "user2");

    verify(nodeRepository, times(1)).saveAndFlush(node);
    verify(nodeRepository, times(1)).save(node);
    assertEquals(NEW_LOCK_TOKEN, node.getLockToken());
  }

  @Test
  public void testGetSelectedFolderWithConflict() {
    node.setLockToken(LOCK_TOKEN);

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false))
      .thenReturn(nodeDTO);

    NodeDTO nodeWithConflict = concurrencyControlService
      .getSelectedNodeWithLockConflict(node.getId(), "differentLockToken");

    verify(nodeRepository, times(1)).fetchById(anyString());
    verify(nodeMapper, times(1))
      .mapToFolderDTO(node, RelativesType.LAZY, false);
    assertEquals(nodeDTO.getId(), nodeWithConflict.getId());
  }

  @Test
  public void testGetSelectedFileWithConflict() {
    file.setLockToken(LOCK_TOKEN);

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(nodeMapper.mapToFileDTO(file, false)).thenReturn(fileDTO);

    NodeDTO nodeWithConflict = concurrencyControlService
      .getSelectedNodeWithLockConflict(file.getId(), "differentLockToken");

    verify(nodeRepository, times(1)).fetchById(anyString());
    verify(nodeMapper, times(1)).mapToFileDTO(file, false);
    assertEquals(file.getId(), nodeWithConflict.getId());
  }

  @Test
  public void testGetSelectedNodeWithoutConflict() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);

    NodeDTO nodeWithConflict = concurrencyControlService
      .getSelectedNodeWithLockConflict(node.getId(), node.getLockToken());

    verify(nodeRepository, times(1)).fetchById(anyString());
    verify(nodeMapper, times(0)).mapToDTO(node, true);
    assertNull(nodeWithConflict);
  }

  @Test
  public void testGetAncestorFolderWithLockConflict() {
    parent.setLockToken("lockToken");

    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);

    when(nodeMapper.mapToFolderDTO(parent, RelativesType.LAZY, false))
      .thenReturn(parentDTO);

    FolderDTO ancestorNodeWithConflict = concurrencyControlService
      .getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN);

    allNodes
      .forEach(n -> verify(nodeRepository, times(1)).fetchById(n.getId()));
    assertEquals(parentDTO, ancestorNodeWithConflict);
  }

  @Test
  public void testGetAncestorFolderWithoutLockConflict() {

    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);

    FolderDTO ancestorNodeWithConflict = concurrencyControlService
      .getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN);

    allNodes
      .forEach(n -> verify(nodeRepository, times(1)).fetchById(n.getId()));
    verify(nodeMapper, times(0)).mapToDTO(any(), anyBoolean());
    assertNull(ancestorNodeWithConflict);
  }


  @Test
  public void testGetCheckDescendantNodeWithLockConflict() {
    child.setLockToken("lockToken");
    allNodes.remove(child);

    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeMapper.mapToFolderDTO(child, RelativesType.LAZY, false))
      .thenReturn(childDTO);

    NodeDTO descendantWithConflict = concurrencyControlService
      .getDescendantNodeWithLockConflict(parent.getId(), LOCK_TOKEN);

    allNodes
      .forEach(n -> verify(nodeRepository, times(1)).fetchById(n.getId()));
    assertEquals(childDTO, descendantWithConflict);
  }

  @Test
  public void testGetCheckDescendantNodeWithoutLockConflict() {
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);

    NodeDTO descendantWithConflict = concurrencyControlService
      .getDescendantNodeWithLockConflict(parent.getId(), LOCK_TOKEN);

    allNodes
      .forEach(n -> verify(nodeRepository, times(1)).fetchById(n.getId()));
    assertNull(descendantWithConflict);
  }

  @Test
  public void lockNodeConflictWithoutName() {
    FolderDTO ancNode = initTestValues.createFolderDTO(null);
    ancNode.setId(null);
    node.setLockToken("lockToken");
    nodeDTO.setName(null);

    Node childDescNode = initTestValues.createNode("null");
    childDescNode.setChildren(Collections.emptyList());
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeRepository.fetchById(child.getId())).thenReturn(childDescNode);
    when(nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false))
      .thenReturn(nodeDTO)
      .thenReturn(ancNode);

    concurrencyControlService.lock(node.getId(), "token123", false, userId);
    verify(nodeRepository, times(1)).save(any(Node.class));
  }

  @Test
  public void lockNodeWithChildConflictWithoutTest() {
    parent.setLockToken("token123");
    child.setLockToken(LOCK_TOKEN);
    node.getChildren().add(child);
    childDTO.setId(null);

    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeMapper.mapToFolderDTO(child, RelativesType.LAZY, false))
      .thenReturn(childDTO);
    concurrencyControlService.lock(node.getId(), "token123", false, userId);
    verify(nodeRepository, times(1)).save(any(Node.class));
  }

  @Test
  public void unlockWithConflictAndNullIdTest() {
    nodeDTO.setName(null);
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false))
      .thenReturn(nodeDTO);
    node.setAttribute(CMConstants.ATTR_LOCKED_ON, anyString());
    node.setAttribute(CMConstants.ATTR_LOCKED_BY, userId);
    node.setLockToken(LOCK_TOKEN);

    concurrencyControlService.unlock(node.getId(), "438972", false, userId);

    verify(nodeRepository, times(0)).save(node);
  }

  @Test
  public void getSelectedFileWithConflictWithNullType() {
    file.setLockToken(LOCK_TOKEN);

    file.setType(null);
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);

    concurrencyControlService
      .getSelectedNodeWithLockConflict(file.getId(), "differentLockToken");

    verify(nodeRepository, times(1)).fetchById(anyString());
  }

}
