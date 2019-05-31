package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends QlackBaseRepository<Group, String> {

  Group findByTitle(String title);
}
