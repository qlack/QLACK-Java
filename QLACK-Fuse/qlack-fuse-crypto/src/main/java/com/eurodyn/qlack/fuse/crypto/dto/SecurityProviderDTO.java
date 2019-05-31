package com.eurodyn.qlack.fuse.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of a security provider.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityProviderDTO {

  // The name of the provider.
  private String name;

  // The version of the provider.
  private double version;

  // Any additional info/description of the provider.
  private String info;
}
