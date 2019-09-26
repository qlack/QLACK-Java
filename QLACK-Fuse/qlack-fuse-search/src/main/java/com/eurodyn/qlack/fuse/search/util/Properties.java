package com.eurodyn.qlack.fuse.search.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties class for qlack-fuse-search module.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.search")
public class Properties {

  /**
   * The elastic search hosts separated by comma (,)
   */
  private String esHosts;

  /**
   * Username
   */
  private String esUsername;

  /**
   * Password
   */
  private String esPassword;

  /**
   * Whether to verify the provided hostname(s)
   */
  private boolean verifyHostname;
}
