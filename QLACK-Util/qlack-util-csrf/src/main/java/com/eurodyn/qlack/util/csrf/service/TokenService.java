package com.eurodyn.qlack.util.csrf.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class TokenService {

  // Maintain a map to store tokens
  private final Map<String, Date> tokenMap = new HashMap<>();

  /**
   * This method will retrieve the entire map of tokens without altering it
   */
  @Cacheable(value = "tokenCache", key = "'allTokens'")
  public Map<String, Date> getCachedTokens() {
    return Collections.unmodifiableMap(tokenMap);
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
    Map<String, Date> getAllTokens = getCachedTokens();
    if (!CollectionUtils.isEmpty(getAllTokens)) {
      Date now = new Date(Instant.now().toEpochMilli());
      List<String> removeKeyList = getAllTokens.entrySet().stream()
          .filter(entry -> entry.getValue().before(now))
          .map(Map.Entry::getKey).toList();
      if (!CollectionUtils.isEmpty(removeKeyList)) {
        removeKeyList.forEach(this::removeToken);
      }
    }
  }

}
