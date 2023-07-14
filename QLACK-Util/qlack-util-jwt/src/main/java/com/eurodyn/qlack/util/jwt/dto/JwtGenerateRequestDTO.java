package com.eurodyn.qlack.util.jwt.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.Collection;
import java.util.Map;

/**
 * A class encapsulating all data necessary to create a JWT.
 */
@Getter
@Setter
@Builder
public class JwtGenerateRequestDTO {
  // The subject of this JWT. It can be anything as long as it makes sense to your application.
  // This is, usually, the place where you place the user Id value.
  private @NotEmpty String subject;

  // Claims to be included in the JWT.
  @Singular
  private Map<String, Object> claims;

  // A collection of authorities to be placed within JWT claims section. Authorities are placed
  // under a custom claim name as per AppConstants.JWT_CLAIM_AUTHORITIES.
  @Singular
  private Collection<String> authorities;
}
