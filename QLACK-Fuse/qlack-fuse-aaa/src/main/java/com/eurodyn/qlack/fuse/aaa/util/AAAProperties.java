package com.eurodyn.qlack.fuse.aaa.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The associated Properties class for AAA.
 *
 * @author European Dynamics SA
 */
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.aaa")
@PropertySource("classpath:qlack.fuse.aaa.application.properties")
public class AAAProperties {

  /**
   * the permissionPrioritisePositive
   */
  private boolean permissionPrioritisePositive;
  /**
   * the ldapEnabled
   */
  private boolean ldapEnabled;
  /**
   * the ldapUrl
   */
  private String ldapUrl;
  /**
   * the ldapBasedn
   */
  private String ldapBasedn;
  /**
   * ldapMappingUid
   */
  private String ldapMappingUid;
  /**
   * the ldapMappingGid
   */
  private String ldapMappingGid;
  /**
   * the ldapMappingAttrs
   */
  private String ldapMappingAttrs;

  /**
   * Checks whether the permission priorities is positive or not
   *
   * @return a {@link Boolean} value whether the permissionPrioritisePositive is true or not
   */
  public boolean isPermissionPrioritisePositive() {
    return permissionPrioritisePositive;
  }

  public void setPermissionPrioritisePositive(boolean permissionPrioritisePositive) {
    this.permissionPrioritisePositive = permissionPrioritisePositive;
  }

  /**
   * Checks whether Ldap is enabled or not
   *
   * @return a {@link Boolean} value if ldap is enabled or not
   */
  public boolean isLdapEnabled() {
    return ldapEnabled;
  }

  public void setLdapEnabled(boolean ldapEnabled) {
    this.ldapEnabled = ldapEnabled;
  }

  public String getLdapUrl() {
    return ldapUrl;
  }

  public void setLdapUrl(String ldapUrl) {
    this.ldapUrl = ldapUrl;
  }

  public String getLdapBasedn() {
    return ldapBasedn;
  }

  public void setLdapBasedn(String ldapBasedn) {
    this.ldapBasedn = ldapBasedn;
  }

  public String getLdapMappingUid() {
    return ldapMappingUid;
  }

  public void setLdapMappingUid(String ldapMappingUid) {
    this.ldapMappingUid = ldapMappingUid;
  }

  public String getLdapMappingGid() {
    return ldapMappingGid;
  }

  public void setLdapMappingGid(String ldapMappingGid) {
    this.ldapMappingGid = ldapMappingGid;
  }

  public String getLdapMappingAttrs() {
    return ldapMappingAttrs;
  }

  public void setLdapMappingAttrs(String ldapMappingAttrs) {
    this.ldapMappingAttrs = ldapMappingAttrs;
  }
}
