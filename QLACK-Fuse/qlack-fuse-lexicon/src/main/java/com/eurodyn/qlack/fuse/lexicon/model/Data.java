package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Cacheable;
import javax.persistence.Column;
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
 * The Data entity, that holds the data of a lexicon translation.
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_data")
@Getter
@Setter
public class Data extends QlackBaseModel {

  private static final long serialVersionUID = 1L;

  /**
   * the version of the Data
   */
  @Version
  private long dbversion;

  /**
   * the id of the Key of the Data
   */
  @ManyToOne
  @JoinColumn(name = "key_id")
  private Key key;

  /**
   * the value of the Data
   */
  private String value;

  /**
   * the language of the Data
   */
  @ManyToOne
  @JoinColumn(name = "language_id")
  private Language language;

  /**
   * the date when Data was last updated
   */
  @Column(name = "last_updated_on")
  private long lastUpdatedOn;

}
