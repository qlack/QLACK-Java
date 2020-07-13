package com.eurodyn.qlack.util.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A placeholder for an encoded JWT.
 */
@Getter
@Setter
@Builder
@ToString
public class JwtDTO {

  private String jwt;

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public JwtDTO() {

  }

  public JwtDTO(String jwt) {
    this.jwt = jwt;
  }

}
