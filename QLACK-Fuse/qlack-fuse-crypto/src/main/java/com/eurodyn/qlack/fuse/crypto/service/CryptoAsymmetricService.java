package com.eurodyn.qlack.fuse.crypto.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CryptoAsymmetricService {

  private static final String RSA_PUBLIC_KEY = "RSA PUBLIC KEY";
  private static final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";

  @SuppressWarnings("squid:S4784")
  private String removePEMHeaderFooter(final String key) {
    String regex = "---.*---\\n*";
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(key);

    return matcher.replaceAll("");
  }

  protected String convertKeyToPEM(final KeyPair keyPair, final String keyType) throws IOException {
    try (StringWriter pemStrWriter = new StringWriter()) {
      try (JcaPEMWriter pemWriter = new JcaPEMWriter(pemStrWriter)) {
        if (keyType.equals(RSA_PRIVATE_KEY)) {
          pemWriter.writeObject(new PemObject(keyType, keyPair.getPrivate().getEncoded()));
        } else if (keyType.equals(RSA_PUBLIC_KEY)) {
          pemWriter.writeObject(new PemObject(keyType, keyPair.getPublic().getEncoded()));
        }
        pemWriter.flush();
        return pemStrWriter.toString();
      }
    }
  }

  /**
   * Generates a new keypair consisting of a public key and a private key.
   *
   * @param createKeyPairRequest The details of the keypair to create
   * @return the generated keypair
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   */
  public KeyPair createKeyPair(final CreateKeyPairDTO createKeyPairRequest)
      throws NoSuchAlgorithmException {
    final KeyPairGenerator keyPairGenerator;

    // Set the provider.
    keyPairGenerator =
        KeyPairGenerator.getInstance(createKeyPairRequest.getKeyPairGeneratorAlgorithm());

    // Set the secret provider and generator.
    keyPairGenerator.initialize(createKeyPairRequest.getKeySize(),
        SecureRandom.getInstance(createKeyPairRequest.getSecureRandomAlgorithm()));

    return keyPairGenerator.generateKeyPair();
  }

  /**
   * Converts a public key to PEM format.
   *
   * @param keyPair The keypair containing the public key
   * @return the generated PEM format
   * @throws IOException thrown when something unexpected happens
   */
  public String publicKeyToPEM(final KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PUBLIC_KEY);
  }

  /**
   * Converts a private key to string in PEM format.
   *
   * @param keyPair the keypair containing the private key to convert
   * @return the generated PEM format
   * @throws IOException thrown when generating PEM
   */
  public String privateKeyToPEM(final KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PRIVATE_KEY);
  }

  /**
   * Converts a text-based public key (in PEM format) to {@link PublicKey}.
   *
   * @param publicKey the public key in PEM format to convert
   * @param algorithm the security algorithm with which this key was generated
   * @return the generated PEM format
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   */
  public PublicKey pemToPublicKey(String publicKey, final String algorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey key;

    // Cleanup the PEM from unwanted text.
    publicKey = removePEMHeaderFooter(publicKey).trim();

    // Read the cleaned up PEM and generate the public key.
    byte[] encoded = Base64.decodeBase64(publicKey);
    final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    final KeyFactory factory = KeyFactory.getInstance(algorithm);
    key = factory.generatePublic(keySpec);

    return key;
  }

  /**
   * Converts a text-based private key (in PEM format) to {@link PrivateKey}.
   *
   * @param privateKey the private key in PEM format to convert
   * @param algorithm the security algorithm with which this key was generated
   * @return the generated PEM format
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   */
  public PrivateKey pemToPrivateKey(String privateKey, final String algorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PrivateKey key;

    // Cleanup the PEM from unwanted text.
    privateKey = removePEMHeaderFooter(privateKey).trim();

    // Read the cleaned up PEM and generate the public key.
    byte[] encoded = Base64.decodeBase64(privateKey);
    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    final KeyFactory factory = KeyFactory.getInstance(algorithm);
    key = factory.generatePrivate(keySpec);

    return key;
  }

  /**
   * Signs a message with a private key.
   *
   * @param privateKeyPEM the private key to sign with in PEM format
   * @param payload the payload to sign
   * @param signatureAlgorithm the signature algorithm to use, e.g. SHA256withRSA
   * @param keyAlgorithm the algorithm with which the private key was generated, e.g. RSA
   * @return the signature in bytes
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   * @throws InvalidKeyException thrown when the provided key is invalid
   * @throws SignatureException thrown when something unexpected occurs during signing
   */
  public byte[] sign(final String privateKeyPEM, final byte[] payload,
      final String signatureAlgorithm, final String keyAlgorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      SignatureException {
    final Signature signatureInstance = Signature.getInstance(signatureAlgorithm);
    signatureInstance.initSign(pemToPrivateKey(privateKeyPEM, keyAlgorithm));
    signatureInstance.update(payload);

    return signatureInstance.sign();
  }

  /**
   * Signs a message using an {@link InputStream}.
   *
   * @param privateKeyPEM the private key to sign with in PEM format
   * @param payload the data to sign
   * @param signatureAlgorithm the signature algorithm to use, e.g. SHA256withRSA
   * @param keyAlgorithm the algorithm with which the private key was generated, e.g. RSA
   * @return the signature in bytes
   * @throws IOException thrown when something unexpected happens
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   * @throws InvalidKeyException thrown when the provided key is invalid
   * @throws SignatureException thrown when something unexpected occurs during signing
   */
  public byte[] sign(final String privateKeyPEM, final InputStream payload,
      final String signatureAlgorithm, String keyAlgorithm)
      throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException,
      InvalidKeyException {
    final Signature signatureInstance = Signature.getInstance(signatureAlgorithm);
    signatureInstance.initSign(pemToPrivateKey(privateKeyPEM, keyAlgorithm));
    try (BufferedInputStream bufin = new BufferedInputStream(payload)) {
      byte[] buffer = new byte[8192];
      int len;
      while ((len = bufin.read(buffer)) >= 0) {
        signatureInstance.update(buffer, 0, len);
      }
    }

    return signatureInstance.sign();
  }

  /**
   * Verifies a signature.
   *
   * @param publicKeyPEM the public key to verify the signature with
   * @param payload the signed content
   * @param signature the signature to verify in Base64 format
   * @param signatureAlgorithm the algorithm with which the signature was created, e.g.
   * SHA256withRSA
   * @param keyAlgorithm the algorithm with which the key was generated, e.g. RSA
   * @return true if the signature is verified, false if it is not
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   * @throws InvalidKeyException thrown when the provided key is invalid
   * @throws SignatureException thrown when something unexpected occurs during signing
   */
  public boolean verifySignature(final String publicKeyPEM, final byte[] payload,
      final String signature, final String signatureAlgorithm, final String keyAlgorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      SignatureException {
    if (StringUtils.isBlank(signature)) {
      throw new QDoesNotExistException("The signature provided to validate is empty.");
    }
    final Signature sign = Signature.getInstance(signatureAlgorithm);
    sign.initVerify(pemToPublicKey(publicKeyPEM, keyAlgorithm));
    sign.update(payload);

    return sign.verify(Base64.decodeBase64(signature));
  }

  /**
   * Verifies a signature using an {@link InputStream}.
   *
   * @param publicKeyPEM the public key to verify the signature with
   * @param payload the signed content
   * @param signature the signature to verify in Base64 format
   * @param signatureAlgorithm the algorithm with which the signature was created, e.g.
   * SHA256withRSA
   * @param keyAlgorithm the algorithm with which the key was generated, e.g. RSA
   * @return true if the signature is verified, false if it is not
   * @throws IOException thrown when something unexpected happens
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   * @throws InvalidKeyException thrown when the provided key is invalid
   * @throws SignatureException thrown when something unexpected occurs during signing
   */
  public boolean verifySignature(final String publicKeyPEM, final InputStream payload,
      final String signature, final String signatureAlgorithm, final String keyAlgorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      SignatureException, IOException {
    if (StringUtils.isBlank(signature)) {
      throw new QDoesNotExistException("The signature provided to validate is empty.");
    }
    final Signature signatureInstance = Signature.getInstance(signatureAlgorithm);
    signatureInstance.initVerify(pemToPublicKey(publicKeyPEM, keyAlgorithm));
    try (BufferedInputStream bufin = new BufferedInputStream(payload)) {
      byte[] buffer = new byte[8192];
      int len;
      while ((len = bufin.read(buffer)) >= 0) {
        signatureInstance.update(buffer, 0, len);
      }
    }

    return signatureInstance.verify(Base64.decodeBase64(signature));
  }

  /**
   * Encrypts a payload.
   *
   * @param publicKeyPEM the public key to encrypt with
   * @param payload the payload to encrypt
   * @param cipherFactory the factory for the encryption cipher to use, e.g. RSA/ECB/PKCS1Padding
   * @param keyAlgorithm the algorithm with which the public key was created, e.g. RSA
   * @return the encrypted key in bytes
   * @throws NoSuchPaddingException thrown when the provided cipherFactory is not valid
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   * @throws InvalidKeyException thrown when the provided key is invalid
   * @throws BadPaddingException thrown when the provided cipherFactory is not valid
   * @throws IllegalBlockSizeException thrown when the provided cipherFactory is not valid
   */
  @SuppressWarnings("squid:S4787")
  public byte[] encrypt(final String publicKeyPEM, final byte[] payload,
      final String cipherFactory, final String keyAlgorithm)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherFactory);
    cipher.init(Cipher.ENCRYPT_MODE, pemToPublicKey(publicKeyPEM, keyAlgorithm));

    return cipher.doFinal(payload);
  }


  /**
   * Decrypts a payload encrypted with a public key using the private key.
   *
   * @param privateKeyPEM the private key to decrypt with
   * @param payload the payload to decrypt
   * @param cipherFactory the factory for the decryption cipher to use, e.g. RSA/ECB/PKCS1Padding
   * @param keyAlgorithm the algorithm with which the private key was created, e.g. RSA
   * @return the decrypted key in bytes
   * @throws NoSuchPaddingException thrown when the provided cipherFactory is not valid
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeySpecException thrown when the provided key is invalid
   * @throws InvalidKeyException thrown when the provided key is invalid
   * @throws BadPaddingException thrown when the provided cipherFactory is not valid
   * @throws IllegalBlockSizeException thrown when the provided cipherFactory is not valid
   */
  @SuppressWarnings("squid:S4787")
  public byte[] decrypt(final String privateKeyPEM, final byte[] payload,
      final String cipherFactory, final String keyAlgorithm)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherFactory);
    cipher.init(Cipher.DECRYPT_MODE, pemToPrivateKey(privateKeyPEM, keyAlgorithm));

    return cipher.doFinal(payload);
  }
}
