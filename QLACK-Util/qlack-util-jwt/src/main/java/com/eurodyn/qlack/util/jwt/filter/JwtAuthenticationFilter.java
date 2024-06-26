package com.eurodyn.qlack.util.jwt.filter;

import com.eurodyn.qlack.util.jwt.config.AppConstants;
import com.eurodyn.qlack.util.jwt.dto.JwtClaimsDTO;
import com.eurodyn.qlack.util.jwt.service.JwtService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * An authentication filter to automatically extract a JWT from incoming requests and validate it.
 * When authentication is successful, the filter also updates Spring's {@link
 * SecurityContextHolder}.
 */
@Log
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  /**
   * Extracts the authorities claim from a JWT.
   *
   * @param jwtClaimsDTO The JWT claims to extract authorities from.
   * @return Returns the collection of authorities found or an empty collection.
   */
  private Collection<GrantedAuthority> getAuthorities(JwtClaimsDTO jwtClaimsDTO) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    final Object authorityClaims = jwtClaimsDTO.getClaims().get(AppConstants.JWT_CLAIM_AUTHORITIES);
    if (authorityClaims != null) {
      Arrays.asList(((String) authorityClaims).split(",")).stream().forEach(
          authority -> authorities.add(new SimpleGrantedAuthority(authority))
      );
    }

    return authorities;
  }

  /**
   * Extracts the JWT from the HTTP request.
   *
   * @param request The request to extract the JWT from.
   * @return An {@link Authentication} object or null if the JWT could not be extracted and
   * validated.
   */
  private Authentication getAuthentication(HttpServletRequest request) {
    // Get JWT token.
    String jwtToken = JwtService.getRawToken(request);

    // Get JWT claims and validate token.
    final JwtClaimsDTO jwtClaimsDTO = jwtService.getClaims(jwtToken);

    // Create the Authentication response.
    if (jwtClaimsDTO != null && StringUtils.isNotBlank(jwtClaimsDTO.getSubject())) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

      // Populate authorities if available.
      usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          jwtClaimsDTO.getSubject(), jwtToken, getAuthorities(jwtClaimsDTO));

      return usernamePasswordAuthenticationToken;
    } else {
      return null;
    }
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.finest("Applying filter JwtAuthenticationFilter.");
    Authentication authentication = getAuthentication((HttpServletRequest) request);
    if (authentication != null) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Do not automatically register this filter.
   *
   * @param filter The filter to configure.
   */
  @Bean(name = "JwtAuthenticationFilterRegistrationBean")
  public FilterRegistrationBean registration(JwtAuthenticationFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean(filter);
    registration.setEnabled(false);
    return registration;
  }

}
