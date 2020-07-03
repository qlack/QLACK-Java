package com.eurodyn.qlack.fuse.aaa.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AAAPropertiesTest {

  private AAAProperties aaaProperties;
  private boolean isTrue;
  private boolean isFalse;
  private String ldapTest;

  @Before
  public void init() {
    aaaProperties = new AAAProperties();
    isTrue = true;
    isFalse = false;
    ldapTest = "testString";
  }

  @Test
  public void isPermissionPrioritisePositiveTest() {
    aaaProperties.setPermissionPrioritisePositive(isTrue);
    assertEquals(isTrue, aaaProperties.isPermissionPrioritisePositive());

    aaaProperties.setPermissionPrioritisePositive(isFalse);
    assertFalse(aaaProperties.isPermissionPrioritisePositive());
  }

  @Test
  public void isLdapEnabledTest() {
    aaaProperties.setLdapEnabled(isTrue);
    assertEquals(isTrue, aaaProperties.isLdapEnabled());

    aaaProperties.setLdapEnabled(isFalse);
    assertFalse(aaaProperties.isLdapEnabled());
  }

  @Test
  public void getLdapUrlTest() {
    aaaProperties.setLdapUrl(null);
    assertNull(aaaProperties.getLdapUrl());

    aaaProperties.setLdapUrl(ldapTest);
    assertEquals(ldapTest, aaaProperties.getLdapUrl());
  }

  @Test
  public void setLdapUrlTest() {
    aaaProperties.setLdapBasedn(null);
    assertNull(aaaProperties.getLdapBasedn());

    aaaProperties.setLdapBasedn(ldapTest);
    assertEquals(ldapTest, aaaProperties.getLdapBasedn());
  }

  @Test
  public void getLdapAttrUsernameTest() {
    aaaProperties.setLdapAttrUsername(null);
    assertNull(aaaProperties.getLdapAttrUsername());

    aaaProperties.setLdapAttrUsername(ldapTest);
    assertEquals(ldapTest, aaaProperties.getLdapAttrUsername());
  }

  @Test
  public void getLdapAttrGroupTest() {
    aaaProperties.setLdapAttrGroup(null);
    assertNull(aaaProperties.getLdapAttrGroup());

    aaaProperties.setLdapAttrGroup(ldapTest);
    assertEquals(ldapTest, aaaProperties.getLdapAttrGroup());
  }

}
