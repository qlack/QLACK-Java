package com.eurodyn.qlack.fuse.security.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import com.eurodyn.qlack.util.jwt.JWTUtil;
import com.eurodyn.qlack.util.jwt.dto.JWTClaimsRequestDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class that is used to for Log out.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class LogoutService {

  @Autowired
  private UserService userService;

  @Autowired
  private AAAUserCaching userCaching;

  @Autowired
  private NonceCachingService nonceCachingService;

  @Value("${qlack.fuse.security.jwt.secret:aqlacksecret}")
  private String jwtSecret;

  /**
   * Default expiration set at 24 hours.
   */
  @Value("${qlack.fuse.security.jwt.expiration:86400000}")
  private int jwtExpiration;

  public void performLogout(HttpServletRequest req) {
    String username = String.valueOf(JWTUtil
        .getSubject(new JWTClaimsRequestDTO(JWTUtil.getRawToken(req), jwtSecret, jwtExpiration)));

    UserDTO user = userService.getUserByName(username);

    if (user == null) {
      throw new QDoesNotExistException("User doesn't exist.");
    }

    // Remove user from cache.
    userCaching.getUserCache().removeUserFromCache(user.getUsername());

    // Clear nonce cache if exists.
    nonceCachingService.clear(user.getUsername());

    // Logout from session.
    if (user.getSessionId() != null) {
      userService.logout(user.getId(), user.getSessionId());
    }
  }
}
