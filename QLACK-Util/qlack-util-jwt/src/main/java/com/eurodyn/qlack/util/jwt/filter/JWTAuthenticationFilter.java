package com.eurodyn.qlack.util.jwt.filter;


import static java.util.Collections.emptyList;

import com.eurodyn.qlack.util.jwt.JWTUtil;
import com.eurodyn.qlack.util.jwt.dto.JWTClaimsRequestDTO;
import com.eurodyn.qlack.util.jwt.dto.JWTClaimsResponseDTO;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean {

  private String secret;

  public JWTAuthenticationFilter(String secret) {
    this.secret = secret;
  }

  private Authentication getAuthentication(HttpServletRequest request) {
    String jwtToken = JWTUtil.getRawToken(request);
    final JWTClaimsResponseDTO jwtClaimsResponseDTO = JWTUtil
      .getClaims(new JWTClaimsRequestDTO(jwtToken, secret));
    if (jwtClaimsResponseDTO != null && StringUtils
      .isNotBlank(jwtClaimsResponseDTO.getSubject())) {
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
