package com.eurodyn.qlack.fuse.crypto.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * SSL socket factory construction details encapsulation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SSLSocketFactoryDTO {

  // The list of trusted certificates (usually, a CA or a remote peer).
  @Singular
  @NotNull
  @NotEmpty
  private List<SSLSocketFactoryCertificateDTO> trustedCertificates;

  // The client certificate to present during client authenticate.
  @NotNull
  private SSLSocketFactoryCertificateDTO clientCertificate;

  // The client private key to sign during client authenticate.
  @NotNull
  private SSLSocketFactoryPrivateKeyDTO clientPrivateKey;

  // TLS version to use.
  @Builder.Default
  private String tlsVersion = "TLSv1.2";
}