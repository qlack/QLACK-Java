package com.eurodyn.qlack.fuse.aaa.service;

import bsh.EvalError;
import bsh.Interpreter;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.exception.DynamicOperationException;
import com.eurodyn.qlack.fuse.aaa.mapper.OperationMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserGroupHasOperationMapper;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import com.eurodyn.qlack.fuse.aaa.repository.OpTemplateRepository;
import com.eurodyn.qlack.fuse.aaa.repository.OperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupHasOperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserHasOperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A Service class that is used to configure Operation
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class OperationService {

  private static final Logger LOGGER = Logger
    .getLogger(OperationService.class.getName());

  //Repositories
  private final OperationRepository operationRepository;
  private final UserHasOperationRepository userHasOperationRepository;
  private final UserRepository userRepository;
  private final ResourceRepository resourceRepository;
  private final OpTemplateRepository opTemplateRepository;
  private final UserGroupHasOperationRepository userGroupHasOperationRepository;
  private final UserGroupRepository userGroupRepository;
  //Mappers
  private final OperationMapper operationMapper;
  private final ResourceMapper resourceMapper;
  private final UserGroupHasOperationMapper userGroupHasOperationMapper;

  @SuppressWarnings("squid:S00107")
  public OperationService(
    OperationRepository operationRepository,
    UserHasOperationRepository userHasOperationRepository,
    UserRepository userRepository,
    ResourceRepository resourceRepository,
    OpTemplateRepository opTemplateRepository,
    UserGroupHasOperationRepository userGroupHasOperationRepository,
    UserGroupRepository userGroupRepository, OperationMapper operationMapper,
    ResourceMapper resourceMapper,
    UserGroupHasOperationMapper userGroupHasOperationMapper) {
    this.operationRepository = operationRepository;
    this.userHasOperationRepository = userHasOperationRepository;
    this.userRepository = userRepository;
    this.resourceRepository = resourceRepository;
    this.opTemplateRepository = opTemplateRepository;
    this.userGroupHasOperationRepository = userGroupHasOperationRepository;
    this.userGroupRepository = userGroupRepository;
    this.operationMapper = operationMapper;
    this.resourceMapper = resourceMapper;
    this.userGroupHasOperationMapper = userGroupHasOperationMapper;
  }

  private boolean prioritisePositive;

  public boolean getPrioritisePositive() {
    return prioritisePositive;
  }

  public void setPrioritisePositive(boolean prioritisePositive) {
    this.prioritisePositive = prioritisePositive;
  }

  public String createOperation(OperationDTO operationDTO) {
    Operation operation = operationMapper.mapToEntity(operationDTO);
    operationRepository.save(operation);

    return operation.getId();
  }

  /**
   * Updates the {@link Operation} object
   *
   * @param operationDTO the {@link OperationDTO}
   */
  public void updateOperation(OperationDTO operationDTO) {
    Operation operation = operationRepository.fetchById(operationDTO.getId());
    operationMapper.mapToExistingEntity(operationDTO, operation);
  }

  /**
   * Deletes the {@link Operation}
   *
   * @param operationID the operationID
   */
  public void deleteOperation(String operationID) {

    operationRepository.delete(findById(operationID));
  }

  /**
   * Finds Operation by its id
   *
   * @param operationId the operationID
   * @return the operation by its id
   */
  private Operation findById(String operationId) {

    return operationRepository.fetchById(operationId);
  }

  /**
   * Retrieves all the operations
   *
   * @return all the operations
   */
  public List<OperationDTO> getAllOperations() {
    return operationMapper.mapToDTO(operationRepository.findAll());
  }

  public OperationDTO getOperationByName(String operationName) {
    return operationMapper
      .mapToDTO(operationRepository.findByName(operationName));
  }

  /**
   * Add operation to a User
   *
   * @param userID the userID
   * @param operationName the operationName
   * @param isDeny if it is denied or not
   */
  public void addOperationToUser(String userID, String operationName,
    boolean isDeny) {
    UserHasOperation uho = userHasOperationRepository
      .findByUserIdAndOperationName(userID, operationName);
    if (uho != null) {
      uho.setDeny(isDeny);
    } else {
      userHasOperationRepository
        .save(commonAddOperationToUser(userID, operationName, isDeny));
    }
  }

  public void addOperationToUser(String userID, String operationName,
    String resourceID,
    boolean isDeny) {
    UserHasOperation uho = userHasOperationRepository
      .findByUserIdAndResourceIdAndOperationName(userID, resourceID,
        operationName);
    if (uho != null) {
      uho.setDeny(isDeny);
    } else {
      uho = commonAddOperationToUser(userID, operationName, isDeny);
      Resource resource = resourceRepository.fetchById(resourceID);
      resource.addUserHasOperation(uho);
      userHasOperationRepository.save(uho);
    }
  }

  /**
   * Adds common operation to user
   *
   * @param userID the userID
   * @param operationName the operation
   * @param isDeny if it is denied or not
   * @return the {@link UserHasOperation} object
   */
  private UserHasOperation commonAddOperationToUser(String userID,
    String operationName,
    boolean isDeny) {
    User user = userRepository.fetchById(userID);
    Operation operation = operationRepository.findByName(operationName);
    UserHasOperation uho = new UserHasOperation();
    uho.setDeny(isDeny);
    user.addUserHasOperation(uho);
    operation.addUserHasOperation(uho);
    return uho;
  }

  /**
   * Adds operation to a user from Template ID
   *
   * @param userID the userID
   * @param templateID the templateID
   */
  public void addOperationsToUserFromTemplateID(String userID,
    String templateID) {
    OpTemplate template = opTemplateRepository.fetchById(templateID);
    addOperationsToUserFromTemplate(userID, template);
  }

  /**
   * Add operations to user from Template Name
   *
   * @param userID the userID
   * @param templateName the templateName
   */
  public void addOperationsToUserFromTemplateName(String userID,
    String templateName) {
    OpTemplate template = opTemplateRepository.findByName(templateName);
    addOperationsToUserFromTemplate(userID, template);
  }

  /**
   * Adds operation to user from template
   *
   * @param userID the userID
   * @param template the template
   */
  private void addOperationsToUserFromTemplate(String userID,
    OpTemplate template) {
    for (OpTemplateHasOperation tho : template.getOpTemplateHasOperations()) {
      if (tho.getResource() == null) {
        addOperationToUser(userID, tho.getOperation().getName(), tho.isDeny());
      } else {
        addOperationToUser(userID, tho.getOperation().getName(),
          tho.getResource().getId(),
          tho.isDeny());
      }
    }
  }

  /**
   * Adds operation to Group
   *
   * @param groupID the groupID
   * @param operationName the operationName
   * @param isDeny isDeny value check whether is denied or not
   */
  public void addOperationToGroup(String groupID, String operationName,
    boolean isDeny) {
    UserGroupHasOperation gho = userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(groupID, operationName);

    if (gho != null) {
      gho.setDeny(isDeny);
    } else {
      userGroupHasOperationRepository
        .save(commonAddOperationToGroup(groupID, operationName, isDeny));
    }
  }

  /**
   * Adds operation to Group
   *
   * @param groupID the groupId
   * @param operationName the operationName
   * @param resourceID the resourceId
   * @param isDeny whether is denied or not
   */
  public void addOperationToGroup(String groupID, String operationName,
    String resourceID,
    boolean isDeny) {
    UserGroupHasOperation gho = userGroupHasOperationRepository
      .findByUserGroupIdAndResourceIdAndOperationName(groupID, operationName,
        resourceID);
    if (gho != null) {
      gho.setDeny(isDeny);
    } else {
      gho = commonAddOperationToGroup(groupID, operationName, isDeny);
      Resource resource = resourceRepository.fetchById(resourceID);
      resource.addGroupHasOperation(gho);
      userGroupHasOperationRepository.save(gho);
    }
  }

  /**
   * Adds operation to Group
   *
   * @param groupID the groupId
   * @param operationName the operationName
   * @param isDeny whether is denied or not
   * @return a {@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation commonAddOperationToGroup(String groupID,
    String operationName,
    boolean isDeny) {
    UserGroup userGroup = userGroupRepository.fetchById(groupID);
    Operation operation = operationRepository.findByName(operationName);
    UserGroupHasOperation gho = new UserGroupHasOperation();
    gho.setDeny(isDeny);
    userGroup.addGroupHasOperation(gho);
    operation.addGroupHasOperation(gho);
    return gho;
  }

  /**
   * Adds operation to group from TemplateID
   *
   * @param groupID the groupId
   * @param templateID the templateID
   */
  public void addOperationsToGroupFromTemplateID(String groupID,
    String templateID) {
    OpTemplate template = opTemplateRepository.fetchById(templateID);
    addOperationsToGroupFromTemplate(groupID, template);
  }

  /**
   * Adds operation to group from template name
   *
   * @param groupID the groupId
   * @param templateName the templateName
   */
  public void addOperationsToGroupFromTemplateName(String groupID,
    String templateName) {
    OpTemplate template = opTemplateRepository.findByName(templateName);
    addOperationsToGroupFromTemplate(groupID, template);
  }

  /**
   * Adds operations to group from template
   *
   * @param groupID the groupId
   * @param template the template
   */
  private void addOperationsToGroupFromTemplate(String groupID,
    OpTemplate template) {
    for (OpTemplateHasOperation tho : template.getOpTemplateHasOperations()) {
      if (tho.getResource() == null) {
        addOperationToGroup(groupID, tho.getOperation().getName(),
          tho.isDeny());
      } else {
        addOperationToGroup(groupID, tho.getOperation().getName(),
          tho.getResource().getId(),
          tho.isDeny());
      }
    }
  }

  /**
   * Removes the operation from user provided by its userID and operationName
   *
   * @param userID the userID
   * @param operationName the operationName
   */
  public void removeOperationFromUser(String userID, String operationName) {
    UserHasOperation uho = userHasOperationRepository
      .findByUserIdAndOperationName(userID, operationName);
    if (uho != null) {
      userHasOperationRepository.delete(uho);
    }
  }

  /**
   * Removes the operation from user provided by its userID , operationName
   * and resourceId
   *
   * @param userID the userID
   * @param operationName the operationName
   * @param resourceID the resourceId
   */
  public void removeOperationFromUser(String userID, String operationName,
    String resourceID) {
    UserHasOperation uho = userHasOperationRepository
      .findByUserIdAndResourceIdAndOperationName(userID, resourceID,
        operationName);
    if (uho != null) {
      userHasOperationRepository.delete(uho);
    }
  }

  /**
   * Removes operation from Group provided by its groupID and operationName
   *
   * @param groupID the groupID
   * @param operationName the operationName
   */
  public void removeOperationFromGroup(String groupID, String operationName) {
    UserGroupHasOperation gho = userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(groupID, operationName);
    if (gho != null) {
      userGroupHasOperationRepository.delete(gho);
    }
  }

  /**
   * Removes operation from group provided by its groupID,operationName and
   * resourceID
   *
   * @param groupID the groupId
   * @param operationName the operationName
   * @param resourceID the resourceID
   */
  public void removeOperationFromGroup(String groupID, String operationName,
    String resourceID) {
    UserGroupHasOperation gho = userGroupHasOperationRepository
      .findByUserGroupIdAndResourceIdAndOperationName(groupID, resourceID,
        operationName);
    if (gho != null) {
      userGroupHasOperationRepository.delete(gho);
    }
  }

  /**
   * Checks whether is permitted the operation or not
   *
   * @param userId the userId
   * @param operationName the operationName
   * @return a {@link Boolean} value whether it is permitted or not
   */
  public Boolean isPermitted(String userId, String operationName) {
    return isPermitted(userId, operationName, null);
  }

  public Boolean isPermitted(String userId, String operationName,
    String resourceObjectID) {
    LOGGER.log(
      Level.FINEST,
      "Checking permissions for user ''{0}'', operation ''{1}'' and resource object ID ''{2}''.",
      new String[]{userId, operationName, resourceObjectID});
    User user = userRepository.fetchById(userId);
    if (user.isSuperadmin()) {
      return true;
    }

    Operation operation = operationRepository.findByName(operationName);
    String resourceId = (resourceObjectID == null) ? null
      : resourceRepository.findByObjectId(resourceObjectID).getId();

    Boolean retVal = false;
    UserHasOperation uho = (resourceId == null)
      ? userHasOperationRepository
      .findByUserIdAndOperationName(userId, operationName)
      : userHasOperationRepository
        .findByUserIdAndResourceIdAndOperationName(userId, resourceId,
          operationName);
    // Check the user's permission on the operation
    if (uho != null) {
      // First check whether this is a dynamic operation.
      if (operation.isDynamic()) {
        retVal = evaluateDynamicOperation(operation, userId, null,
          resourceObjectID);
      } else {
        retVal = !uho.isDeny();
      }
    }
    // If no user permission on the operation exists check the permissions for the user userGroups.
    else {
      List<UserGroup> userUserGroups = user.getUserGroups();
      for (UserGroup userGroup : userUserGroups) {
        Boolean groupPermission = isPermittedForGroup(userGroup.getId(),
          operationName,
          resourceObjectID);
        if ((groupPermission != null) && (groupPermission
          == prioritisePositive)) {
          // Assign the permission we got for the userGroup to the return value only if
          // a. group permission is true and we are prioritising positive
          // b. or the groupPermission is false and we are prioritising negative permissions.
          retVal = groupPermission;
        }
      }
    }

    return retVal;
  }

  public Boolean isPermittedForGroup(String groupID, String operationName) {
    return isPermittedForGroup(groupID, operationName, null);
  }

  public Boolean isPermittedForGroup(String groupID, String operationName,
    String resourceObjectID) {
    LOGGER.log(Level.FINEST,
      "Checking permissions for userGroup {0}, operation {1} and resource with object ID {2}.",
      new String[]{groupID, operationName, resourceObjectID});

    UserGroup userGroup = userGroupRepository.fetchById(groupID);
    Operation operation = operationRepository.findByName(operationName);
    String resourceId =
      (resourceObjectID == null) ? null
        : resourceRepository.findByObjectId(resourceObjectID).getId();
    Boolean retVal = false;
    UserGroupHasOperation gho = (resourceId == null)
      ? userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(groupID, operationName)
      : userGroupHasOperationRepository
        .findByUserGroupIdAndResourceIdAndOperationName(groupID, resourceId,
          operationName);
    if (gho != null) {
      // First check whether this is a dynamic operation.
      if (operation.isDynamic()) {
        retVal = evaluateDynamicOperation(operation, null, groupID,
          resourceObjectID);
      } else {
        retVal = !gho.isDeny();
      }
    } else if (userGroup.getParent() != null) {
      // If this userGroup is not assigned the operation check the userGroup's
      // parents until a result is found or until no other parent exists.
      retVal = isPermittedForGroup(userGroup.getParent().getId(), operationName,
        resourceObjectID);
    }

    return retVal;
  }

  /**
   * Checks whether a specific operation, for a specific userGroup on a
   * specific resource is allowed. The check is performed by checking whether
   * the specified userGroup is assigned the specific operation and resource,
   * if yes, check the deny flag of the operation assignment to decide whether
   * the userGroup is permitted the operation. In case no assignment for this
   * operation can be found for the userGroup the userGroup's ancestors are
   * checked recursively until we arrive to a decision.
   *
   * @param userGroupID The id of the userGroup for which to check the
   * operation
   * @param operationName The name of the operation to check
   * @param resourceName The name of the resource to check
   * @return true, if the group is allowed the operation. false, if the group is denied the
   * operation. null, if there is no information available to reply accordingly.
   */
  public Boolean isPermittedForGroupByResource(String userGroupID, String operationName,
      String resourceName) {
    return isPermittedForGroupByResource(userGroupID, operationName, resourceName, null);
  }

  /**
   * Checks whether a specific operation, for a specific userGroup on a specific resource is
   * allowed. The check is performed by checking whether the specified userGroup is assigned the
   * specific operation and resource, if yes, check the deny flag of the operation assignment to
   * decide whether the userGroup is permitted the operation. In case no assignment for this
   * operation can be found for the userGroup the userGroup's ancestors are checked recursively
   * until we arrive to a decision.
   *
   * @param userGroupID The id of the userGroup for which to check the operation
   * @param operationName The name of the operation to check
   * @param resourceName The name of the resource to check
   * @param resourceObjectId A specific object Id the resource is referring to
   * @return true, if the group is allowed the operation. false, if the group is denied the
   * operation. null, if there is no information available to reply accordingly.
   */
  public Boolean isPermittedForGroupByResource(String userGroupID,
    String operationName,
    String resourceName, String resourceObjectId) {
    LOGGER.log(Level.FINEST,
      "Checking permissions for userGroup {0}, operation {1} and resource with name {2} and object ID {3}.",
      new String[]{userGroupID, operationName, resourceName, resourceObjectId});

    UserGroup userGroup = userGroupRepository.fetchById(userGroupID);

    Boolean retVal = null;
    UserGroupHasOperation gho;
    if (StringUtils.isNotBlank(resourceObjectId)) {
      gho = userGroupHasOperationRepository
          .findByUserGroupIdAndOperationNameAndResourceNameAndResourceObjectId(userGroupID,
              operationName, resourceName,
              resourceObjectId);
    } else {
      gho = userGroupHasOperationRepository
          .findByUserGroupIdAndOperationNameAndResourceName(userGroupID, operationName,
              resourceName);
    }
    if (gho != null) {
      retVal = !gho.isDeny();
    } else if (userGroup.getParent() != null) {
      // If this group is not assigned the operation check the group's
      // parents until a result is found or until no other parent exists.
      retVal = isPermittedForGroup(userGroup.getParent().getId(), operationName,
        resourceObjectId);
    }

    return retVal;
  }

  private Set<String> getUsersForOperation(String operationName,
    String resourceObjectID, boolean checkUserGroups, boolean getAllowed,
      boolean removeSuperAdmin) {
    Set<String> allUsers = userRepository.getUserIds(false);
    // Superadmin users are allowed the operation by default
    Set<String> returnedUsers = new HashSet<>();
    if (!getAllowed || removeSuperAdmin) {
      for (String superadminId : userRepository.getUserIds(true)) {
        allUsers.remove(superadminId);
      }
    } else {
      returnedUsers = userRepository.getUserIds(true);
    }

    getUsersForOperationDynamic(operationName, allUsers, resourceObjectID,
      getAllowed,
      returnedUsers);

    //Checking user group permissions if desired, or returns users found so far.
    return checkUserGroups ?
      getUsersForOperationByUserGroups(operationName, resourceObjectID,
        getAllowed, allUsers,
        returnedUsers) : returnedUsers;
  }

  private Set<String> getUsersForOperationDynamic(String operationName,
    Set<String> allUsers,
    String resourceObjectID, boolean getAllowed, Set<String> returnedUsers) {

    String resourceId = null;
    if (resourceObjectID != null) {
      resourceId = resourceRepository.findByObjectId(resourceObjectID).getId();
    }

    // Checking the permissions of users themselves
    List<UserHasOperation> uhoList;
    if (resourceId == null) {
      uhoList = userHasOperationRepository.findByOperationName(operationName);
    } else {
      uhoList = userHasOperationRepository
        .findByResourceIdAndOperationName(resourceId, operationName);
    }

    for (UserHasOperation uho : uhoList) {
      allUsers.remove(uho.getUser().getId());

      // Check if operation is dynamic and if yes evaluate the operation
      if (uho.getOperation().isDynamic()) {
        Boolean dynamicResult = evaluateDynamicOperation(uho.getOperation(),
          uho.getUser().getId(), null, resourceObjectID);
        if ((dynamicResult != null) && (dynamicResult == getAllowed)) {
          returnedUsers.add(uho.getUser().getId());
        }
      } else if (!uho.isDeny() == getAllowed) {
        returnedUsers.add(uho.getUser().getId());
      }
    }

    return returnedUsers;
  }

  @SuppressWarnings("squid:S3776")
  private Set<String> getUsersForOperationByUserGroups(String operationName,
    String resourceObjectID, boolean getAllowed, Set<String> allUsers,
    Set<String> returnedUsers) {
    // Using Iterator to iterate over allUsers in order to avoid
    // ConcurrentModificationException caused by user removal in the for loop
    Iterator<String> userIt = allUsers.iterator();
    while (userIt.hasNext()) {
      String userId = userIt.next();
      List<UserGroup> userUserGroups = userRepository.fetchById(userId)
        .getUserGroups();
      Boolean userPermission = null;
      for (UserGroup userGroup : userUserGroups) {
        Boolean groupPermission;
        if (resourceObjectID == null) {
          groupPermission = isPermittedForGroup(userGroup.getId(),
            operationName);
        } else {
          groupPermission = isPermittedForGroup(userGroup.getId(),
            operationName, resourceObjectID);
        }
        // We have the following cases depending on the userGroup permission:
        // a. If it was positive and we are prioritising positive permissions the user
        // is allowed and we end the check for this user. The user will be added to
        // the returned users if getAllowed == true.
        // b. If it was negative and we are prioritising negative permissions the user
        // is not allowed and we end the check for this user. The user will be added to
        // the returned users if getAllowed == false.
        // c. In all other cases we wait until the rest of the user userGroups are checked
        // before we make a final decision. For this reason we assign the groupPermission
        // to the userPermission variable to be checked after userGroup check is finished.
        if (groupPermission != null) {
          userIt.remove();
          if (groupPermission.booleanValue() == prioritisePositive) {
            if (groupPermission.booleanValue() == getAllowed) {
              returnedUsers.add(userId);
            }
            userPermission = null;
            break;
          } else {
            userPermission = groupPermission;
          }
        }
      }
      if ((userPermission != null) && (userPermission == getAllowed)) {
        returnedUsers.add(userId);
      }
    }
    return returnedUsers;
  }

  public Set<String> getAllowedUsersForOperation(String operationName,
    boolean checkUserGroups) {
    return getUsersForOperation(operationName, null, checkUserGroups, true, false);
  }

  public Set<String> getAllowedUsersForOperation(String operationName,
    String resourceObjectID,
    boolean checkUserGroups) {
    return getUsersForOperation(operationName, resourceObjectID,
      checkUserGroups, true, false);
  }

  public Set<String> getBlockedUsersForOperation(String operationName,
    boolean checkUserGroups) {
    return getUsersForOperation(operationName, null, checkUserGroups, false, false);
  }

  public Set<String> getBlockedUsersForOperation(String operationName,
    String resourceObjectID,
    boolean checkUserGroups) {
    return getUsersForOperation(operationName, resourceObjectID,
      checkUserGroups, false, false);
  }

  public Set<String> getAllowedUsersForOperationRemoveSuperAdmin(String operationName,
      String resourceObjectID,
      boolean checkUserGroups) {
    return getUsersForOperation(operationName, resourceObjectID,
        checkUserGroups, true, true);
  }

  private Set<String> getGroupsForOperation(String operationName,
    String resourceObjectID,
    boolean checkAncestors, boolean getAllowed) {
    Set<String> allGroups = userGroupRepository.getAllIds();
    Set<String> returnedGroups = new HashSet<>();

    String resourceId = null;
    if (resourceObjectID != null) {
      resourceId = resourceRepository.findByObjectId(resourceObjectID).getId();
    }
    List<UserGroupHasOperation> ghoList;
    if (resourceId == null) {
      ghoList = userGroupHasOperationRepository
        .findByOperationName(operationName);
    } else {
      ghoList = userGroupHasOperationRepository.
        findByResourceIdAndOperationName(resourceId, operationName);
    }
    for (UserGroupHasOperation gho : ghoList) {
      allGroups.remove(gho.getUserGroup().getId());

      // Check if operation is dynamic and if yes evaluate the operation
      if (gho.getOperation().isDynamic()) {
        Boolean dynamicResult = evaluateDynamicOperation(gho.getOperation(),
          null, gho.getUserGroup().getId(), null);
        if ((dynamicResult != null) && (dynamicResult == getAllowed)) {
          returnedGroups.add(gho.getUserGroup().getId());
        }
      } else if (!gho.isDeny() == getAllowed) {
        returnedGroups.add(gho.getUserGroup().getId());
      }
    }

    return getGroupsForOperationAncestors(operationName, resourceObjectID,
      checkAncestors,
      getAllowed, allGroups, returnedGroups);
  }

  private Set<String> getGroupsForOperationAncestors(String operationName,
    String resourceObjectID,
    boolean checkAncestors, boolean getAllowed, Set<String> allGroups,
    Set<String> returnedGroups) {
    // Check the ancestors of the remaining userGroups if so requested
    if (checkAncestors) {
      for (String groupId : allGroups) {
        Boolean groupPermission;
        if (resourceObjectID == null) {
          groupPermission = isPermittedForGroup(groupId, operationName);
        } else {
          groupPermission = isPermittedForGroup(groupId, operationName,
            resourceObjectID);
        }
        if ((groupPermission != null) && (groupPermission == getAllowed)) {
          returnedGroups.add(groupId);
        }
      }
    }

    return returnedGroups;
  }

  public Set<String> getAllowedGroupsForOperation(String operationName,
    boolean checkAncestors) {
    return getGroupsForOperation(operationName, null, checkAncestors, true);
  }

  public Set<String> getAllowedGroupsForOperation(String operationName,
    String resourceObjectID, boolean checkAncestors) {
    return getGroupsForOperation(operationName, resourceObjectID,
      checkAncestors, true);
  }

  public Set<String> getBlockedGroupsForOperation(String operationName,
    boolean checkAncestors) {
    return getGroupsForOperation(operationName, null, checkAncestors, false);
  }

  public Set<String> getBlockedGroupsForOperation(String operationName,
    String resourceObjectID, boolean checkAncestors) {
    return getGroupsForOperation(operationName, resourceObjectID,
      checkAncestors, false);
  }

  private Boolean evaluateDynamicOperation(Operation operation,
    String userID, String groupID, String resourceObjectID) {
    LOGGER.log(Level.FINEST, "Evaluating dynamic operation ''{0}''.",
      operation.getName());

    Boolean retVal;
    String algorithm = operation.getDynamicCode();
    // Create a BeanShell interpreter for this operation.
    Interpreter i = new Interpreter();
    // Pass parameters to the algorithm.
    try {
      i.set("userID", userID);
      i.set("groupID", groupID);
      i.set("resourceObjectID", resourceObjectID);
      i.eval(algorithm);
      retVal = (Boolean) i.get("retVal");
    } catch (EvalError ex) {
      // Catching the EvalError in order to convert it to
      // a RuntimeException which will also rollback the transaction.
      throw new DynamicOperationException(
        "Error evaluating dynamic operation '"
          + operation.getName() + "'.");
    }

    return retVal;
  }

  public Set<String> getPermittedOperationsForUser(String userID,
    boolean checkUserGroups) {
    User user = userRepository.fetchById(userID);
    return getOperationsForUser(user, null, checkUserGroups);
  }

  public Set<String> getPermittedOperationsForUser(String userID,
    String resourceObjectID,
    boolean checkUserGroups) {
    User user = userRepository.fetchById(userID);
    Resource resource = resourceRepository.findByObjectId(resourceObjectID);
    return getOperationsForUser(user, resource, checkUserGroups);
  }

  private Set<String> getOperationsForUser(User user, Resource resource,
    boolean checkUserGroups) {
    Set<String> allowedOperations = new HashSet<>();
    Set<String> deniedOperations = new HashSet<>();
    // If the user is a superadmin then they are allowed all operations
    if (user.isSuperadmin()) {
      for (Operation operation : operationRepository.findAll()) {
        allowedOperations.add(operation.getName());
      }
      return allowedOperations;
    } else {
      // Check operations attributed to the user
      for (UserHasOperation uho : ListUtils.emptyIfNull(user.getUserHasOperations())) {
        if (uho.getResource() == resource) {
          if (getOperationsForUserIsAllowed(user, resource, uho)) {
            allowedOperations.add(uho.getOperation().getName());
          } else if (getOperationsForUserIsNotAllowed(user, resource, uho)) {
            deniedOperations.add(uho.getOperation().getName());
          }
        }
      }
      return getOperationsForUserOperations(user, resource, checkUserGroups,
        allowedOperations, deniedOperations);
    }
  }

  private boolean getOperationsForUserIsAllowed(User user, Resource resource,
    UserHasOperation uho) {
    return ((uho.getOperation().isDynamic() && evaluateDynamicOperation(
      uho.getOperation(), user.getId(), null, resource.getObjectId()))
      || (!uho.getOperation().isDynamic() && !uho.isDeny()));
  }

  private boolean getOperationsForUserIsNotAllowed(User user, Resource resource,
    UserHasOperation uho) {
    return ((uho.getOperation().isDynamic() && !evaluateDynamicOperation(
      uho.getOperation(), user.getId(), null, resource.getObjectId()))
      || (!uho.getOperation().isDynamic() && uho.isDeny()));
  }

  private Set<String> getOperationsForUserOperations(User user,
    Resource resource,
    boolean checkUserGroups,
    Set<String> allowedOperations, Set<String> deniedOperations) {
    if (checkUserGroups) {
      // Then check operations the user may have via their userGroups
      Set<String> allowedGroupOperations = new HashSet<>();
      Set<String> deniedGroupOperations = new HashSet<>();
      // First get all the ids of operations allowed or denied through the user userGroups
      for (UserGroup userGroup : user.getUserGroups()) {
        while (userGroup != null) {
          allowedGroupOperations
            .addAll(getOperationsForGroup(userGroup, resource, true));
          deniedGroupOperations
            .addAll(getOperationsForGroup(userGroup, resource, false));
          userGroup = userGroup.getParent();
        }
      }
      // And then check for each allowed operation if it is explicitly denied
      // to the user or if it is denied through another userGroup (only if prioritisePositive == false)
      for (String groupOperation : allowedGroupOperations) {
        if (!deniedOperations.contains(groupOperation)
          && (prioritisePositive || (!deniedGroupOperations
          .contains(groupOperation)))) {
          allowedOperations.add(groupOperation);
        }
      }
    }
    return allowedOperations;
  }

  private Set<String> getOperationsForGroup(UserGroup userGroup,
    Resource resource,
    boolean allowed) {
    Set<String> retVal = new HashSet<>();
    for (UserGroupHasOperation gho : userGroup.getUserGroupHasOperations()) {
      if (gho.getResource() == resource) {
        String resourceObjectID =
          (resource != null) ? resource.getObjectId() : null;
        if ((gho.getOperation().isDynamic() &&
          (evaluateDynamicOperation(gho.getOperation(), null, userGroup.getId(),
            resourceObjectID)
            == allowed))
          || (!gho.getOperation().isDynamic() && (!gho.isDeny() == allowed))) {
          retVal.add(gho.getOperation().getName());
        }
      }
    }
    return retVal;
  }

  public Set<ResourceDTO> getResourceForOperation(String userID,
    String... operations) {
    return getResourceForOperation(userID, true, false, operations);
  }

  public Set<ResourceDTO> getResourceForOperation(String userID,
    boolean getAllowed, String... operations) {
    return getResourceForOperation(userID, getAllowed, false, operations);
  }

  public Set<ResourceDTO> getResourceForOperation(String userID,
    boolean getAllowed, boolean checkUserGroups, String... operations) {
    Set<ResourceDTO> resourceDTOList = new HashSet<>();
    User user = userRepository.fetchById(userID);
    for (UserHasOperation uho : user.getUserHasOperations()) {
      if (uho.isDeny() != getAllowed && Stream.of(operations)
        .anyMatch(o -> o.equals(uho.getOperation().getName()))) {
        resourceDTOList
          .add(resourceMapper.mapToDTO(
            resourceRepository.fetchById(uho.getResource().getId())));
      }
    }
    /* also the resources of the userGroups the user belongs to should be retrieved */
    if (checkUserGroups) {
      for (UserGroup userGroup : user.getUserGroups()) {
        for (UserGroupHasOperation gho : userGroup
          .getUserGroupHasOperations()) {
          if (gho.isDeny() != getAllowed && Stream.of(operations)
            .anyMatch(o -> o.equals(gho.getOperation().getName()))) {
            resourceDTOList.add(
              resourceMapper.mapToDTO(
                resourceRepository.fetchById((gho.getResource().getId()))));
          }
        }
      }
    }
    return resourceDTOList;
  }

  public OperationDTO getOperationByID(String operationID) {
    Operation o = operationRepository.fetchById(operationID);
    if (o != null) {
      return operationMapper.mapToDTO(o);
    } else {
      return null;
    }
  }

  public List<UserGroupHasOperationDTO> getGroupOperations(String groupName) {

    return userGroupHasOperationMapper.mapToDTO(
      userGroupHasOperationRepository.findByUserGroupName(groupName));
  }

  public List<UserGroupHasOperationDTO> getGroupOperations(
    List<String> groupNames) {
    List<UserGroupHasOperation> entities = new ArrayList<>();
    groupNames
      .forEach(groupName ->
        entities.addAll(
          userGroupHasOperationRepository.findByUserGroupName(groupName))
      );

    return userGroupHasOperationMapper.mapToDTO(entities);
  }
}
