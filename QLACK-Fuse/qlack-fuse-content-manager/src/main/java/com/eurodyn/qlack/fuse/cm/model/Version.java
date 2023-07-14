package com.eurodyn.qlack.fuse.cm.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version")
@Getter
@Setter
public class Version extends QlackBaseModel {

  @jakarta.persistence.Version
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
