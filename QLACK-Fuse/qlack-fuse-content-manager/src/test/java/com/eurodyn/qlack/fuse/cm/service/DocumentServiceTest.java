package com.eurodyn.qlack.fuse.cm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.enums.RelativesType;
import com.eurodyn.qlack.fuse.cm.exception.QAncestorFolderLockException;
import com.eurodyn.qlack.fuse.cm.exception.QDescendantNodeLockException;
import com.eurodyn.qlack.fuse.cm.exception.QInvalidPathException;
import com.eurodyn.qlack.fuse.cm.exception.QSelectedNodeLockException;
import com.eurodyn.qlack.fuse.cm.mappers.NodeMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

  @InjectMocks
  private DocumentService documentService;

  @Mock
  private ConcurrencyControlService concurrencyControlService;

  @Mock
  private VersionService versionService;

  @Mock
  private NodeRepository nodeRepository;

  @Spy
  private NodeMapper nodeMapper;

  @Spy
  private EntityManager em;

  private InitTestValues initTestValues;
  private Node node;
  private Node parent;
  private Node child;
  private Node fileChild;
  private FolderDTO nodeDTO;
  private FolderDTO parentDTO;
  private FolderDTO childDTO;
  private FileDTO fileDTO;
  private String userId;
  private ArrayList<Node> folderNodes;
  private ArrayList<FolderDTO> folderDTONodes;
  private Map<String, String> newAttributes;
  private String LOCK_TOKEN;

  @Before
  public void init() {
    documentService = new DocumentService(concurrencyControlService, versionService, nodeRepository,
        nodeMapper);

    initTestValues = new InitTestValues();
    node = initTestValues.createNode(null);
    parent = initTestValues.createNode("16874d37-1c90-4767-8e23-f78da332e89c");
    child = initTestValues.createNode("25874d37-cl92-1111-9e13-f99da113e89d");

    node.setParent(parent);
    child.setParent(node);
    node.getChildren().add(child);
    parent.getChildren().add(node);

    nodeDTO = initTestValues.createFolderDTO(null);
    nodeDTO.setParentId(parent.getId());
    parentDTO = initTestValues.createFolderDTO("16874d37-1c90-4767-8e23-f78da332e89c");
    childDTO = initTestValues.createFolderDTO("25874d37-cl92-1111-9e13-f99da113e89d");
    childDTO.setParentId(nodeDTO.getId());
    fileDTO = initTestValues.createFileDTO();

    fileChild = initTestValues.createNode(fileDTO.getId());
    fileChild.setType(NodeType.FILE);
    fileChild.setParent(child);
    child.getChildren().add(fileChild);

    userId = node.getAttribute(CMConstants.ATTR_CREATED_BY).getValue();

    folderNodes = new ArrayList<>();
    folderNodes.add(parent);
    folderNodes.add(node);
    folderNodes.add(child);

    folderDTONodes = new ArrayList<>();
    folderDTONodes.add(parentDTO);
    folderDTONodes.add(nodeDTO);
    folderDTONodes.add(childDTO);

    newAttributes = new HashMap<>();
    newAttributes.put("NODE_OWNER", "European Dynamics");
    newAttributes.put("NODE_GROUP", "Developers");

    LOCK_TOKEN = "12345";
  }

  @Test
  public void testCreateFolderWithoutParent() {
    node.setAttributes(null);
    nodeDTO.setParentId(null);
    when(nodeMapper.mapToEntity(nodeDTO, null)).thenReturn(node);
    String createdFolderId = documentService.createFolder(nodeDTO, userId, node.getLockToken());
    assertEquals(node.getId(), createdFolderId);
    assertEquals(6, node.getAttributes().size());
    verify(nodeRepository, times(0)).fetchById(any());
    verify(nodeRepository, times(1)).save(node);
  }

  @Test
  public void testCreateFolderWithParent() {
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeMapper.mapToEntity(nodeDTO, parent)).thenReturn(node);
    String createdFolderId = documentService.createFolder(nodeDTO, userId, node.getLockToken());
    assertEquals(node.getId(), createdFolderId);
    assertEquals(6, node.getAttributes().size());
    verify(nodeRepository, times(1)).fetchById(parent.getId());
    verify(nodeRepository, times(1)).save(node);
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testCreateFolderWithParentConflict() {
    node.setAttributes(null);
    String errorMsg = "An ancestor folder is locked and an invalid lock token was passed; the folder cannot be created.";

    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(concurrencyControlService
        .getAncestorFolderWithLockConflict(parent.getId(), node.getLockToken()))
        .thenReturn(parentDTO);

    documentService.createFolder(nodeDTO, userId, node.getLockToken());

    verify(nodeRepository, times(0)).save(any());
    assertNull(node.getAttributes());
  }

  @Test
  public void testDeleteFolder() {
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);

    documentService.deleteFolder(nodeDTO.getId(), node.getLockToken());

    verify(nodeRepository, times(1)).delete(node);
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testDeleteFolderWithConflict() {
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
    when(concurrencyControlService
        .getSelectedNodeWithLockConflict(nodeDTO.getId(), node.getLockToken())).thenReturn(nodeDTO);
    documentService.deleteFolder(nodeDTO.getId(), node.getLockToken());
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testDeleteFolderWithAncestorConflict() {
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
    when(concurrencyControlService
        .getAncestorFolderWithLockConflict(parent.getId(), node.getLockToken()))
        .thenReturn(parentDTO);
    documentService.deleteFolder(nodeDTO.getId(), node.getLockToken());
  }

  @Test(expected = QDescendantNodeLockException.class)
  public void testDeleteFOlderWithDescendantConflict() {
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
    when(concurrencyControlService
        .getDescendantNodeWithLockConflict(node.getId(), node.getLockToken())).thenReturn(childDTO);
    documentService.deleteFolder(nodeDTO.getId(), node.getLockToken());
  }

  @Test
  public void testRenameFolder() {
    String oldName = node.getAttribute(CMConstants.ATTR_NAME).getValue();
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
    documentService.renameFolder(nodeDTO.getId(), "New Name", userId, node.getLockToken());
    String newName = node.getAttribute(CMConstants.ATTR_NAME).getValue();
    verify(nodeRepository, times(1)).save(node);
    assertEquals("New Name", newName);
    assertNotEquals(oldName, newName);
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testRenameFolderWithConflict() {
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
    when(concurrencyControlService
        .getSelectedNodeWithLockConflict(nodeDTO.getId(), node.getLockToken())).thenReturn(nodeDTO);
    documentService.renameFolder(nodeDTO.getId(), "New Name", userId, node.getLockToken());
    verify(nodeRepository, times(0)).save(node);
    assertEquals("New Name", node.getAttribute(CMConstants.ATTR_NAME).getValue());
  }

  @Test
  public void testGetFolderById() {
    when(nodeRepository.fetchById(parentDTO.getId())).thenReturn(parent);

    documentService.getFolderByID(parentDTO.getId(), true, false);

    verify(nodeRepository, times(1)).fetchById(parentDTO.getId());
    verify(nodeMapper, times(1)).mapToFolderDTO(parent, RelativesType.LAZY, false);
  }

  @Test
  public void testGetFolderAsZipNoChildren() {
    node.setChildren(new ArrayList<>());
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);

    byte[] folderAsZip = documentService.getFolderAsZip(nodeDTO.getId(), true, true);

    verify(nodeRepository, times(1)).fetchById(any());
    assertNotNull(folderAsZip);
  }

  @Test
  public void testGetFolderAsZipWithChildren() {
    for (Node n : folderNodes) {
      when(nodeRepository.fetchById(n.getId())).thenReturn(n);
    }

    byte[] folderAsZip = documentService.getFolderAsZip(parentDTO.getId(), true, true);
    verify(nodeRepository, times(folderNodes.size())).fetchById(any());
    assertNotNull(folderAsZip);
  }

  @Test
  public void testGetFolderAsZipWithChildrenOfBothTypes() {
    for (Node n : folderNodes) {
      when(nodeRepository.fetchById(n.getId())).thenReturn(n);
    }

    when(versionService.getFileAsZip(fileChild.getId(), true)).thenReturn(new byte[1]);
    byte[] folderAsZip = documentService.getFolderAsZip(parentDTO.getId(), true, true);

    verify(nodeRepository, times(folderNodes.size())).fetchById(any());
    verify(versionService, times(1)).getFileAsZip(fileChild.getId(), true);
    assertNotNull(folderAsZip);
  }

  @Test
  public void testCreateFile() {
    fileDTO.setParentId(null);
    when(nodeMapper.mapToEntity(fileDTO, null)).thenReturn(fileChild);
    String fileId = documentService.createFile(fileDTO, fileDTO.getCreatedBy(), null);

    assertEquals(fileChild.getId(), fileId);
    assertEquals(fileDTO.getMimetype(), fileChild.getMimetype());
    assertEquals(fileDTO.getSize(), fileChild.getSize());
    assertEquals(6, fileChild.getAttributes().size());
    verify(nodeRepository, times(1)).save(fileChild);
  }

  @Test
  public void testCreateFileWithNonExistingParentId() {
    fileDTO.setParentId("12345667");
    when(nodeMapper.mapToEntity(fileDTO, null)).thenReturn(fileChild);
    String fileId = documentService.createFile(fileDTO, fileDTO.getCreatedBy(), null);

    verify(concurrencyControlService, times(0)).getAncestorFolderWithLockConflict(any(), any());
    assertEquals(fileChild.getId(), fileId);
    assertEquals(fileDTO.getMimetype(), fileChild.getMimetype());
    assertEquals(fileDTO.getSize(), fileChild.getSize());
    assertEquals(6, fileChild.getAttributes().size());
    verify(nodeRepository, times(1)).save(fileChild);
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testCreateFileWithConflictingParent() {
    fileDTO.setParentId(child.getId());
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(parentDTO);
    String fileId = documentService.createFile(fileDTO, fileDTO.getCreatedBy(), LOCK_TOKEN);
  }

  @Test
  public void testCreateFileWithParent() {
    fileDTO.setParentId(child.getId());
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(nodeMapper.mapToEntity(fileDTO, child)).thenReturn(fileChild);

    String fileId = documentService.createFile(fileDTO, fileDTO.getCreatedBy(), LOCK_TOKEN);

    verify(concurrencyControlService, times(1))
        .getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN);
    assertEquals(fileChild.getId(), fileId);
    assertEquals(fileDTO.getMimetype(), fileChild.getMimetype());
    assertEquals(fileDTO.getSize(), fileChild.getSize());
    assertEquals(6, fileChild.getAttributes().size());
    verify(nodeRepository, times(1)).save(fileChild);
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testDeleteFileWithConflict() {
    when(nodeRepository.fetchById(fileDTO.getId())).thenReturn(fileChild);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(fileDTO.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);

    documentService.deleteFile(fileDTO.getId(), LOCK_TOKEN);

    verify(nodeRepository, times(0)).delete(fileChild);
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testDeleteFileWithParentConflict() {
    when(nodeRepository.fetchById(fileDTO.getId())).thenReturn(fileChild);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(fileDTO.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(parentDTO);

    documentService.deleteFile(fileDTO.getId(), LOCK_TOKEN);
    verify(nodeRepository, times(0)).delete(fileChild);
  }

  @Test
  public void testDeleteFileWithParent() {
    when(nodeRepository.fetchById(fileDTO.getId())).thenReturn(fileChild);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(fileDTO.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.deleteFile(fileDTO.getId(), LOCK_TOKEN);
    verify(nodeRepository, times(1)).delete(fileChild);
  }

  @Test
  public void testRenameFile() {
    String oldName = fileChild.getAttribute(CMConstants.ATTR_NAME).getValue();

    when(nodeRepository.fetchById(fileDTO.getId())).thenReturn(fileChild);

    documentService.renameFolder(fileDTO.getId(), "New Name", userId, node.getLockToken());
    String newName = fileChild.getAttribute(CMConstants.ATTR_NAME).getValue();

    verify(nodeRepository, times(1)).save(fileChild);
    assertEquals("New Name", newName);
    assertNotEquals(oldName, newName);
  }

  @Test
  public void testGetParent() {
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
    when(nodeMapper.mapToFolderDTO(node.getParent(), RelativesType.LAZY, false))
        .thenReturn(parentDTO);

    FolderDTO fetchedParentDTO = documentService.getParent(nodeDTO.getId(), true);
    verify(nodeMapper, times(1)).mapToFolderDTO(node.getParent(), RelativesType.LAZY, false);
    assertEquals(parentDTO, fetchedParentDTO);
  }

  @Test
  public void testGetFileByID() {
    when(nodeRepository.fetchById(fileDTO.getId())).thenReturn(fileChild);
    when(nodeMapper.mapToFileDTO(fileChild, false)).thenReturn(fileDTO);

    FileDTO fileByID = documentService.getFileByID(fileDTO.getId(), false, false);
    verify(versionService, times(0)).getFileVersions(fileDTO.getId());
    assertEquals(fileDTO, fileByID);
  }

  @Test
  public void testGetFileByIdWithVersions() {
    List<VersionDTO> versionsDTO = new ArrayList<>();
    versionsDTO.add(new VersionDTO());

    when(nodeRepository.fetchById(fileDTO.getId())).thenReturn(fileChild);
    when(nodeMapper.mapToFileDTO(fileChild, false)).thenReturn(fileDTO);
    when(versionService.getFileVersions(fileDTO.getId())).thenReturn(versionsDTO);

    FileDTO fileByID = documentService.getFileByID(fileDTO.getId(), true, false);

    verify(versionService, times(1)).getFileVersions(fileDTO.getId());
    assertEquals(fileDTO, fileByID);
    assertEquals(versionsDTO, fileByID.getVersions());
  }

  @Test
  public void testGetNodeByID() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(nodeMapper.mapToDTO(node, true)).thenReturn(nodeDTO);

    NodeDTO nodeByID = documentService.getNodeByID(node.getId());
    assertEquals(nodeDTO, nodeByID);
  }

  @Test
  public void testGetAncestors() {
    List<Node> allNodes = new ArrayList<>(folderNodes);
    allNodes.add(fileChild);

    allNodes.forEach(n -> {
      when(nodeRepository.fetchById(n.getId())).thenReturn(n);
    });

    for (int i = 0; i < folderNodes.size(); i++) {
      when(nodeMapper.mapToFolderDTO(folderNodes.get(i), RelativesType.LAZY, false))
          .thenReturn(folderDTONodes.get(i));
    }

    List<FolderDTO> ancestors = documentService.getAncestors(fileChild.getId());
    assertEquals(folderDTONodes, ancestors);
  }

  @Test
  public void testGetAncestorsForNodeWithoutParent() {
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    List<FolderDTO> ancestors = documentService.getAncestors(parent.getId());

    assertEquals(0, ancestors.size());
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testCreateAttributeForConflictedNode() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(nodeDTO);

    documentService
        .createAttribute(node.getId(), "TEST_ATTRIBUTE", "test value", "User1", LOCK_TOKEN);
  }

  @Test
  public void testCreateAttributeWithoutUserId() {
    int attributes = node.getAttributes().size();
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    String attributeId = documentService
        .createAttribute(node.getId(), "TEST_ATTRIBUTE", "test value", null, LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(node);
    assertNotNull(node.getAttribute("TEST_ATTRIBUTE"));
    assertTrue(node.getAttributes().size() == ++attributes);
  }

  @Test
  public void testCreateAttributeWithUserId() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    String attributeId = documentService
        .createAttribute(node.getId(), "TEST_ATTRIBUTE", "test value", "user2", LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(node);
    assertEquals(node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY).getValue(), "user2");
    assertNotNull(node.getAttribute("TEST_ATTRIBUTE"));
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testUpdateAttributeForConflictedNode() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(nodeDTO);

    documentService
        .updateAttribute(node.getId(), "TEST_ATTRIBUTE", "new value", "user2", LOCK_TOKEN);
  }

  @Test
  public void testUpdateAttributeWithoutUserId() {
    node.setAttribute("TEST_ATTRIBUTE", "old value");
    String oldValue = node.getAttribute("TEST_ATTRIBUTE").getValue();
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService
        .updateAttribute(node.getId(), "TEST_ATTRIBUTE", "updated value", null, LOCK_TOKEN);
    String newValue = node.getAttribute("TEST_ATTRIBUTE").getValue();
    verify(nodeRepository, times(1)).save(node);
    assertNotEquals(oldValue, newValue);
    assertEquals("updated value", newValue);
  }

  @Test
  public void testUpdateAttributeWithUserId() {
    node.setAttribute("TEST_ATTRIBUTE", "old value");
    String oldValue = node.getAttribute("TEST_ATTRIBUTE").getValue();

    node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, "user1");
    String oldUser = node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY).getValue();

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService
        .updateAttribute(node.getId(), "TEST_ATTRIBUTE", "updated value", "user3", LOCK_TOKEN);
    String newValue = node.getAttribute("TEST_ATTRIBUTE").getValue();
    String newUser = node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY).getValue();

    verify(nodeRepository, times(1)).save(node);
    assertNotEquals(oldValue, newValue);
    assertEquals("updated value", node.getAttribute("TEST_ATTRIBUTE").getValue());
    assertNotEquals(oldUser, newUser);
    assertEquals("user3", newUser);
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testUpdateAttributesForConflictingNode() {
    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(nodeDTO);

    documentService.updateAttributes(node.getId(), newAttributes, "user3", LOCK_TOKEN);
  }

  @Test
  public void testUpdateAttributesWithoutUserId() {
    node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, "user1");

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.updateAttributes(node.getId(), newAttributes, null, LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(node);
    newAttributes.forEach((s, s2) -> assertNotNull(node.getAttribute(s)));
    assertNotNull(node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
  }

  @Test
  public void testUpdateAttributesWithUserId() {
    node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, "user1");

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.updateAttributes(node.getId(), newAttributes, "user3", LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(node);
    newAttributes.forEach((s, s2) -> assertNotNull(node.getAttribute(s)));
    assertEquals("user3", node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY).getValue());
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testDeleteAttributeForConflictingNode() {
    when(nodeRepository.fetchById(any())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(anyString(), anyString()))
        .thenReturn(nodeDTO);

    documentService
        .deleteAttribute(node.getId(), CMConstants.ATTR_LAST_MODIFIED_BY, "user1", LOCK_TOKEN);
  }

  @Test
  public void testDeleteAttributeWithoutUser() {
    node.setAttribute(CMConstants.VERSIONABLE, "false");
    node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userId);

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.deleteAttribute(node.getId(), CMConstants.VERSIONABLE, null, LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(node);
    assertEquals(userId, node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY).getValue());
  }

  @Test
  public void testDeleteAttributeWithUser() {
    node.setAttribute(CMConstants.VERSIONABLE, "false");
    node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userId);

    when(nodeRepository.fetchById(node.getId())).thenReturn(node);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(node.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.deleteAttribute(node.getId(), CMConstants.VERSIONABLE, "User2", LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(node);
    assertEquals("User2", node.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY).getValue());
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testCopyConflictedDestinationNode() {
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(parentDTO);

    documentService.copy(child.getId(), parent.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(0)).save(any());
  }

  @Test(expected = QInvalidPathException.class)
  public void testCopyCyclicPath() {
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.copy(parent.getId(), child.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(0)).save(any());
  }

  @Test
  public void testCopy() {
    int nodes = child.getChildren().size() + 1;

    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(null);

    String nodeCopyId = documentService.copy(child.getId(), parent.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(nodes)).save(any());
    verify(nodeRepository, times(0)).save(child);
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testMoveNodeWithConflict() {
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(childDTO);

    documentService.move(child.getId(), parent.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(0)).save(any());
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testMoveNodeToParentWithConflict() {
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(parentDTO);

    documentService.move(child.getId(), parent.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(0)).save(any());
  }

  @Test(expected = QInvalidPathException.class)
  public void testMoveNodeWithCyclicPath() {
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.move(parent.getId(), child.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(0)).save(any());
  }

  @Test()
  public void testMoveNode() {
    when(nodeRepository.fetchById(parent.getId())).thenReturn(parent);
    when(nodeRepository.fetchById(child.getId())).thenReturn(child);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(child.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(null);

    documentService.move(child.getId(), parent.getId(), userId, LOCK_TOKEN);

    verify(nodeRepository, times(1)).save(child);
    assertEquals(parent, child.getParent());
  }

  @Test()
  public void testCreateFileAndVersion() {

    when(nodeMapper.mapToEntity(fileDTO, null)).thenReturn(fileChild);
    fileDTO.setParentId(null);
    fileChild.setAttributes(new ArrayList<>());

    VersionDTO versionDTO = new VersionDTO();
    versionDTO.setFilename("Test File");
    byte[] content = new byte[3];
    Arrays.fill(content, (byte) 1);
    documentService.createFileAndVersion(fileDTO, versionDTO, content, userId, LOCK_TOKEN);
  }
}
