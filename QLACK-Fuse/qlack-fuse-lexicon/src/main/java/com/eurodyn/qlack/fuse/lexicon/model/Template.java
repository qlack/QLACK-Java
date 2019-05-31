package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * The Template entity, that holds the data of a lexicon template.
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_template")
@Getter
@Setter
public class Template extends QlackBaseModel {

  private static final long serialVersionUID = 1L;

  /**
   * the version of the Template
   */
  @Version
  private long dbversion;

  /**
   * the name of the Template
   */
  private String name;

  /**
   * the content of the Template
   */
  private String content;

  /**
   * the Language of the template
   */
  @ManyToOne
  @JoinColumn(name = "language_id")
  private Language language;

}
