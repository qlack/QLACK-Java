package com.eurodyn.qlack.util.csrf.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class TokenService {

  // Maintain a map to store tokens
  private final Map<String, Date> tokenMap = new ConcurrentHashMap<>();

  /**
   * This method will retrieve the entire map of tokens without altering it
   */
  @Cacheable(value = "tokenCache", key = "'allTokens'")
  public Map<String, Date> getCachedTokens() {
    return Collections.unmodifiableMap(new HashMap<>(tokenMap));
  }

  /**
   * Add or update a token to the cache and return the updated map of tokens
   */
  @CachePut(value = "tokenCache", key = "'allTokens'")
  public Map<String, Date> updateToken(String key, Date date) {
    tokenMap.put(key, date);
    return getCachedTokens();
  }

  /**
   * Remove a token from the cache and return the updated map of tokens
   */
  @CachePut(value = "tokenCache", key = "'allTokens'")
  public Map<String, Date> removeToken(String key) {
    tokenMap.remove(key);
    return getCachedTokens();
  }

  /**
   * Clean cache For custom Csrf Cookies
   */
  @Scheduled(cron = "${qlack.util.csrf.cookie-cache-clean-timer}")
  public void cleanTokens() {
    Date now = new Date(Instant.now().toEpochMilli());
    tokenMap.entrySet().removeIf(entry -> entry.getValue().before(now));
  }

}
