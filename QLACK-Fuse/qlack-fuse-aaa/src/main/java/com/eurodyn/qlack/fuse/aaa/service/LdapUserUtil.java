package com.eurodyn.qlack.fuse.aaa.service;


import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.util.LdapProperties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A Util class that is used to configure the LdapUser
 *
 * @author European Dynamics SA.
 */
@Slf4j
@Getter
@Setter
@Service
@Validated
public class LdapUserUtil {

  private final LdapProperties properties;
  private final UserRepository userRepository;
  private final UserGroupRepository userGroupRepository;
  private final UserAttributeRepository userAttributeRepository;
  private static final String CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
  private static final String EMPTY_STRING = "";

  public LdapUserUtil(LdapProperties ldapProperties, UserRepository userRepository,
      UserGroupRepository userGroupRepository,
      UserAttributeRepository userAttributeRepository) {
    this.properties = ldapProperties;
    this.userRepository = userRepository;
    this.userGroupRepository = userGroupRepository;
    this.userAttributeRepository = userAttributeRepository;
  }


  private Map<String, String> attributesMap;


  public void setLdapMappingAttrs(String ldapMappingAttrs) {
    attributesMap = new HashMap<>();
    String[] mappings = ldapMappingAttrs.split(",");
    for (String mapping : mappings) {
      String[] names = mapping.split("-");
      attributesMap.put(names[0], names[1]);
    }
  }

  /**
   * Check if the user can be authenticated with LDAP using 'simple' authentication (bind
   * operation).
   *
   * @param username The LDAP username of the user.
   * @param password The LDAP password of the user.
   * @return The AAA ID of the user if authenticated, null otherwise.
   */
  public String canAuthenticate(String username, String password) {
    if (!properties.isEnabled()) {
      return null;
    }

    DirContext ctx = ldapBind(username, password);
    if (ctx != null) {
      Map<String, List<String>> ldap = ldapSearch(ctx, username);
      if (ldap != null) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
          String userId = createUserFromLdap(username, ldap);
          ldapUnbind(ctx);
          return userId;
        } else {
          String userId = updateUserFromLdap(user, ldap);
          ldapUnbind(ctx);
          return userId;
        }
      } else {
        ldapUnbind(ctx);
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Searches username in AAA users database. If the user does not exists it is created by verifying
   * and getting information from the LDAP server
   *
   * @param username the username
   * @return a {@link User}
   */
  public User syncUserWithAAA(String username) {
    User u = null;

    if (properties.isEnabled()) {
      // Create initial context
      DirContext ctx = ldapBindAdminAuth(properties.getAdminUid(), properties.getAdminPassword());
      log.info(String.format("Successful bind to LDAP %s", properties.getUrl()));

      if (ctx == null || ldapSearch(ctx, username) == null) {
        if (ctx != null) {
          ldapUnbind(ctx);
        }
        log.error("Cannot connect/bind to the LDAP service. Please check your configuration.");
        return null;
      }

      User user = userRepository.findByUsername(username);

      if (user != null) {
        log.trace(String.format("%s is already synced with AAA.", user.getUsername()));
        ldapUnbind(ctx);
        return user;
      }

      u = createUserFromLdapWithoutAttributes(ldapSearch(ctx, username));
      ldapUnbind(ctx);
    } else {
      log.warn("LDAP configuration is not enabled. Please check your configuration properties.");
    }
    return u;
  }

  @SuppressWarnings("squid:S1149")
  private DirContext ldapBind(String username, String password) {
    Hashtable<String, String> env = new Hashtable<>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
    env.put(Context.PROVIDER_URL, properties.getUrl());
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL,
        properties.getMappingUid() + "=" + username + "," + properties.getBasedn());
    env.put(Context.SECURITY_CREDENTIALS, password);

    try {
      return new InitialDirContext(env);
    } catch (NamingException e) {
      log.warn("Cannot bind user to ldap service", e);
      return null;
    }
  }

  private DirContext ldapBindAdminAuth(String adminUsername, String ldapAdminPassword) {
    Hashtable<String, String> env = new Hashtable<>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
    env.put(Context.PROVIDER_URL, properties.getUrl());
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL,
        properties.getMappingUid() + "=" + adminUsername + "," + properties.getBasedn());
    env.put(Context.SECURITY_CREDENTIALS, ldapAdminPassword);

    try {
      return new InitialDirContext(env);
    } catch (NamingException e) {
      log.info("Cannot bind user to ldap service", e);
      return null;
    }
  }

  private Map<String, List<String>> ldapSearch(DirContext ctx, String username) {
    try {
      NamingEnumeration<SearchResult> results =
          ctx.search(properties.getBasedn(),
              "(" + properties.getMappingUid() + "=" + username + ")", null);
      if (results.hasMore()) {
        SearchResult result = results.next();
        Attributes attributes = result.getAttributes();

        Map<String, List<String>> untypedResult = new HashMap<>();
        NamingEnumeration<? extends Attribute> attributesEnumeration = attributes.getAll();
        while (attributesEnumeration.hasMore()) {
          Attribute attribute = attributesEnumeration.next();
          String id = attribute.getID();
          log.debug(MessageFormat.format("{0}:", id));

          List<String> untypedValues = new ArrayList<>();
          NamingEnumeration<?> valuesEnumeration = attribute.getAll();
          while (valuesEnumeration.hasMore()) {
            Object value = valuesEnumeration.next();
            log.debug(MessageFormat.format("\t{0}", value));
            if (value instanceof String) {
              String string = (String) value;
              untypedValues.add(string);
            }
          }

          untypedResult.put(id, untypedValues);
        }

        results.close();
        return untypedResult;
      } else {
        results.close();
        return null;
      }
    } catch (NamingException e) {
      log.warn("Cannot search ldap context", e);
      return null;
    }
  }

  private void ldapUnbind(DirContext ctx) {
    try {
      ctx.close();
    } catch (NamingException e) {
      log.warn("Cannot close ldap context", e);
    }
  }

  /**
   * Create a user from ldap
   *
   * @param username the username
   * @param ldap the ldap
   * @return the userId
   */
  private String createUserFromLdap(String username, Map<String, List<String>> ldap) {
    User user = new User();
    String userId = user.getId();

    user.setUsername(username);
    user.setStatus((byte) 1);
    user.setSuperadmin(false);
    user.setExternal(true);

    String groupId = addGroupFromLdap(user, ldap);
    if (groupId == null) {
      return null;
    }

    userRepository.save(user);
    createUserAttributesFromLdap(user, ldap);
    return userId;
  }

  /**
   * Create a user from LDAP
   *
   * @param ldapSearchResult an LDAP Search response Map
   * @return the userId
   */
  private User createUserFromLdapWithoutAttributes(Map<String, List<String>> ldapSearchResult) {

    String username = getFirst(ldapSearchResult, properties.getMappingUid());

    User user = new User();
    user.setUsername(username);
    user.setPassword(EMPTY_STRING);
    user.setStatus((byte) 1);
    user.setSuperadmin(false);
    user.setExternal(true);

    return userRepository.save(user);
  }

  /**
   * Creates a user from ldap
   *
   * @param user the user
   * @param ldap the ldap
   * @return the groupId
   */
  private String addGroupFromLdap(User user, Map<String, List<String>> ldap) {
    String groupId = getFirst(ldap, properties.getMappingGid());
    if (groupId == null) {
      return null;
    }

    UserGroup userGroup = userGroupRepository.fetchById(groupId);
    if (userGroup == null) {
      return null;
    }

    List<User> users = userGroup.getUsers();
    users.add(user);

    List<UserGroup> userGroups = new ArrayList<>();
    userGroups.add(userGroup);
    user.setUserGroups(userGroups);

    return groupId;
  }

  /**
   * Creates user attributes from ldap
   *
   * @param user the user the user
   * @param ldap the ldap the ldap
   */
  private void createUserAttributesFromLdap(User user, Map<String, List<String>> ldap) {

    for (Entry<String, String> entry : attributesMap.entrySet()) {
      String aaaAttr = entry.getKey();
      String ldapAttr = entry.getValue();

      UserAttribute attribute = new UserAttribute();
      attribute.setUser(user);
      attribute.setName(aaaAttr);
      attribute.setData(getFirst(ldap, ldapAttr));
      userAttributeRepository.save(attribute);
    }
  }

  /**
   * Updates user from ldap
   *
   * @param user the user
   * @param ldap the ldap
   * @return the user id
   */
  private String updateUserFromLdap(User user, Map<String, List<String>> ldap) {
    String userId = user.getId();

    String groupId = updateGroupFromLdap(user, ldap);
    if (groupId == null) {
      return null;
    }

    updateUserAttributesFromLdap(user, ldap);

    return userId;
  }

  /**
   * Updates the group from ldap
   *
   * @param user the user
   * @param ldap the ldap
   * @return the updated groupId
   */
  private String updateGroupFromLdap(User user, Map<String, List<String>> ldap) {
    UserGroup oldUserGroup = user.getUserGroups().get(0);
    String oldGroupId = oldUserGroup.getId();

    String newGroupId = getFirst(ldap, properties.getMappingGid());
    if (newGroupId == null) {
      return null;
    }

    UserGroup newUserGroup = userGroupRepository.fetchById(newGroupId);
    if (newUserGroup == null) {
      return null;
    }

    if (!newGroupId.equals(oldGroupId)) {
      oldUserGroup.getUsers().remove(user);
      newUserGroup.getUsers().add(user);
      user.getUserGroups().remove(oldUserGroup);
      user.getUserGroups().add(newUserGroup);
    }

    return newGroupId;
  }

  /**
   * Updates the user attributes from ldap
   *
   * @param user the user
   * @param ldap the ldap
   */
  private void updateUserAttributesFromLdap(User user, Map<String, List<String>> ldap) {
    List<UserAttribute> attributes = user.getUserAttributes();

    for (Entry<String, String> entry : attributesMap.entrySet()) {
      String aaaAttr = entry.getKey();
      String ldapAttr = entry.getValue();

      UserAttribute attribute = findByName(attributes, aaaAttr);
      if (attribute == null) {
        continue;
      }

      attribute.setData(getFirst(ldap, ldapAttr));
    }

  }

  /**
   * Finds by name the {@link UserAttribute} object
   *
   * @param attributes a list of attributes
   * @param name the name
   * @return the {@link UserAttribute} object
   */
  private UserAttribute findByName(List<UserAttribute> attributes, String name) {
    for (UserAttribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        return attribute;
      }
    }
    return null;
  }

  /**
   * Retrieves the first value
   *
   * @param ldap the ldap
   * @param key the key
   * @return the first value from ldap provided by the key
   */
  private String getFirst(Map<String, List<String>> ldap, String key) {
    List<String> values = ldap.get(key);
    if (values != null && !values.isEmpty()) {
      return values.get(0);
    } else {
      return null;
    }
  }

  /**
   * This method retrieves all the active users in the LDAP directory based on a search filter and
   * returns their uid.
   *
   * @param searchFilter the filter to search with (ex '(objectClass=*)')
   * @return a set containing the uid of the found users
   */
  public Set<String> retrieveLdapUsers(String searchFilter) {
    Set<String> users = new HashSet<>();
    try {
      Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
      env.put(Context.PROVIDER_URL, properties.getUrl());

      // Create initial context
      DirContext ctx = new InitialDirContext(env);

      String searchBase = properties.getBasedn();
      SearchControls searchCtls = new SearchControls();

      // Specify the search scope
      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      NamingEnumeration<?> namingEnum = ctx.search(searchBase, searchFilter, searchCtls);
      while (namingEnum.hasMore()) {
        SearchResult result = (SearchResult) namingEnum.next();

        Attributes attrs = result.getAttributes();

        users.add(attrs.get("uid") != null ? attrs.get("uid").toString().substring(4).trim() : "");
      }
      namingEnum.close();
      // Close the context when we're done
      ctx.close();
    } catch (NamingException e) {
      log.error("Error while retrieving LDAP users: " + e.toString());
    }

    users.remove("");
    return users;
  }

}
