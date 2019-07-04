package com.eurodyn.qlack.fuse.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of a security service.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityServiceDTO {

  // The name of the provider for this service.
  private String provider;

  // The name of the algorithm for this service.
  private String algorithm;

  // The type of the service.
  private String type;

  public String getAlgorithm() {
    return algorithm;
  }

}
