package com.eurodyn.qlack.fuse.rules.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Knowledge Base Rule entity, that contains the rules of a Knowledge Base.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "rul_kbase_rule")
@Getter
@Setter
@NoArgsConstructor
public class KnowledgeBaseRule extends QlackBaseModel {

  /**
   * the .drl rule of the Knowledge Base
   */
  private String rule;

  /**
   * the Knowledge Base
   */
  @ManyToOne
  @JoinColumn(name = "kbase_id")
  private KnowledgeBase base;
}
