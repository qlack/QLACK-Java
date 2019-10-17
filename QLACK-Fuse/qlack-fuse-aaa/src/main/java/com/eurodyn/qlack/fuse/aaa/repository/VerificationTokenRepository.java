package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.VerificationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * A Repository interface for VerificationToken.It is used to define a number of abstract crud
 * methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface VerificationTokenRepository extends AAARepository<VerificationToken, String> {

  /**
   * Deletion if it is expired
   *
   * @param expiryDate the expiry date
   */
  @Modifying
  void deleteByExpiresOnBefore(long expiryDate);
}
