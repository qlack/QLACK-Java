package com.eurodyn.qlack.fuse.security.filters;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.security.exception.QInvalidNonceException;
import com.eurodyn.qlack.fuse.security.service.CachingUserDetailsService;
import com.eurodyn.qlack.fuse.security.service.NonceCachingService;
import com.eurodyn.qlack.util.jwt.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

/**
 * An implementation of a filter that runs at least once in every request. It checks if there is a
 * header that contains a JWT. If exists
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Log
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  @Value("${qlack.fuse.security.jwt.secret:qlackjwtsecret}")
  private String jwtSecret;

  /**
   * Defines the difference between the application server clock and the client's browser clock, if
   * any. Value 120, means that the JWT will be active for 120 seconds more than its expiration
   * date.
   *
   * Default value is 0.
   */
  @Value("${qlack.fuse.security.jwt.clocks.margin:0}")
  private int jwtClocksMargin;

  @Autowired
  private CachingUserDetailsService userDetailsService;

  @Autowired(required = false)
  private NonceCachingService nonceCachingService;

  @Autowired
  private AbstractUserDetailsAuthenticationProvider authenticationProvider;

  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

  /**
   * A flag that indicates if a nonce value will be required for requests.
   *
   * @see <a href="https://en.wikipedia.org/wiki/Cryptographic_nonce">Cryptographic nonce</a>
   *
   * Another possible solution would to be create an implementation of the JTI value included in the
   * JWT spec.
   * @see <a href="https://tools.ietf.org/html/rfc7519#section-4.1.7">JWT ID (JTI)</a>
   */
  private boolean requireNonce = false;

  private static final String NONCE_HEADER = "Nonce";

  private static final int UUID_LENGTH = 36;

  @Override
  public void afterPropertiesSet() {
    Assert.notNull(authenticationProvider, "An AuthenticationProvider is required");

    if (requireNonce) {
      Assert.notNull(nonceCachingService, "Nonce caching must be enabled for requireNonce = true.");
    }
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    // Get the token without the prefix.
    String token = JWTUtil.getRawToken(request);

    // If the token doesn't exist continue with the next filter.
    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // Validate the token.
      Claims claims = Jwts.parser()
          .setSigningKey(jwtSecret.getBytes())
          .setAllowedClockSkewSeconds(jwtClocksMargin)
          .parseClaimsJws(token)
          .getBody();

      String username = claims.getSubject();

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // Try to retrieve the user from database of cache.
        UserDetailsDTO userDetails = (UserDetailsDTO) userDetailsService
            .loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            userDetails.getPassword(),
            userDetails.getAuthorities());
        authentication.setDetails(authenticationDetailsSource.buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (requireNonce) {
          handleNonce(request, authentication);
        }
      }
    } catch (Exception e) {
      log.log(Level.WARNING,
          "JWT token verification failed for address {0} to [{1}] with message {2}",
          new Object[]{request.getRemoteAddr(), request.getPathInfo(), e.getMessage()});

      // In case of failure make sure to clear the context to guarantee the user won't be authenticated.
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }

  public boolean isRequireNonce() {
    return requireNonce;
  }

  public void setRequireNonce(boolean requireNonce) {
    this.requireNonce = requireNonce;
  }

  /**
   * Handles nonce functionality.
   *
   * The current implementation requires a mandatory request header (or parameter) that should be
   * <strong>unique for every request per user</strong>. It uses the configured cache
   * implementation
   * to blacklist nonce values by user and creates a different cache for each user.
   *
   * The nonce value is expected to be in this format: N = n || h(n || p), where: N = nonce n =
   * client generated random UUID h = hash function (e.g. SHA-256) p = hashed user password
   * retrieved from cache or database || = the concatenation symbol
   *
   * @param request HTTP request to acquire the nonce value (from header or parameter)
   * @param authentication Authentication object with user details
   */
  protected void handleNonce(HttpServletRequest request, Authentication authentication) {
    String username = authentication.getPrincipal().toString();
    String nonce = request.getHeader(NONCE_HEADER);

    if (StringUtils.isEmpty(nonce)) {
      throw new QInvalidNonceException("No nonce parameter included in the request.");
    }

    String cacheValue = nonceCachingService.getValueForUser(username, nonce, String.class);

    // If a nonce already exists for user, then reject the request by throwing an exception.
    if (cacheValue != null) {
      throw new QInvalidNonceException("Request rejected for address " + request.getRemoteAddr() +
          ". Nonce was already used for user " + authentication.getPrincipal());
    }

    // Parse nonce string and extract random value and hash.
    String uuid = nonce.substring(0, UUID_LENGTH);
    String requestHash = nonce.substring(UUID_LENGTH);

    String calculatedHash = DigestUtils.sha256Hex(uuid + authentication.getCredentials());

    // Check if the request hash matches the calculated hash.
    if (!requestHash.equals(calculatedHash)) {
      throw new QInvalidNonceException("Request rejected for address " + request.getRemoteAddr() +
          ". Nonce was invalid for user " + authentication.getPrincipal());
    }

    // Currently the value is not useful, so put the date in it.
    nonceCachingService.putForUser(username, nonce, new Date().toString());
  }

}
