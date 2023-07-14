package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
}
