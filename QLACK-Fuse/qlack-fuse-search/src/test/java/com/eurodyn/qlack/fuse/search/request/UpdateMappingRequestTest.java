package com.eurodyn.qlack.fuse.search.request;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateMappingRequestTest {

  private UpdateMappingRequest updateMappingRequest;

  @Before
  public void init() {
    updateMappingRequest = new UpdateMappingRequest();
  }

  @Test
  public void indexNameTest() {
    String indexName = "indexName";
    updateMappingRequest.setIndexName(indexName);
    assertEquals(indexName, updateMappingRequest.getIndexName());
  }

  @Test
  public void typeNameTest() {
    String typeName = "typeName";
    updateMappingRequest.setTypeName(typeName);
    assertEquals(typeName, updateMappingRequest.getTypeName());
  }

  @Test
  public void indexMappingTest() {
    String indexMapping = "indexMapping";
    updateMappingRequest.setIndexMapping(indexMapping);
    assertEquals(indexMapping, updateMappingRequest.getIndexMapping());
  }
}
