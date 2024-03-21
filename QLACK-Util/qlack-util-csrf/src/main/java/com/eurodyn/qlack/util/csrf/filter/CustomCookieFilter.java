package com.eurodyn.qlack.util.csrf.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.eurodyn.qlack.util.csrf.service.TokenService;
import com.eurodyn.qlack.util.jwt.config.AppPropertiesUtilJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A custom filter where create unique token and validate per request
 */
@Component
public final class CustomCookieFilter extends OncePerRequestFilter {

  @Value("${qlack.util.csrf.cookie-name}")
  private String cookieName;

  @Value("${qlack.util.csrf.cookie-timer}")
  private int cookieTimer;

  @Value("${qlack.util.csrf.login-path}")
  private String loginPath;

  @Value("${qlack.util.csrf.logout-path}")
  private String logoutPath;

  private final TokenService tokenService;
  private final AppPropertiesUtilJwt appProperties;

  public CustomCookieFilter(TokenService tokenService, AppPropertiesUtilJwt appProperties) {
    this.tokenService = tokenService;
    this.appProperties = appProperties;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    //if the path it is the logout ignore filter
    if(request.getServletPath().equals(logoutPath)){
      response.setStatus(200);
      filterChain.doFilter(request, response);
      return;
    }
    Date tokenTime = new Date(Instant.now().plus(appProperties.getJwtTtlMinutes(),
        ChronoUnit.MINUTES).toEpochMilli());
    //during the login generate the token for first time
    if (loginPath.equals(request.getServletPath())) {
      putNewTokenToCookie(response, tokenTime);
    } else {
      String clientToken = extractTokenFromCookie(request);
      boolean invalidToken = invalidToken(clientToken);
      Date jwtTokenTime = extractTimeFromJwtToken(request, tokenTime);
      if (jwtTokenTime.before(new Date(Instant.now().toEpochMilli())) || invalidToken) {
        tokenService.removeToken(clientToken);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
        return;
      }
      Map<String, Date> getTokens = tokenService.getCachedTokens();
      long jwtTimeDiffSeconds =
              (jwtTokenTime.getTime() - Date.from(Instant.now()).getTime()) / 1000;
      boolean createNewToken = false;
      synchronized (this) {
        Date tokenExpirationTime = getTokens.get(clientToken);
        long tokenTimeDiffSeconds =
                (tokenExpirationTime.getTime() - Date.from(Instant.now()).getTime()) / 1000;
        tokenService.updateToken(clientToken,
                new Date(Instant.now().plus(cookieTimer, ChronoUnit.SECONDS).toEpochMilli()));
        //in case that the token timer it is higher than cookie timer, we recreate a token
        //we do that for do Not generate tokens every time we have many requests in short of period
        createNewToken = (tokenTimeDiffSeconds > cookieTimer || jwtTimeDiffSeconds < cookieTimer);
      }
      if (createNewToken) {
        putNewTokenToCookie(response, tokenTime);
      }
    }
    filterChain.doFilter(request, response);
  }

  private void putNewTokenToCookie(HttpServletResponse response, Date tokenTime) {
    String generateToken = generateRandomToken();
    Cookie cookie = new Cookie(cookieName, generateToken);
    cookie.setPath("/");
    response.addCookie(cookie);
    tokenService.updateToken(generateToken, tokenTime);
  }

  private Date extractTimeFromJwtToken(HttpServletRequest request, Date tokenTime) {
    String userAuthorizationHeaderJwt = request.getHeader(AUTHORIZATION);
    //get remaining time off jwt token
    if (Objects.nonNull(userAuthorizationHeaderJwt)) {
      String jwt = userAuthorizationHeaderJwt.substring(7);
      byte[] apiKeySecretBytes2 = appProperties.getJwtSecret().getBytes();
      Claims claims = Jwts.parser()
          .setSigningKey(apiKeySecretBytes2)
          .parseClaimsJws(jwt).getBody();
      tokenTime = claims.getExpiration();
    }
    return tokenTime;
  }

  private boolean invalidToken(String key){
    Map<String, Date> getTokens = tokenService.getCachedTokens();
    Date tokenTime = getTokens.get(key);
    return Objects.isNull(key) || CollectionUtils.isEmpty(getTokens)
        || Objects.isNull(tokenTime)
        || tokenTime.before(new Date(Instant.now().toEpochMilli()));
  }

  private String extractTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookieName.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  private String generateRandomToken() {
    return UUID.randomUUID().toString();
  }
}
