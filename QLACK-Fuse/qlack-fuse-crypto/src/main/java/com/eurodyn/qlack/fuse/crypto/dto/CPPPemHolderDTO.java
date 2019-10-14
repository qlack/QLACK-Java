package com.eurodyn.qlack.fuse.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An encapsulation of a key comprising of a public key, a private key and a certificate in PEM
 * format.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CPPPemHolderDTO {

  // The certificate of the key.
  private String certificate;

  // The public key of the key.
  private String publicKey;

  // The private key of the key.
  private String privateKey;

}
