package com.eurodyn.qlack.fuse.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class CryptoAsymmetricServiceTest {

  CryptoAsymmetricService cryptoAsymmetricService = new CryptoAsymmetricService();

  private KeyPair createKeyPair() throws NoSuchAlgorithmException {
    final CreateKeyPairDTO createKeyPairDTO = new CreateKeyPairDTO();
    createKeyPairDTO.setKeyPairGeneratorAlgorithm("RSA");
    createKeyPairDTO.setKeySize(2048);
    return cryptoAsymmetricService.createKeyPair(createKeyPairDTO);
  }

  @Test
  public void createKeyPairTest() throws NoSuchAlgorithmException {
    // Test with default random.
    final CreateKeyPairDTO createKeyPairDTO = new CreateKeyPairDTO();
    createKeyPairDTO.setKeyPairGeneratorAlgorithm("RSA");
    createKeyPairDTO.setKeySize(2048);
    final KeyPair keyPair = cryptoAsymmetricService.createKeyPair(createKeyPairDTO);
    assertNotNull(keyPair);
    assertNotNull(keyPair.getPrivate());
    assertNotNull(keyPair.getPublic());
  }

  @Test
  public void publicKeyToPEM() throws NoSuchAlgorithmException, IOException {
    final String publicKeyToPEM = cryptoAsymmetricService.publicKeyToPEM(createKeyPair());
    assertNotNull(publicKeyToPEM);
  }

  @Test
  public void privateKeyToPEM() throws NoSuchAlgorithmException, IOException {
    final String privateKeyToPEM = cryptoAsymmetricService.privateKeyToPEM(createKeyPair());
    assertNotNull(privateKeyToPEM);
  }

  @Test
  public void pemToPublicKey()
  throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    final String publicKeyToPEM = cryptoAsymmetricService.publicKeyToPEM(createKeyPair());
    final PublicKey rsa = cryptoAsymmetricService.pemToPublicKey(publicKeyToPEM, "RSA");
    assertNotNull(rsa);
  }

  @Test
  public void pemToPrivateKey()
  throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    final String privateKeyToPEM = cryptoAsymmetricService.privateKeyToPEM(createKeyPair());
    final PrivateKey rsa = cryptoAsymmetricService.pemToPrivateKey(privateKeyToPEM, "RSA");
    assertNotNull(rsa);
  }

  @Test
  public void sign()
  throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException,
         SignatureException {
    final String privateKeyToPEM = cryptoAsymmetricService.privateKeyToPEM(createKeyPair());
    String plainText = "Hello World!";
    final byte[] signature = cryptoAsymmetricService
      .sign(privateKeyToPEM, plainText.getBytes(StandardCharsets.UTF_8), "SHA256withRSA", "RSA");
    assertNotNull(signature);
  }

  @Test
  public void verifySignature()
  throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException,
         SignatureException {
    final KeyPair keyPair = createKeyPair();

    // Generate a signature to compare it later.
    final String privateKeyToPEM = cryptoAsymmetricService.privateKeyToPEM(keyPair);
    byte[] plaintext = "Hello World!".getBytes(StandardCharsets.UTF_8);
    final String signature = Base64.encodeBase64String(cryptoAsymmetricService
      .sign(privateKeyToPEM, plaintext, "SHA256withRSA", "RSA"));

    // Calculate and compare signature.
    final String publicKeyToPEM = cryptoAsymmetricService.publicKeyToPEM(keyPair);
    assertTrue(cryptoAsymmetricService
      .verifySignature(publicKeyToPEM, plaintext, signature, "SHA256withRSA", "RSA"));
  }

  @Test
  public void encrypt()
  throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException,
         BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
    byte[] plaintext = "Hello World!".getBytes(StandardCharsets.UTF_8);
    final KeyPair keyPair = createKeyPair();
    final String publicKeyToPEM = cryptoAsymmetricService.publicKeyToPEM(keyPair);
    assertNotNull(
      cryptoAsymmetricService.encrypt(publicKeyToPEM, plaintext, "RSA/ECB/PKCS1Padding", "RSA"));
  }

  @Test
  public void decrypt()
  throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException,
         BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
    // Encrypt some text to compare it later.
    byte[] plaintext = "Hello World!".getBytes(StandardCharsets.UTF_8);
    final KeyPair keyPair = createKeyPair();
    final String publicKeyToPEM = cryptoAsymmetricService.publicKeyToPEM(keyPair);
    final byte[] ciphertext = cryptoAsymmetricService
      .encrypt(publicKeyToPEM, plaintext, "RSA/ECB/PKCS1Padding", "RSA");

    final String privateKeyToPEM = cryptoAsymmetricService.privateKeyToPEM(keyPair);
    final byte[] plaintextDecrypted = cryptoAsymmetricService
      .decrypt(privateKeyToPEM, ciphertext, "RSA/ECB/PKCS1Padding", "RSA");
    assertNotNull(plaintextDecrypted);

    assertEquals(new String(plaintext, StandardCharsets.UTF_8), new String(plaintextDecrypted,
      StandardCharsets.UTF_8));
  }
}
