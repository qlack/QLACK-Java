package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.AAAModel;
import com.eurodyn.qlack.fuse.aaa.model.User;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

/**A Repository interface for User.It is used to
 * define a number of abstract crud methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface UserRepository extends AAARepository<User, String> {

    /**Finds the username by its specified username
     * @param username the username
     * @return a {@link User} object
     */
  User findByUsername(String username);

    /**Finds a {@link User} object
     * @return a set of {@link User} object
     */
  Set<User> findBySuperadminTrue();

    /**Finds a {@link User} object
     * @return a set of {@link User} object
     */
  Set<User> findBySuperadminFalse();

    /**Finds a Set of {@link String} user ids
     * @param superadmin check whether its superAdmin or not
     * @return a set of {@link String} user ids
     */
  default Set<String> getUserIds(Boolean superadmin){

    if(superadmin) {
      return findBySuperadminTrue().stream()
          .map(AAAModel::getId)
          .collect(Collectors.toSet());
    }else {
      return findBySuperadminFalse().stream()
          .map(AAAModel::getId)
          .collect(Collectors.toSet());
    }
  }
}
