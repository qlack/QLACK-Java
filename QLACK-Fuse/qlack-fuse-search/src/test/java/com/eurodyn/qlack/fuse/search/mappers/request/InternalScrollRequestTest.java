package com.eurodyn.qlack.fuse.search.mappers.request;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InternalScrollRequestTest {

  private InternalScrollRequest internalScrollRequest;

  @Before
  public void init() {
    internalScrollRequest = new InternalScrollRequest();
  }

  @Test
  public void scrollTest() {
    String scroll = "scroll";
    internalScrollRequest.setScroll(scroll);
    assertEquals(scroll, internalScrollRequest.getScroll());
  }

  @Test
  public void scrollIdTest() {
    String scrollId = "scrollId";
    internalScrollRequest.setScrollId(scrollId);
    assertEquals(scrollId, internalScrollRequest.getScrollId());
  }

}
