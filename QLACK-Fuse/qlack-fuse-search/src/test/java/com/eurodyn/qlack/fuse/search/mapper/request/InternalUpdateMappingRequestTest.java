package com.eurodyn.qlack.fuse.search.mapper.request;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InternalUpdateMappingRequestTest {

  private InternalUpdateMappingRequest internalUpdateMappingRequest;

  @Before
  public void init() {
    internalUpdateMappingRequest = new InternalUpdateMappingRequest();
  }

  @Test
  public void propertiesTest() {
    String properties = "properties";
    internalUpdateMappingRequest.setProperties(properties);
    assertEquals(properties, internalUpdateMappingRequest.getProperties());
  }

}
