package com.eurodyn.qlack.fuse.search.mapper.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryResponseTest {

  @InjectMocks
  private QueryResponse queryResponse;

  private QueryResponse.Shards shards;
  private QueryResponse.Hits hits;
  private QueryResponse.Hits.Hit hit;
  private QueryResponse.Aggregations aggregations;
  private QueryResponse.Aggregations.Agg agg;
  private QueryResponse.Aggregations.Agg.Bucket bucket;
  private static final double Epsilon = 1e-15;
  private List<QueryResponse.Hits.Hit> hitList;
  private List<QueryResponse.Aggregations.Agg.Bucket> bucketList;

  @Before
  public void init() {
    queryResponse = new QueryResponse();
    shards = new QueryResponse.Shards();
    hits = new QueryResponse.Hits();
    hit = new QueryResponse.Hits.Hit();
    aggregations = new QueryResponse.Aggregations();
    agg = new QueryResponse.Aggregations.Agg();
    bucket = new QueryResponse.Aggregations.Agg.Bucket();
    queryResponse.setShards(shards);
    queryResponse.setHits(hits);
    queryResponse.setAggregations(aggregations);
    hitList = new ArrayList<>();
    bucketList = new ArrayList<>();
  }

  @Test
  public void tookTest() {
    queryResponse.setTook(40);
    assertEquals(40, queryResponse.getTook());
  }

  @Test
  public void timeOutTest() {
    queryResponse.setTimeOut(true);
    assertTrue(queryResponse.isTimeOut());
  }

  @Test
  public void countTest() {
    queryResponse.setCount(20);
    assertEquals(20, queryResponse.getCount());
  }

  @Test
  public void scrollIdTest() {
    queryResponse.setScrollId("scrollId");
    assertEquals("scrollId", queryResponse.getScrollId());
  }


  @Test
  public void shardsTest() {
    shards.setFailed(20);
    shards.setSuccessful(20);
    shards.setTotal(20);

    QueryResponse.Shards actualShards = queryResponse.getShards();

    assertEquals(shards.getFailed(), actualShards.getFailed());
    assertEquals(shards.getSuccessful(), actualShards.getSuccessful());
    assertEquals(shards.getTotal(), actualShards.getTotal());
  }

  @Test
  public void hitsTest() {
    hits.setTotal(20);
    hits.setMaxScore(20);

    QueryResponse.Hits actualHits = queryResponse.getHits();

    assertEquals(hits.getTotal(), actualHits.getTotal());
    assertEquals(hits.getMaxScore(), actualHits.getMaxScore(), Epsilon);
  }

  @Test
  public void hitsHitListTest() {
    hits.setHits(hitList);
    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hitList.size(), actualHits.getHits().size());

    hits.setHits(null);
    actualHits = queryResponse.getHits();
    assertTrue(actualHits.getHits().isEmpty());
  }


  @Test
  public void hitIndexTest() {
    hit.setIndex("index");
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hit.getIndex(), actualHits.getHits().get(0).getIndex());
  }

  @Test
  public void hitTypeTest() {
    hit.setType("type");
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hit.getType(), actualHits.getHits().get(0).getType());
  }

  @Test
  public void hitIdTest() {
    hit.setId("hitId");
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hit.getId(), actualHits.getHits().get(0).getId());
  }

  @Test
  public void hitScoreTest() {
    hit.setScore(20);
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hit.getScore(), actualHits.getHits().get(0).getScore(), Epsilon);
  }

  @Test
  public void hitSourceTest() {
    hit.setSource("src");
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hit.getSource(), actualHits.getHits().get(0).getSource());
  }

  @Test
  public void hitSourceNullTest() {
    hit.setSource(null);
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertNull(actualHits.getHits().get(0).getSource());
  }

  @Test
  public void hitInnerHitsTest() {
    hit.setInnerHits("innerHit");
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertEquals(hit.getInnerHits(), actualHits.getHits().get(0).getInnerHits());
  }

  @Test
  public void hitInnerHitsNullTest() {
    hit.setInnerHits(null);
    hitList.add(hit);
    hits.setHits(hitList);

    QueryResponse.Hits actualHits = queryResponse.getHits();
    assertNull(actualHits.getHits().get(0).getInnerHits());
  }

  @Test
  public void aggregationsDocCountErrorUpperBoundTest() {
    agg.setDocCountErrorUpperBound(20);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualAggregations = queryResponse.getAggregations();
    assertEquals(agg.getDocCountErrorUpperBound(),
        actualAggregations.getAgg().getDocCountErrorUpperBound());
  }

  @Test
  public void aggregationsSumOtherDocCountTest() {
    agg.setSumOtherDocCount(20);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualAggregations = queryResponse.getAggregations();
    assertEquals(agg.getSumOtherDocCount(), actualAggregations.getAgg().getSumOtherDocCount());
  }

  @Test
  public void aggregationsBucketsTest() {
    agg.setBuckets(bucketList);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualAggregations = queryResponse.getAggregations();
    assertEquals(agg.getBuckets(), actualAggregations.getAgg().getBuckets());
  }

  @Test
  public void aggregationsBucketsNullTest() {
    agg.setBuckets(null);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualAggregations = queryResponse.getAggregations();
    assertTrue(actualAggregations.getAgg().getBuckets().isEmpty());
  }

  @Test
  public void bucketKeyTest() {
    bucket.setKey(20);
    bucketList.add(bucket);
    agg.setBuckets(bucketList);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualBucket = queryResponse.getAggregations();
    assertEquals(bucket.getKey(), actualBucket.getAgg().getBuckets().get(0).getKey());
  }

  @Test
  public void bucketKeyAsStringTest() {
    bucket.setKeyAsString("keyAsString");
    bucketList.add(bucket);
    agg.setBuckets(bucketList);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualBucket = queryResponse.getAggregations();
    assertEquals(bucket.getKeyAsString(),
        actualBucket.getAgg().getBuckets().get(0).getKeyAsString());
  }

  @Test
  public void bucketDocCountTest() {
    bucket.setDocCount(20);
    bucketList.add(bucket);
    agg.setBuckets(bucketList);
    aggregations.setAgg(agg);

    QueryResponse.Aggregations actualBucket = queryResponse.getAggregations();
    assertEquals(bucket.getDocCount(), actualBucket.getAgg().getBuckets().get(0).getDocCount());
  }
}
