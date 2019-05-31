package com.eurodyn.qlack.util.jwt.dto;

/**
 * A placeholder for a JWT.
 */
public class JwtDTO {
  // The JWT to include.
  private String jwt;

  // A helper flag to indicate that 2FA is required.
  private boolean requires2FA;

  public JwtDTO(String jwt) {
    this.jwt = jwt;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public boolean isRequires2FA() {
    return requires2FA;
  }

  public void setRequires2FA(boolean requires2FA) {
    this.requires2FA = requires2FA;
  }
}
