package com.eurodyn.qlack.fuse.search.mapper.request;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InternalCreateIndexRequestTest {

  private InternalCreateIndexRequest internalCreateIndexRequest;
  private InternalCreateIndexRequest.Settings settings;
  private InternalCreateIndexRequest.Settings.Index index;
  private InternalCreateIndexRequest.Settings.Analysis analysis;
  private InternalCreateIndexRequest.Settings.Analysis.Filter filter;
  private InternalCreateIndexRequest.Settings.Analysis.Filter.MyStop myStop;

  @Before
  public void init() {
    internalCreateIndexRequest = new InternalCreateIndexRequest();
    settings = new InternalCreateIndexRequest.Settings();
    index = new InternalCreateIndexRequest.Settings.Index();
    analysis = new InternalCreateIndexRequest.Settings.Analysis();
    filter = new InternalCreateIndexRequest.Settings.Analysis.Filter();
    myStop = new InternalCreateIndexRequest.Settings.Analysis.Filter.MyStop();
  }

  @Test
  public void testMappings() {
    String mappings = "mappings";
    internalCreateIndexRequest.setMappings(mappings);
    assertEquals(mappings, internalCreateIndexRequest.getMappings());
  }

  @Test
  public void myStopTypeTest() {
    String type = "type";
    myStop.setType(type);
    assertEquals(type, myStop.getType());
  }

  @Test
  public void myStopWordsTest() {
    List<String> words = Arrays.asList("word1", "word2");
    myStop.setStopwords(words);
    assertEquals(words, myStop.getStopwords());
  }

  @Test
  public void filterMyStopTest() {
    filter.setMyStop(myStop);
    assertEquals(myStop, filter.getMyStop());
  }

  @Test
  public void analysisFilterTest() {
    analysis.setFilter(filter);
    assertEquals(filter, analysis.getFilter());
  }

  @Test
  public void indexShardsTest() {
    String shards = "shards";
    index.setNumberOfShards(shards);
    assertEquals(shards, index.getNumberOfShards());
  }

  @Test
  public void indexReplicasTest() {
    String replicas = "replicas";
    index.setNumberOfReplicas(replicas);
    assertEquals(replicas, index.getNumberOfReplicas());
  }

  @Test
  public void indexTest() {
    settings.setIndex(index);
    assertEquals(index, settings.getIndex());
  }

  @Test
  public void analysisTest() {
    settings.setAnalysis(analysis);
    assertEquals(analysis, settings.getAnalysis());
  }

  @Test
  public void settingsTest() {
    internalCreateIndexRequest.setSettings(settings);
    assertEquals(settings, internalCreateIndexRequest.getSettings());
  }

}
