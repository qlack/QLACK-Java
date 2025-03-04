package com.eurodyn.qlack.common.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 * Superclass that contains common fields for QLACK entities.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@MappedSuperclass
public abstract class QlackBaseModel implements Serializable {

  /**
   * the auto-generated uuid of the entity
   */
  @Id
  @UuidGenerator
  private String id;
}
