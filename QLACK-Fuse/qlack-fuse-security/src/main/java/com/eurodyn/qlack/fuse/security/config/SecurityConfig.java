package com.eurodyn.qlack.fuse.security.config;

import com.eurodyn.qlack.fuse.aaa.service.AuthenticateService;
import com.eurodyn.qlack.fuse.security.access.AAAPermissionEvaluator;
import com.eurodyn.qlack.fuse.security.filters.JwtTokenAuthenticationFilter;
import com.eurodyn.qlack.fuse.security.providers.AAAUsernamePasswordProvider;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * A Configuration class that is used for security reasons.
 *
 * @author European Dynamics SA
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticateService authenticateService;

  @Value("${qlack.fuse.security.authenticated.paths:/qlack/secured/*}")
  private String authenicatedPaths;

  @Value("${qlack.fuse.security.cors.domains:http://localhost}")
  private String corsDomains;

  @Autowired
  public SecurityConfig(AuthenticateService authenticateService) {
    this.authenticateService = authenticateService;
  }

  @Override
  public void configure(WebSecurity web) {
    DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
    handler.setPermissionEvaluator(new AAAPermissionEvaluator());
    web.expressionHandler(handler);
  }

  @Override
  @SuppressWarnings({"squid:S4502", "squid:S4834"})
  protected void configure(HttpSecurity http) throws Exception {

    http.sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and().csrf().disable()
      .addFilterBefore(jwtTokenAuthenticationFilter(),
        UsernamePasswordAuthenticationFilter.class)
      .authorizeRequests()
      .expressionHandler(webExpressionHandler())
      .antMatchers(authenicatedPaths.split(",")).authenticated()
      .anyRequest().permitAll();

    http.logout().permitAll();
    http.logout()
      .logoutSuccessHandler(
        (new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
  }

  /**
   * Enable JWT authentication.
   *
   * @return JwtTokenAuthenticationFilter
   */
  @Bean
  public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
    return new JwtTokenAuthenticationFilter();
  }

  /**
   * Configures AAA operations to be evaluated as spring permissions.
   *
   * @return DefaultWebSecurityExpressionHandler
   */
  @Bean
  public DefaultWebSecurityExpressionHandler webExpressionHandler() {
    DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
    webSecurityExpressionHandler
      .setPermissionEvaluator(new AAAPermissionEvaluator());
    return webSecurityExpressionHandler;
  }

  /**
   * Configures AAA authentication provider with a user service and a password
   * encoder. The AAA user service should be used.
   *
   * @return AAAUsernamePasswordProvider
   */
  @Bean
  public AAAUsernamePasswordProvider authenticationProvider() {
    AAAUsernamePasswordProvider authProvider = new AAAUsernamePasswordProvider();
    authProvider.setUserDetailsService(authenticateService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * Sets bcrypt as the password encoding practice.
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList(corsDomains));
    configuration
      .setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration
      .setAllowedHeaders(
        Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}

