package com.eurodyn.qlack.fuse.crypto.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Locale;

@Data
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
  @NotNull
  private String signatureAlgorithm;

  private boolean ca;
}
