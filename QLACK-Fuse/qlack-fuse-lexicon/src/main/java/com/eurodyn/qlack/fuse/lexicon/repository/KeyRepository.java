package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends QlackBaseRepository<Key, String> {

  Key findByNameAndGroupId(String keyName, String groupId);
}
