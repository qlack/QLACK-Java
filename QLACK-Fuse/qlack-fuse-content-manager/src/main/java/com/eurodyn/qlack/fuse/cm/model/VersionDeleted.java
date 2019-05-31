package com.eurodyn.qlack.fuse.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version_deleted")
@Getter
@Setter
public class VersionDeleted {

  @Id
  @Column(name = "version_id")
  private String id;

}
