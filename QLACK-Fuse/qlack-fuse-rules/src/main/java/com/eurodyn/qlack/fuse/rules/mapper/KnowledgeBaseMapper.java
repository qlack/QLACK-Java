package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {KnowledgeBaseLibraryMapper.class, KnowledgeBaseRuleMapper.class})
public interface KnowledgeBaseMapper extends RulesMapper<KnowledgeBase, KnowledgeBaseDTO> {

}
