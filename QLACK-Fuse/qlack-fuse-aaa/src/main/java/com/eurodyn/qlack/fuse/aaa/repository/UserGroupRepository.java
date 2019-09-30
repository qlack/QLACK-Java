package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**A Repository interface for UserGroup.It is used to define a number of abstract crud methods
 *
 * @author European Dynamics SA
 */
@Repository
public interface UserGroupRepository extends AAARepository<UserGroup, String> {

  /**Finds the {@link UserGroup} object provided by its name
   * @param name the name
   * @return the {@link UserGroup} object
   */
  UserGroup findByName(String name);

  /**Finds a {@link UserGroup} object
   * @param objectId the object id
   * @return a {@link UserGroup} provided by the object id
   */
  UserGroup findByObjectId(String objectId);


  /**Finds a List of {@link UserGroup} objects
   * @param groupIds the groupIds
   * @return a list of {@link UserGroup} objects
   */
  List<UserGroup> findByIdIn(Collection<String> groupIds);

  /** Retrieves a set of {@link String} ids
   * @return a set of {@link String} ids
   */
  default Set<String> getAllIds(){

    return findAll().stream()
        .map(UserGroup::getId)
        .collect(Collectors.toSet());
  }

}
