package com.eurodyn.qlack.util.clamav.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author European Dynamics
 */
@Getter
@Setter
@Configuration
public class ClamAvProperties {
  @Value("${qlack.util.clamav.host:localhost}")
  private String clamAvHost;
  @Value("${qlack.util.clamav.port:3310}")
  private int clamAvPort;
  @Value("${qlack.util.clamav.socket.timeout:100000}")
  private int clamAvSocketTimeout;
}
