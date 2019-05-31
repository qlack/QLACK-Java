package com.eurodyn.qlack.util.jwt.dto;

/**
 * A class encapsulating all data necessary to verify a JWT.
 */
public class JWTClaimsRequestDTO extends JWTTokenDTO {

  /**
   * The secret used to sign the JWT. Use only Latin-1/ISO-8859-1 characters.
   */
  private String secret;

  /**
   * The amount of seconds local and remote clocks can drift
   * to still consider the expiration of the JWT valid.
   */
  private long allowedTimeSkew = 60;

  public JWTClaimsRequestDTO(String jwt, String secret) {
    super();
    this.setJwt(jwt);
    this.secret = secret;
  }

  public JWTClaimsRequestDTO(String jwt, String secret, long allowedTimeSkew) {
    super();
    this.setJwt(jwt);
    this.secret = secret;
    this.allowedTimeSkew = allowedTimeSkew;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public long getAllowedTimeSkew() {
    return allowedTimeSkew;
  }

  public void setAllowedTimeSkew(long allowedTimeSkew) {
    this.allowedTimeSkew = allowedTimeSkew;
  }
}
