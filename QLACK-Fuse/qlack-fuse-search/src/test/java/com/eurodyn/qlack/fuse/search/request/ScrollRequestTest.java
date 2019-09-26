package com.eurodyn.qlack.fuse.search.request;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScrollRequestTest {

  private ScrollRequest scrollRequest;

  @Before
  public void init() {
    scrollRequest = new ScrollRequest();
  }

  @Test
  public void scrollTest() {
    Integer scroll = 10;
    scrollRequest.setScroll(scroll);
    assertEquals(scroll, scrollRequest.getScroll());
  }

  @Test
  public void scrollIdTest() {
    String scrollId = "scrollId";
    scrollRequest.setScrollId(scrollId);
    assertEquals(scrollId, scrollRequest.getScrollId());
  }

}
