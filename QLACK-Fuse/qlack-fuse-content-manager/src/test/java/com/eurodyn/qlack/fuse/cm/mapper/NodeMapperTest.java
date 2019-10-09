package com.eurodyn.qlack.fuse.cm.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeAttributeDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.enums.RelativesType;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeMapperTest {

  @InjectMocks
  private NodeMapperImpl nodeMapper;

  private InitTestValues initTestValues;
  private NodeDTO nodeDTO;
  private Node node;
  private List<NodeDTO> nodeDTOList;
  private List<Node> nodeList;
  private NodeAttribute nodeAttribute;
  private NodeAttributeDTO nodeAttributeDTO;
  private List<NodeAttribute> nodeAttributeList;
  private Set<NodeAttributeDTO> nodeAttributeDTOSet;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    nodeDTO = initTestValues.createFolderDTO(null);
    node = initTestValues.createNode("nodeId");
    nodeDTOList = initTestValues.createListNodeDTO();
    nodeAttributeList = initTestValues.createAttributes(node);
    nodeAttributeDTO = initTestValues.createNodeAttributeDTO();
    nodeAttributeDTOSet = initTestValues.createNodeAttributesDTOSet(nodeAttributeDTO);
    nodeList = initTestValues.createListNode();
  }

  @Test
  public void mapToEntityTest() {
    Node node = nodeMapper.mapToEntity(nodeDTO);
    assertEquals(nodeDTO.getId(), node.getId());
  }

  @Test
  public void mapToEntityNullTest() {
    Node node = nodeMapper.mapToEntity((NodeDTO) null);
    assertNull(node);
  }

  @Test
  public void mapToEntityListNullTest() {
    List<Node> nodeList = nodeMapper.mapToEntity((List<NodeDTO>) null);
    assertNull(nodeList);
  }

  @Test
  public void mapToEntityListTest() {
    List<Node> nodeList = nodeMapper.mapToEntity(nodeDTOList);
    assertEquals(nodeDTOList.size(), nodeList.size());
  }

  @Test
  public void mapToEntityParentNullTest() {
    Node node = nodeMapper.mapToEntity(null, this.node);
    assertNull(node);
  }

  @Test
  public void mapToEntityParentTest() {
    Node node = nodeMapper.mapToEntity(nodeDTO, this.node);
    assertEquals(this.node.getId(), node.getParent().getId());
  }

  @Test
  public void mapToDTOPathNullTest() {
    NodeDTO nodeDTO = nodeMapper.mapToDTO(null, false);
    assertNull(nodeDTO);
  }

  @Test
  public void mapToDTOPathTest() {
    NodeDTO nodeDTO = nodeMapper.mapToDTO(node, false);
    assertEquals(node.getId(), nodeDTO.getId());
  }

  @Test
  public void mapToDTOTest() {
    NodeDTO nodeDTO = nodeMapper.mapToDTO(node);
    assertEquals(node.getId(), nodeDTO.getId());
  }

  @Test
  public void mapToDTONullTest() {
    NodeDTO nodeDTO = nodeMapper.mapToDTO((Node) null);
    assertNull(nodeDTO);
  }

  @Test
  public void mapToFolderDTONullTest() {
    FolderDTO folderDTO = nodeMapper.mapToFolderDTO(null, RelativesType.LAZY, false);
    assertNull(folderDTO);
  }

  @Test
  public void mapToFolderDTOTest() {
    node.setParent(node);
    FolderDTO folderDTO = nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false);
    assertEquals(node.getId(), folderDTO.getId());
  }

  @Test
  public void mapToFolderDTOParentNullTest() {
    Node nodeParent = initTestValues.createNode("parentNode");
    nodeParent.setId(null);
    node.setParent(nodeParent);
    FolderDTO folderDTO = nodeMapper.mapToFolderDTO(node, RelativesType.LAZY, false);
    assertEquals(nodeParent.getId(), folderDTO.getParentId());
  }

  @Test
  public void mapToFileDTONullTest() {
    FileDTO fileDTO = nodeMapper.mapToFileDTO(null, false);
    assertNull(fileDTO);
  }

  @Test
  public void mapToFileDTOTest() {
    FileDTO fileDTO = nodeMapper.mapToFileDTO(node, false);
    assertEquals(node.getId(), fileDTO.getId());
  }

  @Test
  public void nodeAttributeToNodeAttributeDTONullTest() {
    NodeAttributeDTO nodeAttributeDTO = nodeMapper.nodeAttributeToNodeAttributeDTO(null);
    assertNull(nodeAttributeDTO);
  }

  @Test
  public void nodeAttributeListToNodeAttributeDTOSetNullTest() {
    Set<NodeAttributeDTO> nodeAttributeDTOSet = nodeMapper.
        nodeAttributeListToNodeAttributeDTOSet(null);
    assertNull(nodeAttributeDTOSet);
  }

  @Test
  public void nodeAttributeDTOToNodeAttributeNullTest() {
    NodeAttribute nodeAttribute = nodeMapper.
        nodeAttributeDTOToNodeAttribute(null);
    assertNull(nodeAttribute);
  }

  @Test
  public void nodeAttributeDTOToNodeAttributeTest() {
    NodeAttribute nodeAttribute = nodeMapper.
        nodeAttributeDTOToNodeAttribute(nodeAttributeDTO);
    assertEquals(nodeAttributeDTO.getName(), nodeAttribute.getName());
  }

  @Test
  public void nodeAttributeDTOSetToNodeAttributeListTest() {
    List<NodeAttribute> nodeAttributeList = nodeMapper.
        nodeAttributeDTOSetToNodeAttributeList(nodeAttributeDTOSet);
    assertEquals(nodeAttributeDTOSet.size(), nodeAttributeList.size());
  }

  @Test
  public void nodeAttributeToNodeAttributeDTO1NullTest() {
    NodeAttributeDTO nodeAttributeDTO = nodeMapper.
        nodeAttributeToNodeAttributeDTO1(null, false);
    assertNull(nodeAttributeDTO);
  }

  @Test
  public void nodeAttributeListToNodeAttributeDTOSet1NullTest() {
    Set<NodeAttributeDTO> nodeAttributeDTOSet1 = nodeMapper.
        nodeAttributeListToNodeAttributeDTOSet1(null, false);
    assertNull(nodeAttributeDTOSet1);
  }

  @Test
  public void nodeAttributeToNodeAttributeDTO2NullTest() {
    NodeAttributeDTO nodeAttributeDTO2 = nodeMapper.
        nodeAttributeToNodeAttributeDTO2(null, RelativesType.EAGER, false);
    assertNull(nodeAttributeDTO2);
  }

  @Test
  public void nodeAttributeListToNodeAttributeDTOSet2NullTest() {
    Set<NodeAttributeDTO> nodeAttributeDTOSet2 = nodeMapper.
        nodeAttributeListToNodeAttributeDTOSet2(null, RelativesType.EAGER, false);
    assertNull(nodeAttributeDTOSet2);
  }

  @Test
  public void nodeAttributeDTOToNodeAttribute1NullTest() {
    NodeAttribute nodeAttribute1 = nodeMapper.
        nodeAttributeDTOToNodeAttribute1(null, node);
    assertNull(nodeAttribute1);
  }

  @Test
  public void nodeAttributeDTOToNodeAttribute1Test() {
    NodeAttribute nodeAttribute1 = nodeMapper.
        nodeAttributeDTOToNodeAttribute1(nodeAttributeDTO, node);
    assertEquals(nodeAttributeDTO.getName(), nodeAttribute1.getName());
  }

  @Test
  public void nodeAttributeDTOSetToNodeAttributeList1NullTest() {
    List<NodeAttribute> nodeAttributeList1 = nodeMapper.
        nodeAttributeDTOSetToNodeAttributeList1(null, node);
    assertNull(nodeAttributeList1);
  }

  @Test
  public void nodeAttributeDTOSetToNodeAttributeList1Test() {
    List<NodeAttribute> nodeAttributeList1 = nodeMapper.
        nodeAttributeDTOSetToNodeAttributeList1(nodeAttributeDTOSet, node);
    assertEquals(nodeAttributeDTOSet.size(), nodeAttributeList1.size());
  }

  @Test
  public void mapToDTOListTest() {
    List<NodeDTO> nodesDTO = nodeMapper.mapToDTO(nodeList);
    assertEquals(nodesDTO.size(), nodeList.size());
  }

  @Test
  public void mapLastModifiedOnTest() {
    nodeAttributeList.get(0).setName(CMConstants.ATTR_LAST_MODIFIED_ON);
    nodeAttributeList.get(0).setValue("2019");
    long result = nodeMapper.mapLastModifiedOn(nodeAttributeList);
    assertEquals(Long.parseLong(nodeAttributeList.get(0).getValue()), result);
  }

  @Test
  public void mapLastModifiedByTest() {
    nodeAttributeList.get(0).setName(CMConstants.ATTR_LAST_MODIFIED_BY);
    nodeAttributeList.get(0).setValue("user");
    String result = nodeMapper.mapLastModifiedBy(nodeAttributeList);
    assertEquals(nodeAttributeList.get(0).getValue(), result);
  }

  @Test
  public void mapLockedOnTest() {
    nodeAttributeList.get(0).setName(CMConstants.ATTR_LOCKED_ON);
    nodeAttributeList.get(0).setValue("2019");
    long result = nodeMapper.mapLockedOn(nodeAttributeList);
    assertEquals(Long.parseLong(nodeAttributeList.get(0).getValue()), result);
  }

  @Test
  public void mapLockedByTest() {
    nodeAttributeList.get(0).setName(CMConstants.ATTR_LOCKED_BY);
    nodeAttributeList.get(0).setValue("user1");
    String result = nodeMapper.mapLockedBy(nodeAttributeList);
    assertEquals(nodeAttributeList.get(0).getValue(), result);
  }

  @Test
  public void mapVersionableTest() {
    nodeAttributeList.get(0).setName(CMConstants.VERSIONABLE);
    nodeAttributeList.get(0).setValue("true");
    boolean result = nodeMapper.mapVersionable(nodeAttributeList);
    assertEquals(Boolean.valueOf(nodeAttributeList.get(0).getValue()), result);
  }

  @Test
  public void setTypeTest() {
    nodeMapper.setType(nodeDTO, node);
    assertTrue(nodeDTO instanceof FolderDTO);

    nodeDTO = initTestValues.createFileDTO();
    nodeMapper.setType(nodeDTO, node);
    assertTrue(nodeDTO instanceof FileDTO);
  }

  @Test
  public void setParentTest() {
    nodeMapper.setParent(node, null);
    assertNull(node.getParent());

    nodeMapper.setParent(node, node);
    assertNotNull(node.getParent());
  }

  @Test
  public void mapNameNullTest() {
    nodeAttributeList = new ArrayList<>();
    //expects different constant
    nodeAttributeList.add(new NodeAttribute(CMConstants.LOCKABLE, "true", node));
    String result = nodeMapper.mapName(nodeAttributeList);
    nodeAttributeList.forEach(node ->
        assertNotEquals(node.getName(), result)
    );
  }

  @Test
  public void mapLockableTest() {
    nodeAttributeList = new ArrayList<>();
    nodeAttributeList.add(new NodeAttribute(CMConstants.ATTR_NAME, "true", node));
    assertFalse(nodeMapper.mapLockable(nodeAttributeList));
  }

  @Test
  public void mapCreatedByTest() {
    nodeAttributeList = new ArrayList<>();
    nodeAttributeList.add(new NodeAttribute(CMConstants.ATTR_NAME, "true", node));
    String result = nodeMapper.mapCreatedBy(nodeAttributeList);
    nodeAttributeList.forEach(node ->
        assertNotEquals(node.getName(), result)
    );
  }

  @Test
  public void mapChildrenTest() {
    Set<NodeDTO> childrenDTO = nodeMapper.mapChildren(nodeList, RelativesType.EAGER);
    assertEquals(nodeList.size(), childrenDTO.size());
  }

  @Test
  public void mapPathTest() {
    node.setParent(new Node());
    nodeMapper.mapPath(node, nodeDTO, true);
    assertNotNull(nodeDTO.getPath());
  }
}
