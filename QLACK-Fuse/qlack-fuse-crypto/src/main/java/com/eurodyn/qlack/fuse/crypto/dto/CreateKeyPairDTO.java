package com.eurodyn.qlack.fuse.crypto.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.logging.Logger;

/**
 * A request to create a key pair.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateKeyPairDTO {

  // JUL reference.
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private static final Logger LOGGER = Logger.getLogger(CreateKeyPairDTO.class.getName());

  // The algorithm to use while generating the key-pair, e.g. RSA.
  @NotNull
  private String keyPairGeneratorAlgorithm;

  // The name of algorithm to use while initialising the key-pair generator, e.g. NativePRNG.
  // If you leave this field empty, a system default strong random algorithm will be chosen via
  // SecureRandom.getInstanceStrong().
  private String secureRandomAlgorithm;

  // The default bits of the key, e.g. 2048.
  @NotNull
  private int keySize;
}
