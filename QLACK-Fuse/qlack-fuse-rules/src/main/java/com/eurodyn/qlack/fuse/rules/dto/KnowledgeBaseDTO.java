package com.eurodyn.qlack.fuse.rules.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO that represents the KnowledgeBase entity and holds the data of a Drools Knowledge Base.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class KnowledgeBaseDTO {

    /**
     * the id
     */
    private String id;

    /**
     * the serialized KieBase
     */
    private byte[] state;

    /**
     * the libraries of the Knowledge Base
     */
    private List<KnowledgeBaseLibraryDTO> libraries;

    /**
     * the rules of the Knowledge Base
     */
    private List<KnowledgeBaseRuleDTO> rules;
}
