package com.eurodyn.qlack.util.jwt.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class encapsulating all data necessary to create a JWT.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTGenerateRequestDTO {

  /**
   * The secret to be used to sign the JWT.
   */
  private String secret;

  /**
   * The subject of this JWT. It can be anything as long as it makes sense to your application.
   */
  private String subject;

  /**
   * The issuer of this JWT. It can be anything as long as it makes sense to your application.
   */
  private String issuer;

  /**
   * The Id to isse the JWT with.
   */
  private String id;

  /**
   * Claims to be included in the JWT.
   */
  private Map<String, Object> claims = new HashMap<>();

  /**
   * The Time-To-Live (TTL) for the token in milliseconds.
   * This is effectively setting the expiration date for the JWT.
   */
  private long ttl;

  public JWTGenerateRequestDTO(String secret, String subject, long ttl) {
    this.secret = secret;
    this.subject = subject;
    this.ttl = ttl;
  }

  public JWTGenerateRequestDTO(String secret, String subject, Map<String, Object> claims, long ttl) {
    this.secret = secret;
    this.subject = subject;
    this.claims = claims;
    this.ttl = ttl;
  }

}
