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
 * @author European Dynamics
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_application")
@Getter
@Setter
public class Application extends QlackBaseModel {

  @Id
  private String id;
  @Version
  private long dbversion;
  @Column(name = "symbolic_name")
  private String symbolicName;
  private String checksum;
  @Column(name = "executed_on")
  private long executedOn;

}
