package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import org.springframework.stereotype.Repository;

/**
 * An interface KeyRepository that is used to declared crud methods
 * @author European Dynamics SA
 */
@Repository
public interface KeyRepository extends QlackBaseRepository<Key, String> {

  /** A method declaration that the usage is to retrieve the key filtered by the key
   *  name and the group id that belongs to
   * @param keyName the key name
   * @param groupId the group id
   * @return the key by its name and the group id that related to
   */
  Key findByNameAndGroupId(String keyName, String groupId);
}
