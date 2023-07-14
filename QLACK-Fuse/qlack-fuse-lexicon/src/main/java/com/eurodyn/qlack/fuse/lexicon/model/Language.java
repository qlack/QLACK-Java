package com.eurodyn.qlack.fuse.lexicon.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.util.List;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
 * The Language entity, that holds data of a lexicon language.
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "lex_language")
@Getter
@Setter
public class Language extends QlackBaseModel {

  private static final long serialVersionUID = -1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;
  /**
   * the language name
   */
  @NotNull
  private String name;
  /**
   * the locale identifies the language code
   */
  @NotNull
  private String locale;
  /**
   * the active status
   */
  private boolean active;
  /**
   * a list of lexicon data mapped by language
   */
  @OneToMany(mappedBy = "language")
  private List<Data> data;
  /**
   * A list of templates mapped by language
   */
  @OneToMany(mappedBy = "language")
  private List<Template> templates;

}
