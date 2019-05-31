package com.eurodyn.qlack.fuse.cm;

import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.model.Node;

/**
 * @author European Dynamics
 */
public class InitTestValues {

  public Node createNode() {
    Node node = new Node();
    return node;
  }

  public NodeDTO createNodeDTO() {
    NodeDTO nodeDTO = new NodeDTO();
    nodeDTO.setId("123456");
    nodeDTO.setName("test");
    return nodeDTO;
  }

}
