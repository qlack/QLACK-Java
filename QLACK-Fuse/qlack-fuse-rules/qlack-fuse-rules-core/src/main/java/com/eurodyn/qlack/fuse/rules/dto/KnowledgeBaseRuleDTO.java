package com.eurodyn.qlack.fuse.rules.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO that represents the KnowledgeBaseRule entity and contains the rules of
 * a Knowledge Base.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class KnowledgeBaseRuleDTO {

  /**
   * the .drl rule of the Knowledge Base
   */
  private String rule;
}
