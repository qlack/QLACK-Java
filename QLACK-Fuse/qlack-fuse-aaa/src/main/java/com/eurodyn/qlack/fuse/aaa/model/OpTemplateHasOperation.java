package com.eurodyn.qlack.fuse.aaa.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_op_template_has_operation database table.
 */
@Entity
@Table(name = "aaa_op_template_has_operation")
@Getter
@Setter
public class OpTemplateHasOperation extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  private boolean deny;

  //bi-directional many-to-one association to OpTemplate
  @ManyToOne
  @JoinColumn(name = "template")
  private OpTemplate template;

  //bi-directional many-to-one association to Operation
  @ManyToOne
  @JoinColumn(name = "operation")
  private Operation operation;

  //bi-directional many-to-one association to Resource
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private Resource resource;

  public OpTemplateHasOperation() {
    setId(UUID.randomUUID().toString());
  }

}
