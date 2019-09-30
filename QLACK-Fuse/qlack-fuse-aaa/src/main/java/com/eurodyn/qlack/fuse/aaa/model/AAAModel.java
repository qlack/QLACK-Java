package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * A Model definition for AAA.
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
