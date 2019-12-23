package com.eurodyn.qlack.fuse.security.service;

import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A service responsible for user management in the configured system cache.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Service
@Validated
@Transactional
@Qualifier("cachingUserDetailsService")
public class CachingUserDetailsService implements UserDetailsService {

  private final AAAUserCaching userCaching;

  private final UserDetailsService delegate;

  @Autowired
  public CachingUserDetailsService(AAAUserCaching userCaching,
    UserDetailsService delegate) {
    this.userCaching = userCaching;
    this.delegate = delegate;
  }

  /**
   * Removes user from cache by username. If the user doesn't exist it returns
   * without throwing an exception.
   *
   * @param username User name to be evicted from cache
   */
  public void removeUser(String username) {
    getUserCache().removeUserFromCache(username);
  }

  /**
   * Returns the cache implementation used for caching the user details.
   *
   * @return User cache
   */
  public UserCache getUserCache() {
    return userCaching.getUserCache();
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserCache userCache = userCaching.getUserCache();

    UserDetails user = userCache.getUserFromCache(username);

    if (user == null) {
      user = delegate.loadUserByUsername(username);
    }

    if (user != null) {
      userCache.putUserInCache(user);
    }

    return user;
  }
}
