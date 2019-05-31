package com.eurodyn.qlack.fuse.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A request to create a key pair.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateKeyPairDTO {

  // The name of the provider to use while generating the key-pair. If left empty, a provider
  // implementing the requested generatorAlgorithm  will be picked up by the JVM.
  private String generatorProvider;

  // The algorithm to use while generating the key-pair.
  private String generatorAlgorithm;

  // The name of provider to use while initialising the key-pair generator. If left empty,
  // a default SecureRandom.getInstanceStrong() will be used.
  private String secretProvider;

  // The name of algorithm to use while initialising the key-pair generator. If left empty,
  // a default SecureRandom.getInstanceStrong() will be used.
  private String secretAlgorithm;

  // The default bits of the key.
  private int keySize;
}
