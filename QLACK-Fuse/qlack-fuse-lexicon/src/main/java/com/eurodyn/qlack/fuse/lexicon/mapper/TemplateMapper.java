package com.eurodyn.qlack.fuse.lexicon.mapper;

import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * An interface TemplateMapper that is used to map the Template objects with
 * their values.
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateMapper extends LexiconMapper<Template, TemplateDTO> {

}
