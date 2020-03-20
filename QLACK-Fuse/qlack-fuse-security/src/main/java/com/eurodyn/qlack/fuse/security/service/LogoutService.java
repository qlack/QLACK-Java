package com.eurodyn.qlack.fuse.security.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.util.CachedUserUtil;
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

  private UserService userService;

  private CachedUserUtil cachedUserUtil;

  @Value("${qlack.fuse.security.jwt.secret:aqlacksecret}")
  private String jwtSecret;

  /**
   * Default expiration set at 24 hours.
   */
  @Value("${qlack.fuse.security.jwt.expiration:86400000}")
  private int jwtExpiration;

  @Autowired
  public LogoutService(UserService userService, CachedUserUtil cachedUserUtil) {
    this.userService = userService;
    this.cachedUserUtil = cachedUserUtil;
  }

  public void performLogout(HttpServletRequest req) {
    String username = String.valueOf(JWTUtil
        .getSubject(new JWTClaimsRequestDTO(JWTUtil.getRawToken(req), jwtSecret,
            jwtExpiration)));

    UserDTO user = userService.getUserByName(username);

    if (user == null) {
      throw new QDoesNotExistException("User doesn't exist.");
    }

    // Logout from session.
    if (user.getSessionId() != null) {
      userService.logout(user.getId(), user.getSessionId());
    }

    cachedUserUtil.removeUserFromCache(username);
  }
}
