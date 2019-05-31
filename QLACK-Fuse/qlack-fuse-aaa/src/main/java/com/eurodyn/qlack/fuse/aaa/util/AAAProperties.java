package com.eurodyn.qlack.fuse.aaa.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.aaa")
@PropertySource("classpath:qlack.fuse.aaa.application.properties")
public class AAAProperties {

  private boolean permissionPrioritisePositive;
  private boolean ldapEnabled;
  private String ldapUrl;
  private String ldapBasedn;
  private String ldapMappingUid;
  private String ldapMappingGid;
  private String ldapMappingAttrs;

  public boolean isPermissionPrioritisePositive() {
    return permissionPrioritisePositive;
  }

  public void setPermissionPrioritisePositive(boolean permissionPrioritisePositive) {
    this.permissionPrioritisePositive = permissionPrioritisePositive;
  }

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
