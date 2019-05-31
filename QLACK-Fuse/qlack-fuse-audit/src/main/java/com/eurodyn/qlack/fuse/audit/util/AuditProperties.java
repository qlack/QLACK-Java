package com.eurodyn.qlack.fuse.audit.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.audit")
@Setter
@Getter
public class AuditProperties {

  private boolean traceData;
}
