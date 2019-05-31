package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends QlackBaseRepository<Template, String> {

  List<Template> findByName(String name);

  Template findByNameAndLanguageLocale(String templateName, String locale);

  Template findByNameAndLanguageId(String templateName, String languageId);
}
