package com.eurodyn.qlack.util.jwt.dto;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;

/**
 * Encapsulates a reply from a verification request. It contains an indicator of whether the JWT
 * was found to be valid, together with an error description in case the token was invalid.
 */
public class JWTClaimsResponseDTO {
  // Indicates whether this JWT successfully passed verification or not.
  private boolean valid;

  // The error message resulted during an unsuccessful verification.
  private String errorMessage;

  // The claims found on the JWT. This map contains all standard claims as well as custom claims
  // placed into the original JWT. Standard claims are named after their RFC-defined names
  // (https://tools.ietf.org/html/rfc7519), however this class also provide helper getters to
  // access them (e.g. getIssuer(), getIssuedAt(), etc.
  private Map<String, Object> claims;

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Map<String, Object> getClaims() {
    return claims;
  }

  public void setClaims(Map<String, Object> claims) {
    this.claims = claims;
  }

  public String getIssuer() {
    return claims != null ? (String)claims.get(Claims.ISSUER) : null;
  }

  public String getId() {
    return claims != null ? (String)claims.get(Claims.ID) : null;
  }

  public Date getIssuedAt() {
    return claims != null ? new Date((int)claims.get(Claims.ISSUED_AT) * 1000l) : null;
  }

  public Date getExpiresAt() {
    return claims != null ? new Date((int)claims.get(Claims.EXPIRATION) * 1000l) : null;
  }

  public String getSubject() {
    return claims != null ? (String)claims.get(Claims.SUBJECT) : null;
  }

}