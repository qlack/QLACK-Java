package com.eurodyn.qlack.fuse.search.request;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseRequestTest {

  private BaseRequest baseRequest;

  @Before
  public void init(){
    baseRequest = new CreateIndexRequest();
  }

  @Test
  public void asyncTest(){
    baseRequest.setAsync(true);
    assertEquals(true, baseRequest.isAsync());
  }

}
