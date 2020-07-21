package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.common.exception.QMismatchException;
import com.eurodyn.qlack.fuse.aaa.mapper.UserAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.util.AAAProperties;
import com.eurodyn.qlack.fuse.aaa.util.LDAPAttributeHandler;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Provides methods to access and communicate with an LDAP server. This class also provides a generic querying mechanism
 * for LDAP as well as the capability to create AAA users based on LDAP users.
 */
@Log
@Getter
@Setter
@Service
@Validated
@Transactional
public class LdapService {

  private static final String CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
  private static final String EMPTY_STRING = "";
  private final UserRepository userRepository;
  private final UserGroupRepository userGroupRepository;
  private final UserAttributeRepository userAttributeRepository;
  private Map<String, String> attributesMap;
  private final AAAProperties aaaProperties;
  private final UserGroupService userGroupService;
  private final UserAttributeMapper userAttributeMapper;
  private final Optional<LDAPAttributeHandler> ldapAttributeHandler;

  public LdapService(
      UserRepository userRepository,
      UserGroupRepository userGroupRepository,
      UserAttributeRepository userAttributeRepository,
      AAAProperties aaaProperties,
      UserGroupService userGroupService,
      UserAttributeMapper userAttributeMapper,
      Optional<LDAPAttributeHandler> ldapAttributeHandler) {
    this.userRepository = userRepository;
    this.userGroupRepository = userGroupRepository;
    this.userAttributeRepository = userAttributeRepository;
    this.aaaProperties = aaaProperties;
    this.userGroupService = userGroupService;
    this.userAttributeMapper = userAttributeMapper;
    this.ldapAttributeHandler = ldapAttributeHandler;
  }

  /**
   * Prevent LDAP injection by escaping as per http://www.faqs.org/rfcs/rfc2254.html.
   *
   * @param filter The string to escape.
   * @return Returns an LDAP-safe string.
   */
  private static final String escapeLDAPSearchFilter(String filter) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < filter.length(); i++) {
      char curChar = filter.charAt(i);
      switch (curChar) {
        case '\\':
          sb.append("\\5c");
          break;
        case '*':
          sb.append("\\2a");
          break;
        case '(':
          sb.append("\\28");
          break;
        case ')':
          sb.append("\\29");
          break;
        case '\u0000':
          sb.append("\\00");
          break;
        default:
          sb.append(curChar);
      }
    }
    return sb.toString();
  }

  /**
   * Checks whether a set of credentials can authenticate against the underlying LDAP server. Authentication takes place
   * via an LDAP bind operation.
   *
   * @param username The username to bind with.
   * @param password The password to bind with.
   */
  @SuppressWarnings("squid:S1149")
  private void authenticate(String username, String password) throws NamingException {
    // Prepare an LDAP context for the user details passed.
    Hashtable<String, String> env = new Hashtable<>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
    env.put(Context.PROVIDER_URL, aaaProperties.getLdapUrl());
    if (StringUtils.isEmpty(aaaProperties.getLdapBindWith())) {
      env.put(Context.SECURITY_PRINCIPAL,
          aaaProperties.getLdapAttrUsername() + "=" + username + "," + aaaProperties
              .getLdapBasedn());
    } else {
      final Set<MultiValuedMap<String, Object>> users = search(escapeLDAPSearchFilter(
          "(" + aaaProperties.getLdapAttrUsername() + "=" + username + ")"));
      if (users.size() == 1) {
        env.put(Context.SECURITY_PRINCIPAL,
            escapeLDAPSearchFilter(aaaProperties.getLdapBindWith() + "=" + users.iterator().next()
                .get(aaaProperties.getLdapBindWith()).iterator().next() + "," + aaaProperties
                .getLdapBasedn()));
      } else {
        throw new QDoesNotExistException("2-step LDAP binding failed for user {0} as the "
            + "user could not be found in the LDAP under attribute {1}.", username,
            aaaProperties.getLdapBindWith());
      }
    }
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_CREDENTIALS, password);

    new InitialDirContext(env);
  }

  /**
   * Creates a AAA user using the LDAP user and attributes for the given username. This method requires an LDAP server
   * allowing public querying. This method expects the user to already exist in LDAP (as it is used internally by
   * canAuthenticate which already guarantees this).
   *
   * @param username The username of the user to create. This argument is matched against ldapAttrUsername in LDAP.
   * @return Returns the AAA user Id of the created (or updated) user.
   */
  private String createUserFromLdap(String username) throws NamingException {
    // Check if the user already exists in AAA.
    User aaaUser = userRepository.findByUsername(username);

    // Find the user in LDAP.
    String searchFilter = String
        .join("", "(", aaaProperties.getLdapAttrUsername(), "=", username, ")");
    final Set<MultiValuedMap<String, Object>> ldapUsers = search(searchFilter);

    // If the user doesn't exist in AAA, create a new user.
    if (aaaUser == null) {
      if (ldapUsers.size() == 1) {
        // Create the AAA user object.
        final MultiValuedMap<String, Object> ldapUser = ldapUsers.iterator().next();
        User user = new User();
        user.setUsername(
            ldapUser.get(aaaProperties.getLdapAttrUsername()).iterator().next().toString());
        user.setStatus((byte) 1);
        user.setSuperadmin(false);
        user.setExternal(true);
        aaaUser = userRepository.save(user);
      } else {
        throw new QMismatchException("More than 1 users were found for {0}.", username);
      }
    }

    // Create or update user's attributes and group membership.
    // Note that AAA does not currently support multivalued attributes, so only the first
    // key when multiple ones exist is copied.
    final MultiValuedMap<String, Object> ldapUser = ldapUsers.iterator().next();
    for (Iterator<String> attributesIterator = ldapUser.keys().uniqueSet().iterator();
        attributesIterator.hasNext(); ) {
      String key = attributesIterator.next();
      if (key.equals(aaaProperties.getLdapAttrGroup())) {
        // LDAP groups handling.
        String groupName = String.valueOf(ldapUser.get(key).iterator().next());
        if (userGroupRepository.findByName(groupName) != null) {
          userGroupService
              .addUserByGroupName(aaaUser.getId(), groupName);
        }
      } else {
        // LDAP attributes handling.
        if (StringUtils.isEmpty(aaaProperties.getLdapIncludeAttr()) ||
            (Arrays.asList(aaaProperties.getLdapIncludeAttr().split(",")).contains(key))) {
          Object attributeValue = ldapUser.get(key).iterator().next();
          // If a custom LDAP attributes handler is present, use it for key and value handling.
          if (ldapAttributeHandler.isPresent()) {
            String newKey = ldapAttributeHandler.get().handleAttributeName(key, attributeValue);
            Object newAttributeValue = ldapAttributeHandler.get().handleAttributeValue(key, attributeValue);
            key = newKey;
            attributeValue = newAttributeValue;
          }
          UserAttribute attribute = userAttributeRepository
              .findByUserIdAndName(aaaUser.getId(), key);
          if (attribute == null) {
            attribute = new UserAttribute();
            attribute.setName(key);
            attribute.setUser(aaaUser);
            attribute.setData(attributeValue.toString());
          } else {
            if (!attribute.getData().equals(attributeValue)) {
              attribute.setData(attributeValue.toString());
            }
          }
          userAttributeRepository.save(attribute);
        }
      }
    }

    return aaaUser.getId();
  }

  /**
   * Check if the user can be authenticated with LDAP using 'simple' authentication (bind operation).
   *
   * @param username The LDAP username of the user. This will be matched against qlack.fuse.aaa.ldap_mapping_uid or
   * qlack.fuse.aaa.ldap_bind_attr if set.
   * @param password The LDAP password of the user.
   * @return The AAA ID of the user if authenticated, null otherwise.
   */
  public String canAuthenticate(String username, String password) {
    String userId = null;

    try {
      // Try to authenticate the user by binding to the LDAP.
      authenticate(username, password);

      // If LDAP binding was successful, create a user in AAA.
      userId = createUserFromLdap(username);
    } catch (NamingException e) {
      log.fine(MessageFormat
          .format("Could not bind user {0} to LDAP: {1}", username, e.getMessage()));
    }

    return userId;
  }


  /**
   * A generic LDAP search implementation. Make sure that your `searchfilter` is properly sanitised.
   *
   * @param searchFilter The filter to search with (ex '(objectClass=*)')
   * @return Returns a set of all entries matched the filter together with all their LDAP attributes.
   */
  public Set<MultiValuedMap<String, Object>> search(String searchFilter)
      throws NamingException {
    DirContext ctx = null;
    Set<MultiValuedMap<String, Object>> users = new HashSet<>();

    try {
      Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, CTX_FACTORY);
      env.put(Context.PROVIDER_URL, aaaProperties.getLdapUrl());

      // Create initial context
      ctx = new InitialDirContext(env);

      String searchBase = aaaProperties.getLdapBasedn();
      SearchControls searchCtls = new SearchControls();

      // Specify the search scope
      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      // Iterate the results and compile the return answer.
      for (NamingEnumeration<SearchResult> searchResults = ctx
          .search(searchBase, searchFilter, searchCtls); searchResults.hasMore(); ) {
        Attributes attrs = searchResults.next().getAttributes();

        MultiValuedMap<String, Object> nextUser = new HashSetValuedHashMap<>();
        for (NamingEnumeration<? extends Attribute> attrsEnum = attrs.getAll();
            attrsEnum.hasMore(); ) {
          final Attribute attr = attrsEnum.next();
          for (NamingEnumeration<?> attrEnum = attr.getAll(); attrEnum.hasMore(); ) {
            nextUser.put(attr.getID(), attrEnum.nextElement());
          }
        }
        users.add(nextUser);
      }
    } catch (NamingException e) {
      log.severe(MessageFormat.format("Error while retrieving LDAP users: {0}.", e.toString()));
    } finally {
      if (ctx != null) {
        ctx.close();
      }
    }

    return users;
  }

  public void sync() throws NamingException {
    // Try to recreate all existing/registered users.
    for (User user : userRepository.findByExternalTrue()) {
      createUserFromLdap(user.getUsername());
    }
  }

}
