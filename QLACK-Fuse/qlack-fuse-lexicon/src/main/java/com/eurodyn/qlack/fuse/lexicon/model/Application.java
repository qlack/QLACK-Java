package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
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
