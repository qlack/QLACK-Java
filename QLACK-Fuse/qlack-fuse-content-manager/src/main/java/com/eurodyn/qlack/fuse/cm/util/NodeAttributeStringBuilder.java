package com.eurodyn.qlack.fuse.cm.util;

import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;

/**
 * @author European Dynamics
 */
public class NodeAttributeStringBuilder {

  public StringBuilder nodeAttributeBuilder(Node node) {
    StringBuilder buf = new StringBuilder();
    // Include a created on property
    buf.append(CMConstants.CREATED_ON).append(" = ").append(node.getCreatedOn())
      .append("\n");
    for (NodeAttribute attribute : node.getAttributes()) {
      buf.append(attribute.getName());
      buf.append(" = ");
      buf.append(attribute.getValue());
      buf.append("\n");
    }
    return buf;
  }

}
