package com.eurodyn.qlack.fuse.audit.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuditPropertiesTest {

  @InjectMocks
  private AuditProperties auditProperties;

  @Before
  public void init() {
    auditProperties = new AuditProperties();
  }

  @Test
  public void traceDataTest() {
    auditProperties.setTraceData(true);
    assertEquals(true, auditProperties.isTraceData());
  }

}
