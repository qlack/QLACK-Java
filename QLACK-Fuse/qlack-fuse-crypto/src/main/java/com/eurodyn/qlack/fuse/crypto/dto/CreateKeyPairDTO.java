package com.eurodyn.qlack.fuse.crypto.dto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * A request to create a key pair.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

  public String getSecureRandomAlgorithm() {
    if (StringUtils.isBlank(secureRandomAlgorithm)) {
      try {
        return SecureRandom.getInstanceStrong().getAlgorithm();
      } catch (NoSuchAlgorithmException e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
        return secureRandomAlgorithm;
      }
    } else {
      return secureRandomAlgorithm;
    }
  }
}
