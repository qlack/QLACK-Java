package com.eurodyn.qlack.fuse.mailing.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailingPropertiesTest {

  @InjectMocks
  private MailingProperties mailingProperties;

  @BeforeEach
  public void init() {
    mailingProperties = new MailingProperties();
  }

  @Test
  public void pollingTest() {
    mailingProperties.setPolling(true);
    assertTrue(mailingProperties.isPolling());
  }


  @Test
  public void maxTriesTest() {
    byte maxTries = Byte.parseByte("3");
    mailingProperties.setMaxTries(maxTries);
    assertEquals(maxTries, mailingProperties.getMaxTries());
  }

  @Test
  public void debugTest() {
    mailingProperties.setDebug(true);
    assertTrue(mailingProperties.isDebug());
  }

}
