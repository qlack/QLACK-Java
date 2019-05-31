package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.VerificationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends AAARepository<VerificationToken, String> {

  @Modifying
  void deleteByExpiresOnBefore(long expiryDate);
}
