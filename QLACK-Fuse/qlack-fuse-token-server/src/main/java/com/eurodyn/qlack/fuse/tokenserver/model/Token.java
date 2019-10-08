package com.eurodyn.qlack.fuse.tokenserver.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ts_token")
public class Token extends QlackBaseModel {

  private static final long serialVersionUID = 1L;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "last_modified_at")
  private Instant lastModifiedAt;

  @Column(name = "revoked")
  private boolean revoked;

  @Column(name = "payload")
  private String payload;

  @Column(name = "valid_until")
  private Instant validUntil;

  @Column(name = "auto_extend_until")
  private Instant autoExtendUntil;

  @Column(name = "auto_extend_duration")
  private Long autoExtendDuration;

  @Column(name = "created_by")
  private String createdBy;
}