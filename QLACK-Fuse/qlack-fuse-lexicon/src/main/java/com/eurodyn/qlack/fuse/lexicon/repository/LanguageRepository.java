package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends QlackBaseRepository<Language, String> {

  Language findByLocale(String locale);

  List<Language> findByActiveTrueOrderByNameAsc();

}
