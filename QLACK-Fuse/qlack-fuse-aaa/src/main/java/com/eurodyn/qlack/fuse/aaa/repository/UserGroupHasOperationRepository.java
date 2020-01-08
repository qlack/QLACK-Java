package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A Repository interface for UserGroupHasOperation.It is used to define a
 * number of abstract methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface UserGroupHasOperationRepository extends
  AAARepository<UserGroupHasOperation, String> {

  /**
   * Finds {@link UserGroupHasOperation} object by its userGroupId and
   * operationName
   *
   * @param userGroupId the user group id
   * @param operationName the operation name
   * @return the {@link UserGroupHasOperation} object
   */
  UserGroupHasOperation findByUserGroupIdAndOperationName(String userGroupId,
    String operationName);

  /**
   * Finds {@link UserGroupHasOperation} object by its userGroupId, resourceId
   * and operationName
   *
   * @param userGroupId the userGroupId
   * @param resourceId the resourceId
   * @param operationName the operationName
   * @return the {@link UserGroupHasOperation} object
   */
  UserGroupHasOperation findByUserGroupIdAndResourceIdAndOperationName(
    String userGroupId,
    String resourceId,
    String operationName);

  /**
   * Finds {@link UserGroupHasOperation} by its userGroupId, the
   * operationName,the resourceName and the resourceObjectId
   *
   * @param userGroupId the userGroupId
   * @param operationName the operationName
   * @param resourceName the resourceName
   * @param resourceObjectId the resourceObjectId
   * @return the {@link UserGroupHasOperation} object
   */
  UserGroupHasOperation findByUserGroupIdAndOperationNameAndResourceNameAndResourceObjectId(
    String userGroupId, String operationName, String resourceName,
    String resourceObjectId);

  /**
   * Finds {@link UserGroupHasOperation} by its userGroupId, the operationName, and resourceName.
   *
   * @param userGroupId the userGroupId
   * @param operationName the operationName
   * @param resourceName the resourceName
   * @return the {@link UserGroupHasOperation} object
   */
  UserGroupHasOperation findByUserGroupIdAndOperationNameAndResourceName(
      String userGroupId, String operationName, String resourceName);

  /**
   * Finds a list of {@link UserGroupHasOperation} objects
   *
   * @param operationName the operationName
   * @return a list of {@link UserGroupHasOperation} objects
   */
  List<UserGroupHasOperation> findByOperationName(String operationName);

  /**
   * Finds a list of {@link UserGroupHasOperation} objects
   *
   * @param resourceId the resourceId
   * @param operationName the operationName
   * @return a list of  {@link UserGroupHasOperation} object
   */
  List<UserGroupHasOperation> findByResourceIdAndOperationName(
    String resourceId,
    String operationName);

  /**
   * Finds a list of {@link UserGroupHasOperation} objects
   *
   * @param userGroupName the userGroupName
   * @return a list of {@link UserGroupHasOperation} objects
   */
  List<UserGroupHasOperation> findByUserGroupName(String userGroupName);
}
