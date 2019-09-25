package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseLibraryDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>KnowledgeBaseLibrary</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KnowledgeBaseLibraryMapper extends
    RulesMapper<KnowledgeBaseLibrary, KnowledgeBaseLibraryDTO> {

}
