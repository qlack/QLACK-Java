package com.eurodyn.qlack.fuse.aaa.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * LDAP Configuration class
 *
 * @author European Dynamics SA.
 */
@Getter
@Configuration
public class LdapProperties {

  @Value("${qlack.fuse.aaa.ldap.enabled:false}")
  private boolean enabled;
  @Value("${qlack.fuse.aaa.ldap.url:}")
  private String url;
  @Value("${qlack.fuse.aaa.ldap.basedn:}")
  private String basedn;
  @Value("${qlack.fuse.aaa.ldap.mapping.uid:uid}")
  private String mappingUid;
  @Value("${qlack.fuse.aaa.ldap.mapping.gid:cn}")
  private String mappingGid;
  @Value("${qlack.fuse.aaa.ldap.admin.uid:}")
  private String adminUid;
  @Value("${qlack.fuse.aaa.ldap.admin.password:}")
  private String adminPassword;
}
