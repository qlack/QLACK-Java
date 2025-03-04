package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 * A Model definition for AAA.
 *
 * @author European Dynamics SA
 */

@MappedSuperclass
@Getter
@Setter
public class AAAModel implements Serializable {

  /**
   * the id
   */
  @Id
  @UuidGenerator
  private String id;
}
