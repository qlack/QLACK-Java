package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupHasOperationRepository extends AAARepository<UserGroupHasOperation, String> {

  UserGroupHasOperation findByUserGroupIdAndOperationName(String userGroupId, String operationName);

  UserGroupHasOperation findByUserGroupIdAndResourceIdAndOperationName(String userGroupId, String resourceId,
                                                                   String operationName);

  UserGroupHasOperation findByUserGroupIdAndOperationNameAndResourceNameAndResourceObjectId(String userGroupId, String operationName, String resourceName,String resourceObjectId);

  List<UserGroupHasOperation> findByOperationName(String operationName);

  List<UserGroupHasOperation> findByResourceIdAndOperationName(String resourceId, String operationName);

  List<UserGroupHasOperation> findByUserGroupName(String userGroupName);
}
