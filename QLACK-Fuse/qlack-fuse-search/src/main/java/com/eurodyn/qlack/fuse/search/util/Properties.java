package com.eurodyn.qlack.fuse.search.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.search")
public class Properties {

  private String esHosts;
  private String esUsername;
  private String esPassword;
  private boolean verifyHostname;
}
