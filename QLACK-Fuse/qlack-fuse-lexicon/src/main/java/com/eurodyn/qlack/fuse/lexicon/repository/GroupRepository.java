package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import org.springframework.stereotype.Repository;

/**
 * An interface Repository for Group that is used to declare abstract methord for crud operations
 *
 * @author European Dynamics SA
 */
@Repository
public interface GroupRepository extends QlackBaseRepository<Group, String> {

  /**
   * An abstract method that is used to retrieve group by its title
   *
   * @param title the title description of group
   * @return the group by its title
   */
  Group findByTitle(String title);
}
