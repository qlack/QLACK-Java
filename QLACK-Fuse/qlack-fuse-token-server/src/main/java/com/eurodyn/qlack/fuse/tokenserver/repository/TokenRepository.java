package com.eurodyn.qlack.fuse.tokenserver.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface TokenRepository extends QlackBaseRepository<Token, String> {

  void deleteAllByValidUntilIsBefore(Instant now);
  void deleteAllByRevokedIsTrue();

}