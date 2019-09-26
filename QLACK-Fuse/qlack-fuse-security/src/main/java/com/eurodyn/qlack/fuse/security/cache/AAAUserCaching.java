package com.eurodyn.qlack.fuse.security.cache;

import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.stereotype.Component;

/**
 * Creates a default user cache based on
 * the system's configured caching mechanism.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Component
public class AAAUserCaching implements InitializingBean {

    @Autowired(required = false)
    private CacheManager cacheManager;

    private String cacheName = "users";

    /**
     * Initialize with an empty implementation (cache does nothing).
     */
    private UserCache userCache = new NullUserCache();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (cacheManager != null) {
            this.userCache = new SpringCacheBasedUserCache(Objects.requireNonNull(cacheManager.getCache(cacheName)));
        }
    }

    /** the user cache
     * @return the user cache
     */
    public UserCache getUserCache() {
        return userCache;
    }

    /** the cache name
     * @return the cache name
     */
    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

}
