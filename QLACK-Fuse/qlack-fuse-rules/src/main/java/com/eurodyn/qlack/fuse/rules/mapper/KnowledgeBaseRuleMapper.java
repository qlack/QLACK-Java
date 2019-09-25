package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseRuleDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KnowledgeBaseRuleMapper extends
    RulesMapper<KnowledgeBaseRule, KnowledgeBaseRuleDTO> {

}
