package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean.BooleanType;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryExists;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryExistsNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySort;
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
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.ScrollRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Arrays;
import java.util.HashMap;
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

  public QuerySort createQuerySort() {
    QuerySort querySort = new QuerySort();
    querySort.setSort("field1", SortOrder.ASC);

    return querySort;
  }

  public QueryString createQueryString() {
    QueryString queryString = new QueryString().setQueryStringValue("fooField: valueToMatch");

    queryString.setScroll(10);
    queryString.setQuerySort(createQuerySort());

    return queryString;
  }

  public QueryMatch createQueryMatch() {
    QueryMatch queryMatch = new QueryMatch();
    queryMatch.setTerm("fieldName", "valueToMatch");
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
    queryBoolean.setTerm(createQueryString(), BooleanType.MUST);
    queryBoolean.setTerm(createQueryString(), BooleanType.MUSTNOT);
    queryBoolean.setTerm(createQueryString(), BooleanType.SHOULD);

    return queryBoolean;
  }

  public QueryMultiMatch createQueryMultiMatch() {
    QueryMultiMatch queryMultiMatch = new QueryMultiMatch();
    queryMultiMatch.setTerm("valueToMatch", "field1", "field2");
    queryMultiMatch.setQuerySort(createQuerySort());

    return queryMultiMatch;
  }

  public QueryTerm createQueryTerm() {
    QueryTerm queryTerm = new QueryTerm();
    queryTerm.setTerm("fieldName", "valueToMatch");
    queryTerm.setQuerySort(createQuerySort());

    return queryTerm;
  }

  public QueryTermNested createQueryTermNested() {
    QueryTermNested queryTermNested = new QueryTermNested();
    queryTermNested.setTerm("nestedObjectName.fieldName", "valueToMatch", "nestedObjectName",
        Arrays.asList("field1, field2"));
    queryTermNested.setQuerySort(createQuerySort());

    return queryTermNested;
  }

  public QueryWildcard createQueryWildcard() {
    QueryWildcard queryWildcard = new QueryWildcard();
    queryWildcard.setTerm("filedName", "valueToMa*");
    queryWildcard.setQuerySort(createQuerySort());

    return queryWildcard;
  }

  public QueryWildcardNested createQueryWildcardNested() {
    QueryWildcardNested queryWildcardNested = new QueryWildcardNested();
    queryWildcardNested.setTerm("nestedObjectName.fieldName", "valueToMa*", "nestedObjectName",
        Arrays.asList("field1, field2"));
    queryWildcardNested.setQuerySort(createQuerySort());

    return queryWildcardNested;
  }

  public QueryTerms createQueryTerms() {
    QueryTerms queryTerms = new QueryTerms();
    queryTerms.setTerm("fieldName", Arrays.asList("valueToMatch1, valueToMatch2"));
    queryTerms.setQuerySort(createQuerySort());

    return queryTerms;
  }

  public QueryTermsNested createQueryTermsNested() {
    QueryTermsNested queryTermsNested = new QueryTermsNested();
    queryTermsNested.setTerm("nestedObjectName.fieldName", Arrays.asList("valueToMatch1",
        "valueToMatch2"),
        "nestedObjectName",
        Arrays.asList("field1, field2"));
    queryTermsNested.setQuerySort(createQuerySort());

    return queryTermsNested;
  }

  public QueryRange createQueryRange() {
    QueryRange queryRange = new QueryRange();
    queryRange.setTerm("fieldName", 10, 50);
    queryRange.setQuerySort(createQuerySort());

    return queryRange;
  }

  public QueryStringSpecField createQueryStringSpecField() {
    QueryStringSpecField queryStringSpecField = new QueryStringSpecField();

    queryStringSpecField.setTerm("fieldName", "ra* em*", "OR");
    queryStringSpecField.setQuerySort(createQuerySort());

    return queryStringSpecField;
  }

  public QueryStringSpecFieldNested createQueryStringSpecFieldNested() {
    QueryStringSpecFieldNested queryStringSpecFieldNested = new QueryStringSpecFieldNested();
    queryStringSpecFieldNested.setTerm("nestedObjectName.fieldName", "ra* em*", "OR",
        "nestedObjectName",
        Arrays.asList("field1, field2"));
    queryStringSpecFieldNested.setQuerySort(createQuerySort());

    return queryStringSpecFieldNested;
  }

  public SimpleQueryString createSimpleQueryString() {
    SimpleQueryString simpleQueryString = new SimpleQueryString();
    simpleQueryString.setTerm("fieldName", "valueToMatch?", "OR");
    simpleQueryString.setQuerySort(createQuerySort());

    return simpleQueryString;
  }

  public QueryExists createQueryExists() {
    QueryExists queryExists = new QueryExists();
    queryExists.setField("fieldName");
    queryExists.setQuerySort(createQuerySort());

    return queryExists;
  }

  public QueryExistsNested createQueryExistsNested() {
    QueryExistsNested queryExistsNested = new QueryExistsNested();
    queryExistsNested.setTerm("nestedObjectName.fieldName", "nestedObjectName");
    queryExistsNested.setQuerySort(createQuerySort());

    return queryExistsNested;
  }

  public SearchHit[] searchHits() {
    BytesReference source = new BytesArray("{search source result}");
    SearchHit[] searchHits = new SearchHit[1];
    searchHits[0] = new SearchHit(1).sourceRef(source);

    return searchHits;
  }
}
