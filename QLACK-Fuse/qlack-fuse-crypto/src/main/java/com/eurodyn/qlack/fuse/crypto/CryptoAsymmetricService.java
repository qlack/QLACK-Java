package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
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

@Service
@Validated
public class CryptoAsymmetricService {

  private final String RSA_PUBLIC_KEY = "RSA PUBLIC KEY";
  private final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";

  private String removePEMHeaderFooter(final String key) {
    String regex = "---.*---\\n*";
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(key);

    return matcher.replaceAll("");
  }

  private String convertKeyToPEM(final KeyPair keyPair, final String keyType) throws IOException {
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
   * @param createKeyPairRequest The details of the keypair to create.
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
   * @param keyPair The keypair containing the public key.
   */
  public String publicKeyToPEM(final KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PUBLIC_KEY);
  }

  /**
   * Converts a private key to string in PEM format.
   *
   * @param keyPair The keypair containing the private key to convert.
   */
  public String privateKeyToPEM(final KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PRIVATE_KEY);
  }

  /**
   * Converts a text-based public key (in PEM format) to {@link PublicKey}.
   *
   * @param publicKey The public key in PEM format to convert.
   * @param algorithm The security algorithm with which this key was generated.
   */
  public PublicKey pemToPublicKey(String publicKey, final String algorithm)
  throws NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey key = null;

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
   * @param privateKey The private key in PEM format to convert.
   * @param algorithm The security algorithm with which this key was generated.
   */
  public PrivateKey pemToPrivateKey(String privateKey, final String algorithm)
  throws NoSuchAlgorithmException, InvalidKeySpecException {
    PrivateKey key = null;

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
   * @param privateKeyPEM The private key to sign with in PEM format.
   * @param payload The payload to sign.
   * @param signatureAlgorithm The signature algorithm to use, e.g. SHA256withRSA.
   * @param keyAlgorithm The algorithm with which the private key was generated, e.g. RSA.
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
   * @param privateKeyPEM The private key to sign with in PEM format.
   * @param payload The data to sign.
   * @param signatureAlgorithm The signature algorithm to use, e.g. SHA256withRSA.
   * @param keyAlgorithm The algorithm with which the private key was generated, e.g. RSA.
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
   * @param publicKeyPEM The public key to verify the signature with.
   * @param payload The signed content.
   * @param signature The signature to verify in Base64 format.
   * @param signatureAlgorithm The algorithm with which the signature was created, e.g.
   * SHA256withRSA.
   * @param keyAlgorithm The algorithm with which the key was generated, e.g. RSA.
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
   * @param publicKeyPEM The public key to verify the signature with.
   * @param payload The signed content.
   * @param signature The signature to verify in Base64 format.
   * @param signatureAlgorithm The algorithm with which the signature was created, e.g.
   * SHA256withRSA.
   * @param keyAlgorithm The algorithm with which the key was generated, e.g. RSA.
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
   * @param publicKeyPEM The public key to encrypt with.
   * @param payload The payload to encrypt.
   * @param cipherFactory The factory for the encryption cipher to use, e.g. RSA/ECB/PKCS1Padding.
   * @param keyAlgorithm The algorithm with which the public key was created, e.g. RSA.
   */
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
   * @param privateKeyPEM The private key to decrypt with.
   * @param payload The payload to decrypt.
   * @param cipherFactory The factory for the decryption cipher to use, e.g. RSA/ECB/PKCS1Padding.
   * @param keyAlgorithm The algorithm with which the private key was created, e.g. RSA.
   */
  public byte[] decrypt(final String privateKeyPEM, final byte[] payload,
    final String cipherFactory, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
         InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherFactory);
    cipher.init(Cipher.DECRYPT_MODE, pemToPrivateKey(privateKeyPEM, keyAlgorithm));

    return cipher.doFinal(payload);
  }
}
