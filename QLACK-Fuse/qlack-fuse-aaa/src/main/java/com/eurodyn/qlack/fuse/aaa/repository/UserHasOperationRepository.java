package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**A Repository interface for UserHasOperation.It is used to define a number of
 * crud methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface UserHasOperationRepository extends AAARepository<UserHasOperation, String> {

    /**Finds a {@link UserHasOperation} object
     * @param userId the userId
     * @return a list of {@link UserHasOperation} object
     */
  List<UserHasOperation> findByUserId(String userId);

    /**Finds a {@link UserHasOperation} object by its userId and the operationName
     * @param userId the userId
     * @param operationName the operationName
     * @return a {@link UserHasOperation} object
     */
  UserHasOperation findByUserIdAndOperationName(String userId, String operationName);

    /**Finds a {@link UserHasOperation} object
     * @param userId the userId
     * @param resourceId the resourceId
     * @param operationName the operationName
     * @return a {@link UserHasOperation} object
     */
  UserHasOperation findByUserIdAndResourceIdAndOperationName(String userId, String resourceId,String operationName);

    /**Finds a list of {@link UserHasOperation}
     * @param name the name
     * @return a list of {@link UserHasOperation} object
     */
  List<UserHasOperation> findByOperationName(String name);

    /**Finds a list of {@link UserHasOperation} objects
     * @param resourceId the resource Id
     * @param operationName the operationName
     * @return a list of {@link UserHasOperation} objects
     */
  List<UserHasOperation> findByResourceIdAndOperationName(String resourceId, String operationName);
}
