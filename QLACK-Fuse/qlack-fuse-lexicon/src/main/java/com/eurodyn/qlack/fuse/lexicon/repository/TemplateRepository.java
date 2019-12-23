package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * A Template Repository interface that is used to define abstract methods for
 * crud operations for the Template entity.
 *
 * @author European Dynamics SA
 */
@Repository
public interface TemplateRepository extends
  QlackBaseRepository<Template, String> {

  /**
   * A method to retrieve the template by its name
   *
   * @param name the name
   * @return a list of template
   */
  List<Template> findByName(String name);

  /**
   * An abstract method declaration to find the name of the template specified
   * by its name and the language locale
   *
   * @param templateName the template name
   * @param locale the locale of the language
   * @return the template filtering by its name and the locale code of the
   * language
   */
  Template findByNameAndLanguageLocale(String templateName, String locale);

  /**
   * An abstract method that is used to retrieve the name of the template by
   * its name and the language id
   *
   * @param templateName the name of the template
   * @param languageId the language id
   * @return the template specified by its name and the language id
   */
  Template findByNameAndLanguageId(String templateName, String languageId);
}
