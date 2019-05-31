package com.eurodyn.qlack.fuse.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * An encapsulation of a key comprising of a public key, a private key and a certificate.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CPPHolderDTO {

  // The certificate of the key.
  private Certificate certificate;

  // The public key of the key.
  private PublicKey publicKey;

  // The private key of the key.
  private PrivateKey privateKey;

}
