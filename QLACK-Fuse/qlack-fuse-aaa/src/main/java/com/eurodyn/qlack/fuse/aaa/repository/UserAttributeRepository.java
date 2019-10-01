package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.springframework.stereotype.Repository;

/**
 * A repository interface for UserAttribute.It is used to define a number
 * of crud methods.
 *
 * @author European Dynamics SA
 *
 */
@Repository
public interface UserAttributeRepository extends AAARepository<UserAttribute, String> {

  /**
   * A method that retrieves the relative {@link UserAttribute} object
   * @param userId the user id
   * @param name the name
   * @return the {@link UserAttribute} object
   */
  UserAttribute findByUserIdAndName(String userId, String name);
}
