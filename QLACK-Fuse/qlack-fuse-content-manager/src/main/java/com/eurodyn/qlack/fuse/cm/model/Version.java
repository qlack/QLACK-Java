package com.eurodyn.qlack.fuse.cm.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version")
@Getter
@Setter
public class Version extends QlackBaseModel {

  @javax.persistence.Version
  private long dbversion;
  private String name;
  @ManyToOne
  @JoinColumn(name = "node")
  private Node node;
  @Column(name = "created_on")
  private long createdOn;
  private String filename;
  // The media type of the latest version.
  private String mimetype;

  // The size of the latest version.
  private Long size;
  @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
  private List<VersionAttribute> attributes;
  @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VersionBin> versionBins;

  public VersionAttribute getAttribute(String name) {
    for (VersionAttribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        return attribute;
      }
    }
    return null;
  }

  public void setAttribute(String name, String value) {
    VersionAttribute attribute = getAttribute(name);
    if (attribute == null) {
      attribute = new VersionAttribute();
      attribute.setVersion(this);
      attribute.setName(name);
      attributes.add(attribute);
    }
    attribute.setValue(value);
  }

  public void removeAttribute(String name) {
    VersionAttribute attribute = getAttribute(name);
    getAttributes().remove(attribute);
    attribute.setVersion(null);
  }
}