package com.eurodyn.qlack.fuse.tokenserver.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {

  private String id;
  private Instant createdAt;
  private Instant lastModifiedAt;
  private boolean revoked;
  private String payload;
  private Instant validUntil;
  private Instant autoExtendUntil;
  private Long autoExtendDuration;
  private String createdBy;
}