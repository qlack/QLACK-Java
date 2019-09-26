package com.eurodyn.qlack.fuse.search.request;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateIndexRequestTest {

  private CreateIndexRequest createIndexRequest;

  @Before
  public void init() {
    createIndexRequest = new CreateIndexRequest();
  }

  @Test
  public void testName() {
    String name = "name";
    createIndexRequest.setName(name);
    assertEquals(name, createIndexRequest.getName());
  }

  @Test
  public void typeTest() {
    String type = "type";
    createIndexRequest.setType(type);
    assertEquals(type, createIndexRequest.getType());
  }

  @Test
  public void shardsTest() {
    int shards = 5;
    createIndexRequest.setShards(shards);
    assertEquals(shards, createIndexRequest.getShards());
  }

  @Test
  public void replicasTest() {
    int replicas = 5;
    createIndexRequest.setReplicas(replicas);
    assertEquals(replicas, createIndexRequest.getReplicas());
  }

  @Test
  public void indexMappingTest() {
    String indexMapping = "indexMapping";
    createIndexRequest.setIndexMapping(indexMapping);
    assertEquals(indexMapping, createIndexRequest.getIndexMapping());
  }

  @Test
  public void stopWordsTest() {
    List<String> words = Arrays.asList("word1", "word2");
    createIndexRequest.setStopwords(words);
    assertEquals(words, createIndexRequest.getStopwords());
  }

  @Test
  public void addStopWordsTest() {
    createIndexRequest.addStopWords(null);
    createIndexRequest.addStopWords("newWord");
  }

}
