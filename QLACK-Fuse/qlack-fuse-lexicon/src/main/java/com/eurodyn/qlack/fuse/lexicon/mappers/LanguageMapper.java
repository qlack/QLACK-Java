package com.eurodyn.qlack.fuse.lexicon.mappers;

import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/** A LanguageMapper interface that is is used to map the language data and their values.
 * @author
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LanguageMapper extends LexiconMapper<Language, LanguageDTO> {

}
