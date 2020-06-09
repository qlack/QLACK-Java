package com.eurodyn.qlack.fuse.rules.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * The Knowledge Base entity, that holds the data of a Drools Knowledge Base.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "rul_kbase")
@Getter
@Setter
@NoArgsConstructor
public class KnowledgeBase extends QlackBaseModel {

  /**
   * the serialized KieBase
   */
  @Lob
  private byte[] state;

  /**
   * the libraries of the Knowledge Base
   */
  @OneToMany(mappedBy = "base", cascade = {CascadeType.PERSIST,
    CascadeType.MERGE})
  private List<KnowledgeBaseLibrary> libraries;

  /**
   * the rules of the Knowledge Base
   */
  @OneToMany(mappedBy = "base", cascade = {CascadeType.PERSIST,
    CascadeType.MERGE})
  private List<KnowledgeBaseRule> rules;

}
