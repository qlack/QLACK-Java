package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseLibraryDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KnowledgeBaseLibraryMapper extends
    RulesMapper<KnowledgeBaseLibrary, KnowledgeBaseLibraryDTO> {

}
