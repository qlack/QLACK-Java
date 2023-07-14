package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.util.List;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * The Key entity, that holds the key of a lexicon translation.
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_key")
@Getter
@Setter
public class Key extends QlackBaseModel {

  private static final long serialVersionUID = 1L;

  /**
   * the version of the Key
   */
  @Version
  private long dbversion;

  /**
   * the name of the Key
   */
  private String name;
  @ManyToOne

  /**
   * the id of the Group, that the Key is part of
   */
  @JoinColumn(name = "group_id")
  private Group group;

  /**
   * the translation Data of the Key
   */
  @OneToMany(mappedBy = "key")
  private List<Data> data;

}
