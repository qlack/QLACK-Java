package com.eurodyn.qlack.fuse.cm.mappers;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.NodeAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NodeAttributeMapperTest {

  private NodeAttributeMapper nodeAttributeMapper;
  private InitTestValues initTestValues;
  private List<NodeAttribute> nodeAttributeList;
  private List<NodeAttributeDTO> nodeAttributeListDTO;
  private Node node;
  private NodeAttributeDTO nodeAttributeDTO;
  private NodeAttribute nodeAttribute;

  @Before
  public void init(){
    nodeAttributeMapper = new NodeAttributeMapperImpl();
    initTestValues = new InitTestValues();
    node = initTestValues.createNode("nodeId");
    nodeAttributeList = initTestValues.createAttributes(node);
    nodeAttributeDTO = initTestValues.createNodeAttributeDTO();
    nodeAttributeListDTO = initTestValues.createNodeAttributesDTOList(nodeAttributeDTO);
    nodeAttribute = initTestValues.createNodeAttribute();
  }

  @Test
  public void mapToDTOTest(){
    assertNotNull(nodeAttributeMapper.mapToDTO(nodeAttributeList));
  }

  @Test
  public void mapToDTONullTest(){
    nodeAttributeList = null;
    assertNull(nodeAttributeMapper.mapToDTO(nodeAttributeList));
  }

  @Test
  public void mapToEntityTest(){
    assertNotNull(nodeAttributeMapper.mapToEntity(nodeAttributeListDTO));
  }

  @Test
  public void mapToEntityNullTest(){
    nodeAttributeListDTO = null;
    assertNull(nodeAttributeMapper.mapToEntity(nodeAttributeListDTO));
  }

  @Test
  public void mapToEntityDTONullTest(){
    nodeAttributeDTO= null;
    assertNull(nodeAttributeMapper.mapToEntity(nodeAttributeDTO));
  }

  @Test
  public void mapToDTONodeTest(){
    nodeAttribute.getNode().setId(null);
    assertNotNull(nodeAttributeMapper.mapToDTO(nodeAttribute));
  }

  @Test
  public void mapToDTONodeAttributeNodeIdTest(){
    nodeAttribute.setNode(null);
    assertNotNull(nodeAttributeMapper.mapToDTO(nodeAttribute));
  }

  @Test
  public void mapToDTONodeNullTest(){
    nodeAttribute = null;
    assertNull(nodeAttributeMapper.mapToDTO(nodeAttribute));
  }
}
