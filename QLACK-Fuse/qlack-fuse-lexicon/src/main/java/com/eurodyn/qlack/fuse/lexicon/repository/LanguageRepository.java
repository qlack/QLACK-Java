package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * A Repository interface for Language object that is used in order
 * to declare abstract methods for crud operations.
 *
 * @author  European Dynamics SA
 */
@Repository
public interface LanguageRepository extends QlackBaseRepository<Language, String> {

  /** A declared method that is used to retrieve the language according to its locale
   * @param locale the language code
   * @return the locale
   */
  Language findByLocale(String locale);

  /** A method that is used to retrieve a list of language with ascending order and
   *  active status true
   * @return a list of language objects that theirs status is active and the results ordered by ascending
   */
  List<Language> findByActiveTrueOrderByNameAsc();

}
