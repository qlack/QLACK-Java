package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * A Repository interface for Data. Its is used to declare abstract methods for
 * crud operations
 * @author European Dynamics SA
 */
@Repository
public interface DataRepository extends QlackBaseRepository<Data, String> {

  /** An abstract method that is used to retrieve data filtered by the
   *  key id and language id
   * @param keyId the key id
   * @param languageId the language id
   * @return data by their key id and language id
   */
  Data findByKeyIdAndLanguageId(String keyId, String languageId);

  /** An abstract method that is used to retrieve data filtered by the
   *  key name and language id
   * @param keyName the name of the key
   * @param languageId the language Id
   * @return the data specified by their key name and the id of the language
   */
  Data findByKeyNameAndLanguageId(String keyName, String languageId);

  /** An abstract method that is used to retrieve the data by the keyId and
   *  locale code
   * @param keyId the key Id
   * @param locale the locale an abbreviation of language code
   * @return the data specified by their key id and locale
   */
  Data findByKeyIdAndLanguageLocale(String keyId, String locale);

  /** An abstract method that is used to retrieve the data by the key name and
   *  the locale language code
   * @param keyName the key name
   * @param locale the  abbreviation of language code
   * @return the data specified by their key name and locale code
   */
  Data findByKeyNameAndLanguageLocale(String keyName, String locale);

  /** An abstract method that is used to retrieve the data by the group id and
   *  the locale language code
   * @param groupId the group id
   * @param locale the abbreviation of language code
   * @return the data specified by their group id and the locale
   */
  List<Data> findByKeyGroupIdAndLanguageLocale(String groupId, String locale);
}
