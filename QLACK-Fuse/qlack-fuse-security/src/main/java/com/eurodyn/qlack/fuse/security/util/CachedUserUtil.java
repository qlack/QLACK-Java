package com.eurodyn.qlack.fuse.security.util;

import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import com.eurodyn.qlack.fuse.security.service.CachingUserDetailsService;
import com.eurodyn.qlack.fuse.security.service.NonceCachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A utility class that provides methods related to caching.
 *
 * @author European Dynamics SA
 */
@Component
public class CachedUserUtil {

  private AAAUserCaching userCaching;

  private NonceCachingService nonceCachingService;

  private CachingUserDetailsService cachingUserDetailsService;

  @Autowired
  public CachedUserUtil(AAAUserCaching userCaching,
      NonceCachingService nonceCachingService,
      CachingUserDetailsService cachingUserDetailsService) {
    this.userCaching = userCaching;
    this.nonceCachingService = nonceCachingService;
    this.cachingUserDetailsService = cachingUserDetailsService;
  }

  /**
   * This method removes the user and the user from the cache.
   *
   * @param username the username of the user to be removed from cache
   */
  public void removeUserFromCache(String username) {
    // Remove user from cache.
    userCaching.getUserCache().removeUserFromCache(username);

    // Clear nonce cache if exists.
    nonceCachingService.clear(username);

    cachingUserDetailsService.removeUser(username);
  }
}
