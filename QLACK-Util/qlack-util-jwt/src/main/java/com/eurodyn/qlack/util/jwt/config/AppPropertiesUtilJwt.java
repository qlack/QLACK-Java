package com.eurodyn.qlack.util.jwt.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Runtime configuration properties for QLACK Util JWT module.
 */
@Data
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "qlack.util.jwt")
public class AppPropertiesUtilJwt {
  // The secret to sign the JWT.
  @Value("${qlack.util.jwt.secret:random.uuid}")
  private String jwtSecret;

  // The number of minutes a JWT is valid for.
  @Value("${qlack.util.jwt.validity:60}")
  private int jwtTtlMinutes;

  // The issuer of the JWT.
  @Value("${qlack.util.jwt.issuer:qlack}")
  private String jwtIssuer;

  // The number of seconds-skew remote and local clocks are allowed.
  @Value("${qlack.util.jwt.clock.skew:60}")
  private long jwtSkewAllowed;
}