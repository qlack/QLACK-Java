package com.eurodyn.qlack.fuse.tokenserver;

import com.eurodyn.qlack.fuse.tokenserver.dto.TokenDTO;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class InitTestValues {

  private static final String PAYLOAD = "payload";

  private final Instant now = Instant.now();

  public Token createToken() {
    Token token = new Token();
    token.setCreatedAt(now);
    token.setLastModifiedAt(now);
    token.setPayload(PAYLOAD);
    token.setValidUntil(now.plus(1, ChronoUnit.HOURS));
    return token;
  }

  public TokenDTO createTokenDTO() {
    return TokenDTO.builder()
      .id("id")
      .createdAt(now)
      .lastModifiedAt(now)
      .payload(PAYLOAD)
      .validUntil(now.plus(1, ChronoUnit.HOURS))
      .build();
  }

}
