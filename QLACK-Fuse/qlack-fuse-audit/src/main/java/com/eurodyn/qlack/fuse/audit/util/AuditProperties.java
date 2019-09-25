package com.eurodyn.qlack.fuse.audit.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties class for the qlack-fuse-audit module
 */
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.audit")
@Setter
@Getter
public class AuditProperties {

  /**
   * Whether trace data should be stored
   */
  private boolean traceData;
}
