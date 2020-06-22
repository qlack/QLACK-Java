package com.eurodyn.qlack.fuse.aaa.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Runtime properties holder for AAA.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.aaa")
@PropertySource("classpath:qlack.fuse.aaa.application.properties")
public class AAAProperties {

  // Specifies whether a positive permission overrides negative ones.
  private boolean permissionPrioritisePositive;

  // Specifies if LDAP integrated is active or not.
  private boolean ldapEnabled;

  // The URL of the LDAP server to use.
  private String ldapUrl;

  // The baseDN to be used when communicating with the LDAP. This is the basis under which queries
  // and binding takes place.
  private String ldapBasedn;

  // The LDAP attribute denoting the username of a user. This is the value that will be placed in
  // AAA's User model as 'username'.
  private String ldapAttrUsername;

  // The LDAP attribute denoting the group Id of a user. All groups identified in the LDAP will be
  // linked to AAA groups (if they exist).
  private String ldapAttrGroup;

  // The attribute to use when binding to the LDAP. This is to facilitate scenarios where the
  // username you use in AAA does not match the bindDN of the LDAP (which may be
  // using a different one). If this property is empty, ldapAttrUsername is used instead.
  private String ldapBindWith;

  // A comma-separated list of attributes to include when creating a new user from LDAP. All other
  // attributes will be excluded if this value is set.
  private String ldapIncludeAttr = "";
}
