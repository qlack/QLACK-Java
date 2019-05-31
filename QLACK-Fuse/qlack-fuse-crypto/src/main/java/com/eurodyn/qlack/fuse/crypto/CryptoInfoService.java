package com.eurodyn.qlack.fuse.crypto;

import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.AES;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.AES_WEAK_LENGTH;

import com.eurodyn.qlack.fuse.crypto.dto.SecurityProviderDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SecurityServiceDTO;
import javax.crypto.Cipher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A provider of information regarding the security algorithms and services available in the runtime.
 */
@Service
@Validated
public class CryptoInfoService {

  /**
   * Returns the security providers available in the runtime.
   *
   * @return a list containing the security providers
   */
  public List<SecurityProviderDTO> getSecurityProviders() {
    return Arrays.stream(Security.getProviders())
        .flatMap(o -> Arrays.asList(new SecurityProviderDTO(o.getName(), o.getVersion(), o.getInfo()))
            .stream()
        ).collect(Collectors.toList());
  }

  /**
   * Returns the security services provided by a specific provider available in the runtime.
   *
   * @param providerName The provider to inquiry for available services.
   *
   * @return a list containing the security providers
   */
  public List<SecurityServiceDTO> getSecurityServices(String providerName) {
    return getSecurityProviders().stream()
        .filter(o -> o.getName().equals(providerName))
        .flatMap(o -> Arrays.asList(Security.getProvider(o.getName()).getServices()).stream())
        .flatMap(o -> o.stream().flatMap(
            x -> Arrays.asList(new SecurityServiceDTO(providerName, x.getAlgorithm(), x.getType()))
                .stream()))
        .collect(Collectors.toList());
  }

  /**
   * Returns the security services providing a specific security algorithm.
   *
   * @param algorithmType The algorithm type to find services providing it.
   *
   * @return a list containing the security providers
   */
  public List<SecurityServiceDTO> getSecurityServicesForAlgorithmType(String algorithmType) {
    return getSecurityProviders().stream()
        .flatMap(o -> Arrays.asList(o.getName()).stream())
        .flatMap(o -> getSecurityServices(o).stream())
        .filter(o -> o.getType().equals(algorithmType))
        .collect(Collectors.toList());
  }

  /**
   * Returns all available security algorithms available in the runtime.
   *
   * @return a list containing the types
   */
  public List<String> getAlgorithmTypes() {
    return getSecurityProviders().stream()
        .flatMap(o -> getSecurityServices(o.getName()).stream())
        .flatMap(o -> Arrays.asList(o.getType()).stream())
        .distinct()
        .sorted()
        .collect(Collectors.toList());
  }

  /**
   * Pretty prints a list of services.
   *
   * @param services The list of services to prety print.
   *
   * @return the string buffer
   */
  public String prettyPrint(List<SecurityServiceDTO> services) {
    StringBuffer s = new StringBuffer();
    services.stream().sorted((o1, o2) -> (o1.getType() + o1.getAlgorithm()).compareTo(o2.getType() + o2.getAlgorithm()))
        .forEach(service -> s.append(
            "Provider: " + service.getProvider() + ", Algorithm: " + service.getAlgorithm() + ", Type: " + service
                .getType() + "\n"));

    return s.toString();
  }

  /**
   * Checks if unlimited strength cryptography is available. The check is taking place by comparing the maximum size
   * available of an AES key. If this method returns false, you can enable unlimited strength cryptography by
   * downloading https://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html (or in JDK greater than 8 by
   * setting Security.setProperty("crypto.policy", "unlimited")).
   *
   * return the boolean value
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   */
  public boolean isUnlimitedStrengthActive() throws NoSuchAlgorithmException {
    return Cipher.getMaxAllowedKeyLength(AES) > AES_WEAK_LENGTH;
  }
}
