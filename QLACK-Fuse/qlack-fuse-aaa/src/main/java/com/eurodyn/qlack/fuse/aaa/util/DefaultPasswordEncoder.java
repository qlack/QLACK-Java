package com.eurodyn.qlack.fuse.aaa.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A default implementation of a {@link PasswordEncoder} based on AAA's legacy MD5 encoder. It is
 * suggested to switch to a better implementation in your project, such as {@link
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}.
 */
@Component
public class DefaultPasswordEncoder {

  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(DefaultPasswordEncoder.class.getName());

  @Bean
  public PasswordEncoder md5PasswordEncoder() {
    LOGGER.log(Level.CONFIG, "Initialising {0} as password encoder.", this.getClass().toString());
    return new Md5PasswordEncoder();
  }
}
