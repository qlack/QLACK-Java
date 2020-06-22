package com.eurodyn.qlack.util.jwt.filter;

import static java.util.Collections.emptyList;

import com.eurodyn.qlack.util.jwt.dto.JwtClaimsDTO;
import com.eurodyn.qlack.util.jwt.service.JwtService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * An authentication filter to automatically extract a JWT from incoming requests and validate it.
 * When authentication is successful, the filter also updates Spring's {@link
 * SecurityContextHolder}.
 */
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  /**
   * Extracts the filter from the HTTP requests.
   *
   * @param request The request to extract JWT from.
   */
  private Authentication getAuthentication(HttpServletRequest request) {
    String jwtToken = JwtService.getRawToken(request);
    final JwtClaimsDTO jwtClaimsResponseDTO = jwtService.getClaims(jwtToken);
    if (jwtClaimsResponseDTO != null && StringUtils.isNotBlank(jwtClaimsResponseDTO.getSubject())) {
      return new UsernamePasswordAuthenticationToken(
          jwtClaimsResponseDTO.getSubject(), jwtToken,
          emptyList());
    } else {
      return null;
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain)
  throws IOException, ServletException {
    Authentication authentication = getAuthentication(
        (HttpServletRequest) request);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
