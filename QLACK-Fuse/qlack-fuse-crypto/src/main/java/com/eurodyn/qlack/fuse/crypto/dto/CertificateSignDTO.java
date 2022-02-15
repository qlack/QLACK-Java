package com.eurodyn.qlack.fuse.crypto.dto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CertificateSignDTO {

  @NotNull
  private PrivateKey privateKey;
  @NotNull
  private PublicKey publicKey;
  @NotNull
  private String issuerCN;
  @NotNull
  private PrivateKey issuerPrivateKey;
  @NotNull
  private Instant validForm;
  @NotNull
  private Instant validTo;
  @NotNull
  private Locale locale;
  @NotNull
  private String subjectCN;
  // A list of SANs that will be added to the certificate as a Subject Alternative Names of type DNS.
  private List<String> sans;
  @NotNull
  private String signatureAlgorithm;

  private boolean ca;
}
