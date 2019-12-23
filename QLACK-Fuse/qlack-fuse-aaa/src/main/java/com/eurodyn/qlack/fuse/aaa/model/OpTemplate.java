package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_op_template database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_op_template")
@Getter
@Setter
public class OpTemplate extends AAAModel {

  private static final long serialVersionUID = 1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * the description
   */
  private String description;

  /**
   * the name
   */
  private String name;

  /**
   * bi-directional many-to-one association to OpTemplateHasOperation
   **/
  @OneToMany(mappedBy = "template")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  public OpTemplate() {
    setId(UUID.randomUUID().toString());
  }

  /**
   * A method that adds a {@link OpTemplateHasOperation} object
   *
   * @param opTemplateHasOperation a {@link OpTemplateHasOperation} object
   * @return a {@link OpTemplateHasOperation} object
   */
  public OpTemplateHasOperation addOpTemplateHasOperation(
    OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setTemplate(this);

    return opTemplateHasOperation;
  }

  /**
   * A method that removes a {@link OpTemplateHasOperation} object
   *
   * @param opTemplateHasOperation a {@link OpTemplateHasOperation} object
   * @return a {@link OpTemplateHasOperation} object
   */
  public OpTemplateHasOperation removeOpTemplateHasOperation(
    OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setTemplate(null);

    return opTemplateHasOperation;
  }

}
