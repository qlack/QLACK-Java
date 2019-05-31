package com.eurodyn.qlack.fuse.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.Optional;

/**
 * A service that manage the nonce cache operations.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Service
@Validated
@Transactional
public class NonceCachingService {

    private CacheManager cacheManager;

    private static final String NONCE_CACHE_PREFIX = "nonce-";

    @Autowired
    public NonceCachingService(Optional<CacheManager> cacheManager) {
        cacheManager.ifPresent(cm -> this.cacheManager = cm);
    }

    /**
     * Returns a value stored to a user cache for a given nonce key.
     *
     * Creates the user cache if it doesn't exist.
     *
     * @param username Authenticated user name
     * @param nonce Nonce value to lookup
     * @param type Value type class
     * @param <T> Value type
     * @return Value mapped to nonce key or null if key doesn't exist
     */
    public <T> T getValueForUser(String username, String nonce, Class<T> type) {
        if (cacheManager == null) {
            return null;
        }

        return Objects.requireNonNull(cacheManager.getCache(NONCE_CACHE_PREFIX + username)).get(nonce, type);
    }

    /**
     * Puts a new pair nonce, value to a user cache.
     *
     * Creates the user cache if it doesn't exist.
     *
     * @param username Authenticated user name
     * @param nonce Nonce key
     * @param value Value that should be mapped to the nonce key
     */
    public void putForUser(String username, String nonce, Object value) {
        if (cacheManager == null) {
            return;
        }

        Objects.requireNonNull(cacheManager.getCache(NONCE_CACHE_PREFIX + username)).put(nonce, value);
    }

    /**
     * Clears a user cache from all mappings.
     *
     * @param username Authenticated user name
     */
    public void clear(String username) {
        if (cacheManager == null) {
            return;
        }

        Optional.ofNullable(cacheManager.getCache(NONCE_CACHE_PREFIX + username)).ifPresent(Cache::clear);
    }

}
