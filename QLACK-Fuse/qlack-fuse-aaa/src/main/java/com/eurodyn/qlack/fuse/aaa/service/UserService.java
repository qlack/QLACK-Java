package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.mapper.SessionMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserMapper;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.QUser;
import com.eurodyn.qlack.fuse.aaa.model.QUserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.repository.SessionRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class that is used to define a number of crud methods and configure the User model
 *
 * @author European Dynamics SA
 */
@Log
@Primary
@Service
@Validated
@Transactional
public class UserService {

  private static final Logger LOGGER = Logger
      .getLogger(UserService.class.getName());

  // Service REFs
  private AccountingService accountingService;
  private LdapUserUtil ldapUserUtil;
  // Repositories
  private final UserRepository userRepository;
  private final UserAttributeRepository userAttributeRepository;
  private final SessionRepository sessionRepository;
  private final UserGroupRepository userGroupRepository;
  // Mappers
  private final UserMapper userMapper;
  private final SessionMapper sessionMapper;
  private final UserAttributeMapper userAttributeMapper;

  //QueryDSL helpers
  private QUser qUser = QUser.user;
  private QUserAttribute qUserAttribute = QUserAttribute.userAttribute;
  private QSession qSession = QSession.session;

  private final PasswordEncoder passwordEncoder;

  @SuppressWarnings("squid:S00107")
  public UserService(AccountingService accountingService,
      LdapUserUtil ldapUserUtil,
      UserRepository userRepository,
      UserAttributeRepository userAttributeRepository,
      SessionRepository sessionRepository,
      UserGroupRepository userGroupRepository,
      UserMapper userMapper,
      SessionMapper sessionMapper, UserAttributeMapper userAttributeMapper,
      PasswordEncoder passwordEncoder) {
    this.accountingService = accountingService;
    this.ldapUserUtil = ldapUserUtil;
    this.userRepository = userRepository;
    this.userAttributeRepository = userAttributeRepository;
    this.sessionRepository = sessionRepository;
    this.userGroupRepository = userGroupRepository;
    this.userMapper = userMapper;
    this.sessionMapper = sessionMapper;
    this.userAttributeMapper = userAttributeMapper;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Creates a new user in AAA. If the password encoder you have chosen needs a salt, make sure you
   * populate one into {@link UserDTO}. You can find a secure seed/salt generator in
   * qlack-fuse-crypto's generateSecureRandom.
   *
   * @param salt the salt
   * @param dto The DTO with the user details to create.
   * @return a user id
   */
  public String createUser(UserDTO dto, Optional<String> salt) {
    User user = userMapper.mapToEntity(dto);
    setUserPassword(dto, user, salt);
    if (CollectionUtils.isNotEmpty(user.getUserAttributes())) {
      for (UserAttribute attribute : user.getUserAttributes()) {
        attribute.setUser(user);
      }
    }
    userRepository.save(user);

    return user.getId();
  }

  /**
   * Updates a user in AAA.
   *
   * @param dto the {@link UserDTO}
   * @param updatePassword updatePassword value checks if password needs to be updated
   * @param createIfMissing the createIfMissing
   */
  public void updateUser(UserDTO dto, boolean updatePassword,
      boolean createIfMissing) {
    User user = userRepository.fetchById(dto.getId());
    userMapper.mapToExistingEntity(dto, user);

    if (updatePassword) {
      Optional<String> salt =
          user.getSalt() != null ? Optional.of(user.getSalt()) :
              Optional.empty();
      setUserPassword(dto, user, salt);
    }
    if (CollectionUtils.isNotEmpty(dto.getUserAttributes())) {
      for (UserAttributeDTO attribute : dto.getUserAttributes()) {
        updateAttribute(attribute, createIfMissing);
      }
    }
  }

  /**
   * Deletes user by its id
   *
   * @param userID the userId
   */
  public void deleteUser(String userID) {
    User user = userRepository.fetchById(userID);
    userRepository.delete(user);
  }

  /**
   * Retrieves {@link UserDTO} id
   *
   * @param userID the userID
   * @return the {@link UserDTO} id
   */
  public UserDTO getUserById(String userID) {
    User user = userRepository.fetchById(userID);

    return userMapper.mapToDTO(user);
  }

  /**
   * Retrieves the users
   *
   * @param userIDs the userIds
   * @return the users
   */
  public Set<UserDTO> getUsersById(Collection<String> userIDs) {
    Predicate predicate = qUser.id.in(userIDs);

    return userRepository.findAll(predicate).stream()
        .map(userMapper::mapToDTO)
        .collect(Collectors.toSet());
  }

  public Map<String, UserDTO> getUsersByIdAsHash(Collection<String> userIDs) {
    Predicate predicate = qUser.id.in(userIDs);

    return userRepository.findAll(predicate).stream()
        .map(userMapper::mapToDTO)
        .collect(Collectors.toMap(UserDTO::getId, dto -> dto));
  }

  /**
   * Retrieves the name of the user
   *
   * @param userName the userName
   * @return the user by its name
   */
  public UserDTO getUserByName(String userName) {
    return userMapper.mapToDTO(userRepository.findByUsername(userName));
  }

  /**
   * Update user status
   *
   * @param userID the userID
   * @param status the status
   */
  public void updateUserStatus(String userID, byte status) {
    User user = userRepository.fetchById(userID);
    user.setStatus(status);
  }

  /**
   * Retrieves the status of user
   *
   * @param userID the userId
   * @return the user status
   */
  public byte getUserStatus(String userID) {
    User user = userRepository.fetchById(userID);
    return user.getStatus();
  }

  /**
   * Checks whether is superAdmin or not
   *
   * @param userID the userID
   * @return true or false if is superAdmin
   */
  public boolean isSuperadmin(String userID) {
    User user = userRepository.fetchById(userID);
    if (user != null) {
      return user.isSuperadmin();
    } else {
      return false;
    }
  }

  /**
   * Checks whether is external or not
   *
   * @param userID the id of user
   * @return whether is external or not
   */
  public boolean isExternal(String userID) {
    User user = userRepository.fetchById(userID);
    return user.isExternal();
  }

  public String canAuthenticate(final String username, String password) {
    String retVal = null;

    /* Try to find this user in the database */
    User user = userRepository.findByUsername(username);

    /* If the user was found proceed trying to authenticate it. Otherwise, if LDAP integration is
     * enabled try to authenticate the user in LDAP. Note that if the user is successfully
     * authenticated with LDAP, a new user will also be created/duplicated in AAA as an external
     * user.
     */
    if (user != null && BooleanUtils.isFalse(user.isExternal())) {
      if (StringUtils.isNotBlank(user.getSalt())) {
        password = user.getSalt() + password;
      }
      if (passwordEncoder.matches(password, user.getPassword())) {
        retVal = user.getId();
      }
    } else {
      if (ldapUserUtil.getProperties().isEnabled()) {
        retVal = ldapUserUtil.canAuthenticate(username, password);
      }
    }

    return retVal;
  }

  /**
   * Login a {@link UserDTO}
   *
   * @param userID the userId
   * @param applicationSessionID the applicationSessionID
   * @param terminateOtherSessions the terminateOtherSessions
   * @return the {@link UserDTO}
   */
  public UserDTO login(String userID, String applicationSessionID,
      boolean terminateOtherSessions) {
    User user = userRepository.fetchById(userID);

    // Check if other sessions of this user need to be terminated first.
    if (terminateOtherSessions && (user.getSessions() != null)) {
      for (Session session : user.getSessions()) {
        if (session.getTerminatedOn() == null) {
          accountingService.terminateSession(session.getId());
        }
      }

    }

    // Create a new session for the user.
    SessionDTO session = new SessionDTO();
    session.setUserId(user.getId());
    session.setApplicationSessionId(applicationSessionID);
    String sessionId = accountingService.createSession(session);

    // Create a DTO representation of the user and populate the session Id of the session that was
    // just created.
    final UserDTO userDTO = userMapper.mapToDTO(user);
    userDTO.setSessionId(sessionId);

    return userDTO;
  }

  /**
   * Logout the user
   *
   * @param userID the userID
   * @param applicationSessionID the applicationSessionID
   */
  public void logout(String userID, String applicationSessionID) {
    User user = userRepository.fetchById(userID);

    if (user.getSessions() != null) {
      for (Session session : user.getSessions()) {
        if (((applicationSessionID != null) && (session
            .getApplicationSessionId().equals(applicationSessionID)))
            || ((applicationSessionID == null) && (session
            .getApplicationSessionId() == null))) {
          accountingService.terminateSession(session.getId());
        }
      }
    }
  }

  /**
   * Log out all from session
   */
  public void logoutAll() {
    Predicate predicate = qSession.terminatedOn.isNull();
    List<Session> queryResult = sessionRepository.findAll(predicate);
    for (Session session : queryResult) {
      logout(session.getUser().getId(),
          session.getApplicationSessionId());
    }

  }

  /**
   * Retrieves the logged in users or null value if no one has logged in
   *
   * @param userID the user id
   * @return a list of users that have already logged in the system or null value if no one has
   * logged in
   */
  public List<SessionDTO> isUserAlreadyLoggedIn(String userID) {
    Predicate predicate = qSession.user.id.eq(userID)
        .and(qSession.terminatedOn.isNull());
    List<SessionDTO> retVal = sessionMapper.mapToDTO(sessionRepository
        .findAll(predicate, Sort.by("createdOn").ascending()));

    return retVal.isEmpty() ? null : retVal;
  }

  /**
   * Check if user's group children belongs to any group name
   *
   * @param userID the id of User
   * @param groupName the group name
   * @param includeChildren whether include children or not
   * @return a {@link Boolean} value whether belongs to or not
   */
  public boolean belongsToGroupByName(String userID, String groupName,
      boolean includeChildren) {
    User user = userRepository.fetchById(userID);
    UserGroup userGroup = userGroupRepository.findByName(groupName);
    boolean retVal = userGroup.getUsers().contains(user);

    if (!retVal && includeChildren) {
      for (UserGroup child : userGroup.getChildren()) {
        if (belongsToGroupByName(userID, child.getName(),
            includeChildren)) {
          return true;
        }
      }
    }

    return retVal;
  }

  /**
   * Updates attributes
   *
   * @param attributes the attributes
   * @param createIfMissing the createIfMissing check value
   */
  public void updateAttributes(Collection<UserAttributeDTO> attributes,
      boolean createIfMissing) {
    for (UserAttributeDTO attributeDTO : attributes) {
      updateAttribute(attributeDTO, createIfMissing);
    }
  }

  /**
   * Updates user's attribute
   *
   * @param attributeDTO the UserAttributeDTO
   * @param createIfMissing the createIfMissing
   */
  public void updateAttribute(UserAttributeDTO attributeDTO,
      boolean createIfMissing) {
    String userId = attributeDTO.getUserId();
    String name = attributeDTO.getName();

    UserAttribute attribute = userAttributeRepository
        .findByUserIdAndName(userId, name);
    if (attribute != null) {
      mapAttribute(attribute, attributeDTO);
      userAttributeRepository.save(attribute);
    } else if (createIfMissing) {
      attribute = new UserAttribute();
      mapAttribute(attribute, attributeDTO);
      userAttributeRepository.save(attribute);
    }
  }

  /**
   * Maps a DTO to existing Entity
   *
   * @param attribute the user's attribute
   * @param attributeDTO the UserAttributeDTO
   */
  private void mapAttribute(UserAttribute attribute,
      UserAttributeDTO attributeDTO) {
    String userId = attributeDTO.getUserId();
    User user = userRepository.fetchById(userId);
    attribute.setUser(user);
    userAttributeMapper.mapToExistingEntity(attributeDTO, attribute);
  }

  /**
   * Deletes user's attribute
   *
   * @param userID the userId
   * @param attributeName the attributeName
   */
  public void deleteAttribute(String userID, String attributeName) {
    UserAttribute attribute = userAttributeRepository
        .findByUserIdAndName(userID, attributeName);
    if (attribute != null) {
      userAttributeRepository.delete(attribute);
    }
  }

  /**
   * Retrieves the {@link UserAttributeDTO}
   *
   * @param userID the userId
   * @param attributeName the attributeName
   * @return the {@link UserAttributeDTO} dto
   */
  public UserAttributeDTO getAttribute(String userID, String attributeName) {
    UserAttribute attribute = userAttributeRepository
        .findByUserIdAndName(userID, attributeName);
    return userAttributeMapper.mapToDTO(attribute);
  }

  public List<UserAttributeDTO> getAttributes(Collection<String> userIds, String attributeName) {
    final Collection<UserAttribute> attributes = userAttributeRepository
        .findAllByUserIdInAndName(userIds, attributeName);

    return userAttributeMapper.mapToDTO((List) attributes);
  }

  /**
   * Retrieves the user ids for attribute
   *
   * @param userIDs the userIds
   * @param attributeName the attributeName
   * @param attributeValue the attributeValue
   * @return the user ids
   */
  public Set<String> getUserIDsForAttribute(Collection<String> userIDs,
      String attributeName, String attributeValue) {
    BooleanExpression predicate = qUser.userAttributes.any().name
        .eq(attributeName)
        .and(qUser.userAttributes.any().data.eq(attributeValue));
    if ((userIDs != null) && (!userIDs.isEmpty())) {
      predicate = predicate.and(qUser.id.in(userIDs));
    }

    return userRepository.findAll(predicate).stream()
        .map(User::getId)
        .collect(Collectors.toSet());
  }

  /**
   * Retrieves the user ids for specific attribute
   *
   * @param attributeName the attributeName
   * @param attributeValue the attributeValue
   * @return the user ids
   */
  public List<String> getUserIdByAttribute(String attributeName, String attributeValue) {
    Predicate predicate = qUserAttribute.name.eq(attributeName)
        .and(qUserAttribute.data.eq(attributeValue));
    // convert Set to List
    List<UserAttributeDTO> qResult = userAttributeMapper
        .mapToDTO(userAttributeRepository.findAll(predicate));
    ArrayList<UserAttributeDTO> list = new ArrayList<>(qResult);

    return list.stream()
        .map(UserAttributeDTO::getUserId)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves users
   *
   * @param criteria the criteria that is specified to search for a user
   * @return a list of Users
   */
  public Iterable<UserDTO> findUsers(UserSearchCriteria criteria) {
    Predicate predicate = buildPredicate(criteria);
    if (criteria.getPageable() != null) {

      return listUsersPaginated(predicate, criteria.getPageable());
    } else {

      return listUsers(predicate);
    }
  }

  /**
   * Retrieves users
   *
   * @param predicate the predicate
   * @return a list of {@link UserDTO} objects
   */
  private List<UserDTO> listUsers(Predicate predicate) {

    return userRepository.findAll(predicate).stream()
        .map(userMapper::mapToDTO)
        .collect(Collectors.toList());
  }

  private Predicate buildPredicate(UserSearchCriteria criteria) {
    Predicate predicate = new BooleanBuilder();
    if (criteria.getIncludeGroupIds() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.userGroups.any().id.in(criteria.getIncludeGroupIds()));
    }
    if (criteria.getExcludeGroupIds() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.userGroups.any().id.notIn(criteria.getExcludeGroupIds()));
    }
    if (criteria.getIncludeIds() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.id.in(criteria.getIncludeIds()));
    }
    if (criteria.getExcludeIds() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.id.notIn(criteria.getExcludeIds()));
    }
    if (criteria.getIncludeStatuses() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.status.in(criteria.getIncludeStatuses()));
    }
    if (criteria.getExcludeStatuses() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.status.notIn(criteria.getExcludeStatuses()));
    }
    if (criteria.getUsername() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.username.eq(criteria.getUsername()));
    }
    if (criteria.getSuperadmin() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.superadmin.eq(criteria.getSuperadmin()));
    }
    if (criteria.getUsernameLike() != null) {
      predicate = ((BooleanBuilder) predicate)
          .and(qUser.username.like(criteria.getUsernameLike()));
    }

    return predicate;
  }

  private Page<UserDTO> listUsersPaginated(Predicate predicate,
      Pageable pageable) {

    return userRepository.findAll(predicate, pageable)
        .map(userMapper::mapToDTO);
  }

  public long findUserCount(UserSearchCriteria criteria) {
    Predicate predicate = buildPredicate(criteria);

    return userRepository.findAll(predicate).size();
  }

  /**
   * Checks the uniqueness of an attribute value
   *
   * @param attributeName the attributeName
   * @param userID the userId
   * @return {@link Boolean} value whether the attribute value is unique or not
   */
  public boolean isAttributeValueUnique(String attributeName, String userID) {

    boolean isAttributeValueUnique = false;
    QUserAttribute quserAttribute = QUserAttribute.userAttribute;

    Predicate predicate = quserAttribute.name.eq(attributeName)
        .and(quserAttribute.data.eq(attributeName));
    // convert Set to List
    List<UserAttributeDTO> qResult = userAttributeMapper
        .mapToDTO(userAttributeRepository.findAll(predicate));
    ArrayList<UserAttributeDTO> list = new ArrayList<>(qResult);
    //in case of no user exists with this user attribute value	or there is only the given user
    if ((list.size() == 1 && list.get(0).getUserId().equals(userID)) || (list
        .isEmpty())) {
      isAttributeValueUnique = true;
    }
    return isAttributeValueUnique;
  }

  public Page<UserDTO> findAll(Predicate predicate, Pageable pageable) {
    return userMapper.map(userRepository.findAll(predicate, pageable));
  }

  /**
   * Sets user's password
   *
   * @param dto the {@link UserDTO}
   * @param user the {@link User}
   * @param salt the salt
   */
  private void setUserPassword(UserDTO dto, User user, Optional<String> salt) {
    if (dto == null || user == null) {
      return;
    }

    if (StringUtils.isBlank(dto.getPassword())) {
      LOGGER.log(Level.WARNING, "Password is empty.");
    } else {
      if (salt.isPresent()) {
        user.setSalt(salt.get());
        user
            .setPassword(passwordEncoder.encode(salt.get() + dto.getPassword()));
      } else {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
      }
    }
  }

  /**
   * Adds a list of userGroups for a specific user.
   *
   * @param userGroupIds the list that contains all the user group Ids to be added.
   * @param userId the user that userGroups will be added to.
   */
  public void addUserGroups(Collection<String> userGroupIds, String userId) {
    addUserGroups(userGroupIds, userRepository.fetchById(userId));
  }

  private void addUserGroups(Collection<String> userGroupIds, User user) {
    for (String userGroupId : userGroupIds) {
      UserGroup userGroup = userGroupRepository.fetchById(userGroupId);
      if (user.getUserGroups() == null) {
        user.setUserGroups(new ArrayList<UserGroup>());
      }
      user.getUserGroups().add(userGroup);
      if (userGroup.getUsers() == null) {
        userGroup.setUsers(new ArrayList<User>());
      }
      userGroup.getUsers().add(user);
    }
  }

  /**
   * Removes the userGroups for a specific user.
   *
   * @param userGroupIds the list that contains all the user groups Ids to be removed.
   * @param userId the user that userGroups will be removed from.
   */
  public void removeUserGroups(Collection<String> userGroupIds, String userId) {
    removeUserGroups(userGroupIds, userRepository.fetchById(userId));
  }

  private void removeUserGroups(Collection<String> userGroupIds, User user) {
    for (String userGroupId : userGroupIds) {
      UserGroup userGroup = userGroupRepository.fetchById(userGroupId);
      user.getUserGroups().remove(userGroup);
      userGroup.getUsers().remove(user);
    }
  }
}
