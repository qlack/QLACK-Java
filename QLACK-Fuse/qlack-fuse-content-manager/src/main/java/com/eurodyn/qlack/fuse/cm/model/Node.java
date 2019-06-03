package com.eurodyn.qlack.fuse.cm.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cm_node")
@Getter
@Setter
@NoArgsConstructor
public class Node extends QlackBaseModel {

  @Id
  private String id;
  @Version
  private long dbversion;
  @Enumerated(EnumType.ORDINAL)
  private NodeType type;
  @ManyToOne
  @JoinColumn(name = "parent")
  private Node parent;
  @Column(name = "created_on")
  private long createdOn;
  @OneToMany(mappedBy = "parent")
  private List<Node> children;
  @Column(name = "lock_token")
  private String lockToken;
  @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NodeAttribute> attributes;
  // The media type of the latest version.
  private String mimetype;
  // The size of the latest version.
  private Long size;

  public NodeAttribute getAttribute(String name) {
    for (NodeAttribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        return attribute;
      }
    }
    return null;
  }

  public void setAttribute(String name, String value) {
    NodeAttribute attribute = getAttribute(name);
    if (attribute == null) {
      attribute = new NodeAttribute();
      attribute.setNode(this);
      attribute.setName(name);
    }
    attribute.setValue(value);
    getAttributes().add(attribute);
    attribute.setNode(this);
  }

  public void removeAttribute(String name) {
    NodeAttribute attribute = getAttribute(name);
    getAttributes().remove(attribute);
    attribute.setNode(null);
  }

}
