package com.eurodyn.qlack.fuse.tokenserver.service;

import com.eurodyn.qlack.fuse.tokenserver.dto.TokenDTO;
import com.eurodyn.qlack.fuse.tokenserver.exception.QTokenRevokedException;
import com.eurodyn.qlack.fuse.tokenserver.mapper.TokenMapper;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import com.eurodyn.qlack.fuse.tokenserver.repository.TokenRepository;
import com.querydsl.core.types.Predicate;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@Transactional
public class TokenServerService {

  private final TokenMapper mapper;

  private final TokenRepository repository;

  @Value("${qlack.fuse.tokenserver.enableCleanup:false}")
  private boolean enableCleanup;
  @Value("${qlack.fuse.tokenserver.cleanupExpired:false}")
  private boolean cleanupExpired;
  @Value("${qlack.fuse.tokenserver.cleanupRevoked:false}")
  private boolean cleanupRevoked;

  @Autowired
  public TokenServerService(TokenMapper mapper, TokenRepository repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  public String createToken(TokenDTO tokenDTO) {
    Token token = mapper.mapToEntity(tokenDTO);
    repository.save(token);
    return token.getId();
  }

  public void deleteById(String tokenId) {
    repository.deleteById(tokenId);
  }

  public void deleteTokens(Collection<String> tokenIds) {
    for (String tokenID : tokenIds) {
      deleteById(tokenID);
    }
  }

  public boolean isValid(String tokenId) {
    boolean retVal = false;
    Token token = repository.fetchById(tokenId);
    if (!token.isRevoked()
      && ((token.getValidUntil() == null) || Instant.now()
      .isBefore(token.getValidUntil()))) {
      retVal = true;
    }

    if (retVal && token.getValidUntil() != null
      && token.getAutoExtendDuration() != null
      && token.getAutoExtendDuration() > 0) {
      Instant now = Instant.now();
      Instant newValidUntil;
      if (token.getAutoExtendUntil() == null
        || now.plusMillis(token.getAutoExtendDuration()).isBefore(token
        .getAutoExtendUntil())) {
        newValidUntil = now.plusMillis(token.getAutoExtendDuration());
      } else {
        newValidUntil = token.getAutoExtendUntil();
      }
      token.setValidUntil(newValidUntil);
    }

    return retVal;
  }

  public Instant getValidUntil(String tokenId) {
    Token token = repository.fetchById(tokenId);
    return token.getValidUntil();
  }

  public void revoke(String tokenId) {
    Token token = repository.fetchById(tokenId);
    if (token.isRevoked()) {
      throw new QTokenRevokedException(MessageFormat.format(
        "Cannot revoke token with ID {0}; the token is already "
          + "revoked", tokenId));
    }
    token.setRevoked(true);
    token.setLastModifiedAt(Instant.now());
  }

  public void revoke(Collection<String> tokenIds) {
    for (String tokenID : tokenIds) {
      revoke(tokenID);
    }
  }

  public void extendValidity(String tokenId, Instant validUntil) {
    Token token = repository.fetchById(tokenId);
    if (token.isRevoked()) {
      throw new QTokenRevokedException(MessageFormat.format(
        "Cannot extend the validity of token with ID "
          + "{0}; the token is revoked.", tokenId));
    }
    token.setValidUntil(validUntil);
    token.setLastModifiedAt(Instant.now());
  }

  public void extendAutoExtendValidity(String tokenId,
    Instant autoExtendUntil) {
    Token token = repository.fetchById(tokenId);
    if (token.isRevoked()) {
      throw new QTokenRevokedException(MessageFormat.format(
        "Cannot extend the auto extend date of token with ID {0} "
          + "; the token is revoked.",
        tokenId));
    }
    token.setAutoExtendUntil(autoExtendUntil);
    token.setLastModifiedAt(Instant.now());
  }

  public TokenDTO findById(String tokenId) {
    return mapper.mapToDTO(repository.fetchById(tokenId));
  }

  public List<TokenDTO> findTokens(Predicate predicate) {
    List<Token> all = repository.findAll(predicate);
    return mapper.mapToDTO(all);
  }

  public void cleanupExpired() {
    repository.deleteAllByValidUntilIsBefore(Instant.now());
  }

  public void cleanupRevoked() {
    repository.deleteAllByRevokedIsTrue();
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void checkAndSendQueued() {
    if (enableCleanup) {
      if (cleanupExpired) {
        cleanupExpired();
        log.info("Clean up of expired tokens finished successfully.");
      }
      if (cleanupRevoked) {
        cleanupRevoked();
        log.info("Clean up of revoked tokens finished successfully.");
      }
    }
  }
}
