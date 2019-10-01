package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean.BooleanType;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySort;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySpec;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryString;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryStringSpecField;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryStringSpecFieldNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTerm;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTermNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTerms;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTermsNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryWildcard;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryWildcardNested;
import com.eurodyn.qlack.fuse.search.dto.queries.SimpleQueryString;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Aggregations;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Aggregations.Agg;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Aggregations.Agg.Bucket;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Hits;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Hits.Hit;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Shards;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.ScrollRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitTestValues {

  public CreateIndexRequest createIndexRequest() {
    CreateIndexRequest createIndexRequest = new CreateIndexRequest();
    createIndexRequest.setName("name");
    createIndexRequest.setType("type");
    createIndexRequest.setIndexMapping("indexMapping");
    createIndexRequest.setStopwords(Arrays.asList("word1"));

    return createIndexRequest;
  }

  public UpdateMappingRequest updateMappingRequest() {
    UpdateMappingRequest updateMappingRequest = new UpdateMappingRequest();
    updateMappingRequest.setIndexName("indexName");
    updateMappingRequest.setTypeName("typeName");
    updateMappingRequest.setIndexMapping("indexMappings");
    updateMappingRequest.setAsync(true);

    return updateMappingRequest;
  }

  public IndexingDTO createIndexingDTO() {
    IndexingDTO indexingDTO = new IndexingDTO();
    indexingDTO.setIndex("indexName");
    indexingDTO.setType("typeName");
    indexingDTO.setId("id");
    indexingDTO.setSourceObject(new SerializableClass("id_obj"));
    indexingDTO.setConvertToJSON(true);

    return indexingDTO;
  }

  public UpdateMappingRequest createUpdateMappingRequest() {
    UpdateMappingRequest updateMappingRequest = new UpdateMappingRequest();
    updateMappingRequest.setIndexMapping("indexMapping");
    updateMappingRequest.setIndexName("indexName");
    updateMappingRequest.setAsync(false);
    updateMappingRequest.setTypeName("typeName");

    return updateMappingRequest;
  }

  public Map<String, String> createSettings() {
    Map<String, String> settings = new HashMap<>();
    settings.put("setting1", "option1");

    return settings;
  }

  public QueryResponse createQueryResponse() {
    QueryResponse queryResponse = new QueryResponse();
    queryResponse.setTook(1000);
    queryResponse.setTimeOut(false);
    queryResponse.setCount(100);

    Hits hits = new Hits();
    hits.setMaxScore(100.0f);
    hits.setTotal(700);

    Hit hit = new Hit();
    hit.setId("id");
    List<Hit> hitList = new ArrayList<>();
    hitList.add(hit);
    hits.setHitList(hitList);
    queryResponse.setHits(hits);

    Shards shards = new Shards();
    shards.setFailed(2);
    shards.setSuccessful(20);
    shards.setTotal(22);
    queryResponse.setShards(shards);

    Aggregations aggregations = new Aggregations();
    Agg agg = new Agg();
    List<Bucket> buckets = new ArrayList<>();
    Bucket bucket = new Bucket();
    bucket.setKey(100L);
    buckets.add(bucket);
    agg.setBuckets(buckets);
    aggregations.setAgg(agg);
    queryResponse.setAggregations(aggregations);

    return queryResponse;
  }

  public QuerySort createQuerySort() {
    QuerySort querySort = new QuerySort();
    querySort.setSort("field1", "asc");

    return querySort;
  }

  public QuerySpec createQuerySpec() {
    QuerySpec querySpec = new QueryString();

    querySpec.setScroll(10);
    querySpec.setQuerySort(createQuerySort());
    querySpec.setScroll(null);

    return querySpec;
  }

  public QueryMatch createQueryMatch() {
    QueryMatch queryMatch = new QueryMatch();
    queryMatch.setField("field1");
    queryMatch.setTerm("term1", this);
    queryMatch.setValue(this);
    queryMatch.setQuerySort(createQuerySort());

    return queryMatch;
  }

  public ScrollRequest createScrollRequest() {
    ScrollRequest scrollRequest = new ScrollRequest();
    scrollRequest.setScroll(10);
    scrollRequest.setScrollId("id");

    return scrollRequest;
  }

  public QueryBoolean createQueryBoolean() {
    QueryBoolean queryBoolean = new QueryBoolean();
    queryBoolean.setQuerySort(createQuerySort());
    queryBoolean.setTerm(createQuerySpec(), BooleanType.MUST);
    queryBoolean.setTerm(createQuerySpec(), BooleanType.MUST);
    queryBoolean.setTerm(createQuerySpec(), BooleanType.MUSTNOT);
    queryBoolean.setTerm(createQuerySpec(), BooleanType.SHOULD);
    queryBoolean.setTerm(createQuerySpec(), null);

    return queryBoolean;
  }

  public QueryMultiMatch createQueryMultiMatch() {
    QueryMultiMatch queryMultiMatch = new QueryMultiMatch();
    queryMultiMatch.setTerm(this, "field1");
    queryMultiMatch.setQuerySort(createQuerySort());

    return queryMultiMatch;
  }

  public QueryTerm createQueryTerm() {
    QueryTerm queryTerm = new QueryTerm();
    queryTerm.setQuerySort(createQuerySort());

    return queryTerm;
  }

  public QueryTermNested createQueryTermNested() {
    QueryTermNested queryTermNested = new QueryTermNested();
    queryTermNested.setQuerySort(createQuerySort());

    return queryTermNested;
  }

  public QueryWildcard createQueryWildcard() {
    QueryWildcard queryWildcard = new QueryWildcard();
    queryWildcard.setQuerySort(createQuerySort());

    return queryWildcard;
  }

  public QueryWildcardNested createQueryWildcardNested() {
    QueryWildcardNested queryWildcardNested = new QueryWildcardNested();
    queryWildcardNested.setQuerySort(createQuerySort());

    return queryWildcardNested;
  }

  public QueryTerms createQueryTerms() {
    QueryTerms queryTerms = new QueryTerms();
    queryTerms.setQuerySort(createQuerySort());

    return queryTerms;
  }

  public QueryTermsNested createQueryTermsNested() {
    QueryTermsNested queryTermsNested = new QueryTermsNested();
    queryTermsNested.setQuerySort(createQuerySort());

    return queryTermsNested;
  }

  public QueryRange createQueryRange() {
    QueryRange queryRange = new QueryRange();
    queryRange.setQuerySort(createQuerySort());

    return queryRange;
  }

  public QueryStringSpecField createQueryStringSpecField() {
    QueryStringSpecField queryStringSpecField = new QueryStringSpecField();
    queryStringSpecField.setQuerySort(createQuerySort());

    return queryStringSpecField;
  }

  public QueryStringSpecFieldNested createQueryStringSpecFieldNested() {
    QueryStringSpecFieldNested queryStringSpecFieldNested = new QueryStringSpecFieldNested();
    queryStringSpecFieldNested.setQuerySort(createQuerySort());

    return queryStringSpecFieldNested;
  }

  public SimpleQueryString createSimpleQueryString() {
    SimpleQueryString simpleQueryString = new SimpleQueryString();
    simpleQueryString.setQuerySort(createQuerySort());

    return simpleQueryString;
  }

}
