package com.eurodyn.qlack.fuse.audit.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditPropertiesTest {

  @InjectMocks
  private AuditProperties auditProperties;

  @BeforeEach
  public void init() {
    auditProperties = new AuditProperties();
  }

  @Test
  public void traceDataTest() {
    auditProperties.setTraceData(true);
    assertTrue(auditProperties.isTraceData());
  }

}
