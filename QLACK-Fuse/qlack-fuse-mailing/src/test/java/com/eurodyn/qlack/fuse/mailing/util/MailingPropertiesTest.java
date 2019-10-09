package com.eurodyn.qlack.fuse.mailing.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MailingPropertiesTest {

  @InjectMocks
  private MailingProperties mailingProperties;

  @Before
  public void init() {
    mailingProperties = new MailingProperties();
  }

  @Test
  public void pollingTest() {
    mailingProperties.setPolling(true);
    assertEquals(true, mailingProperties.isPolling());
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
    assertEquals(true, mailingProperties.isDebug());
  }

}
