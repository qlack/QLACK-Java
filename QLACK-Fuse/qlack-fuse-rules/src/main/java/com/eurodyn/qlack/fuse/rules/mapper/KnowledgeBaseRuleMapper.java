package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseRuleDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>KnowledgeBaseRule</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KnowledgeBaseRuleMapper extends
    RulesMapper<KnowledgeBaseRule, KnowledgeBaseRuleDTO> {

}
