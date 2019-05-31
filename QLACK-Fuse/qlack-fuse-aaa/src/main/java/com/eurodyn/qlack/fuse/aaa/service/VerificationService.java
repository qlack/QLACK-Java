package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.model.VerificationToken;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
@Transactional
public class VerificationService {

  // Service references.
  private VerificationTokenRepository verificationTokenRepository;
  private UserRepository userRepository;

  @Autowired
  public VerificationService(
      VerificationTokenRepository verificationTokenRepository,
      UserRepository userRepository) {
    this.verificationTokenRepository = verificationTokenRepository;
    this.userRepository = userRepository;
  }

  public String createVerificationToken(String userId, long expiresOn) {
    return createVerificationToken(userId, expiresOn, null);
  }

  public String createVerificationToken(String userId, long expiresOn, String data) {
    VerificationToken vt = new VerificationToken();
    vt.setUser(userRepository.findById(userId).get());
    vt.setCreatedOn(Instant.now().toEpochMilli());
    if (data != null) {
      vt.setData(data);
    }
    vt.setExpiresOn(expiresOn);
    vt.setId(UUID.randomUUID().toString());
    verificationTokenRepository.save(vt);

    return vt.getId();
  }

  public String verifyToken(String tokenID) {
    String userId = null;
    Optional<VerificationToken> ovt = verificationTokenRepository.findById(tokenID);
    if (ovt.isPresent()) {
      if (ovt.get().getExpiresOn() >= Instant.now().toEpochMilli()) {
        userId = ovt.get().getUser().getId();
      }
    }

    return userId;
  }

  public void deleteExpired() {
    verificationTokenRepository.deleteByExpiresOnBefore(new Date().getTime());
  }

  public void deleteToken(String tokenID) {
    verificationTokenRepository.deleteById(tokenID);
  }

  public String getTokenPayload(String tokenID) {
    Optional<VerificationToken> ovt = verificationTokenRepository.findById(tokenID);
    if (ovt.isPresent()) {
      return ovt.get().getData();
    } else {
      return null;
    }
  }

  public String getTokenUser(String tokenID) {
    String userId = null;

    Optional<VerificationToken> ovt = verificationTokenRepository.findById(tokenID);
    if (ovt.isPresent()) {
      userId = ovt.get().getUser().getId();
    }

    return userId;
  }

}
