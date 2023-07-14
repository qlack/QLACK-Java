package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * The Application Entity that holds data for Application
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_application")
@Getter
@Setter
public class Application extends QlackBaseModel {

  /**
   * the application id
   */
  @Id
  private String id;
  /**
   * the dbversion
   */
  @Version
  private long dbversion;
  /**
   * the name that is symbolic for the application
   */
  @Column(name = "symbolic_name")
  private String symbolicName;
  /**
   * the  checksum
   */
  private String checksum;
  /**
   * the date of execution
   */
  @Column(name = "executed_on")
  private long executedOn;

}
