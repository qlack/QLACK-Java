package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.util.List;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * The Group entity, that holds the data of a lexicon group.
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_group")
@Getter
@Setter
public class Group extends QlackBaseModel {

  private static final long serialVersionUID = 1L;

  /**
   * the version of the Group
   */
  @Version
  private long dbversion;

  /**
   * the title of the Group
   */
  private String title;

  /**
   * the description of the Group
   */
  private String description;

  /**
   * the Keys that are part of the Group
   */
  @OneToMany(mappedBy = "group")
  private List<Key> keys;

}
