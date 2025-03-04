package com.eurodyn.qlack.fuse.aaa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_op_template_has_operation database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_op_template_has_operation")
@Getter
@Setter
public class OpTemplateHasOperation extends AAAModel {

  private static final long serialVersionUID = 1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * checking whether its denied or not
   */
  private boolean deny;

  /**
   * bi-directional many-to-one association to OpTemplate
   **/
  @ManyToOne
  @JoinColumn(name = "template")
  private OpTemplate template;

  /**
   * bi-directional many-to-one association to Operation
   **/
  @ManyToOne
  @JoinColumn(name = "operation")
  private Operation operation;

  /**
   * bi-directional many-to-one association to Resource
   **/
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private Resource resource;

  public OpTemplateHasOperation() {
  }

}
