package com.eurodyn.qlack.common.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Superclass that contains common fields for the Qlack entities.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@MappedSuperclass
@Deprecated
public abstract class QlackBaseModel implements Serializable {

  /**
   * the auto-generated uuid of the entity
   */
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
}
