package com.eurodyn.qlack.fuse.search.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.UnknownSearchDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySpec;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryStringSpecField;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryStringSpecFieldNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTerm;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTermNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTerms;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTermsNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryWildcard;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryWildcardNested;
import com.eurodyn.qlack.fuse.search.dto.queries.SimpleQueryString;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.mapper.request.InternalSearchRequest;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Aggregations;
import com.eurodyn.qlack.fuse.search.mapper.response.QueryResponse.Hits;
import com.eurodyn.qlack.fuse.search.request.ScrollRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

  @InjectMocks
  private SearchService searchService;

  @Mock
  private RestHighLevelClient restHighLevelClient;

  @Mock
  private RestClient restClient;

  @Mock
  private ESClient esClient;

  @Mock
  private Response response;

  @Mock
  private HttpEntity httpEntity;

  @Mock
  private ObjectMapper mappedObjectMapper;

  @Mock
  private StatusLine statusLine;

  @Mock
  private SearchResponse searchResponse;

  private InitTestValues initTestValues;

  private QuerySpec querySpec;

  private QueryResponse queryResponse;

  private ObjectMapper objectMapper;

  private QueryMatch queryMatch;

  private ScrollRequest scrollRequest;

  private QueryBoolean queryBoolean;

  private QueryMultiMatch queryMultiMatch;

  private QueryTerm queryTerm;

  private QueryTermNested queryTermNested;

  private QueryWildcard queryWildcard;

  private QueryWildcardNested queryWildcardNested;

  private QueryTerms queryTerms;

  private QueryTermsNested queryTermsNested;

  private QueryRange queryRange;

  private QueryStringSpecField queryStringSpecField;

  private QueryStringSpecFieldNested queryStringSpecFieldNested;

  private SimpleQueryString simpleQueryString;

  @Before
  public void init() throws IOException {
    searchService = new SearchService(esClient);
    initTestValues = new InitTestValues();
    querySpec = initTestValues.createQuerySpec();
    queryResponse = initTestValues.createQueryResponse();
    objectMapper = new ObjectMapper();
    queryMatch = initTestValues.createQueryMatch();
    scrollRequest = initTestValues.createScrollRequest();
    queryBoolean = initTestValues.createQueryBoolean();
    queryMultiMatch = initTestValues.createQueryMultiMatch();
    queryTerm = initTestValues.createQueryTerm();
    queryTermNested = initTestValues.createQueryTermNested();
    queryWildcard = initTestValues.createQueryWildcard();
    queryWildcardNested = initTestValues.createQueryWildcardNested();
    queryTerms = initTestValues.createQueryTerms();
    queryTermsNested = initTestValues.createQueryTermsNested();
    queryRange = initTestValues.createQueryRange();
    queryStringSpecField = initTestValues.createQueryStringSpecField();
    queryStringSpecFieldNested = initTestValues
        .createQueryStringSpecFieldNested();
    simpleQueryString = initTestValues.createSimpleQueryString();

    when(esClient.getClient()).thenReturn(restHighLevelClient);
    when(restHighLevelClient.getLowLevelClient()).thenReturn(restClient);
    when(response.getEntity()).thenReturn(httpEntity);
    when(httpEntity.getContent())
        .thenReturn(new ByteArrayInputStream(
            objectMapper.writeValueAsBytes(queryResponse)));
  }

  @Test
  public void searchTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchCountOnlyTest() throws IOException {
    querySpec.setCountOnly(true);
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchTotalEditVariablesTest() throws IOException {
    querySpec.setPageSize(1000);
    querySpec.setIndex("indexName1", "indexName2");
    querySpec.setType("type1", "type2");
    querySpec.setAggregate("aggregate");
    querySpec.setScroll(10);
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchTotalEditVariablesScenario2Test() throws IOException {
    querySpec.setCountOnly(false);
    querySpec.includeAllSources();
    querySpec.excludeResults();
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchTotalEditVariablesScenario3Test() throws IOException {
    querySpec.setCountOnly(true);
    querySpec.includeAllSources();
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test(expected = SearchException.class)
  public void searchMappingExceptionTest() throws IOException {
    querySpec.setCountOnly(false);
    querySpec.includeAllSources();
    when(restClient.performRequest(any(Request.class))).thenReturn(response);

    ReflectionTestUtils.setField(searchService, "mapper", mappedObjectMapper);
    when(
        mappedObjectMapper.writeValueAsString(any(InternalSearchRequest.class)))
        .thenReturn("entity");
    when(mappedObjectMapper
        .readValue(response.getEntity().getContent(), QueryResponse.class))
        .thenReturn(queryResponse);
    when(mappedObjectMapper.writeValueAsString(any(QueryResponse.class)))
        .thenThrow(new JsonProcessingException("") {
        });
    searchService.search(querySpec);
  }

  @Test(expected = SearchException.class)
  public void searchIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class)))
        .thenThrow(new IOException());
    searchService.search(querySpec);
  }

  @Test(expected = SearchException.class)
  public void searchMapperIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getEntity().getContent()).thenThrow(new IOException());
    searchService.search(querySpec);
  }

  @Test
  public void findByIdTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);
    assertNotNull(searchService.findById("indexName", "typeName", "id"));
  }

  @Test
  public void findByIdNullTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(403);
    assertNull(searchService.findById("indexName", "typeName", "id"));
  }

  @Test
  public void findByIdIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class)))
        .thenThrow(new IOException());
    assertNull(searchService.findById("indexName", "typeName", "id"));
  }

  @Test
  public void prepareScrollTest() throws IOException {
    when(restHighLevelClient.search(any(SearchRequest.class), any(
        RequestOptions.class))).thenReturn(searchResponse);
    assertNotNull(searchService.prepareScroll("indexName", queryMatch, 10));
  }

  @Test
  public void prepareScrollIoExceptionTest() throws IOException {
    when(restHighLevelClient.search(any(SearchRequest.class), any(
        RequestOptions.class))).thenThrow(new IOException());
    assertNotNull(searchService.prepareScroll("indexName", queryMatch, 10));
  }

  @Test
  public void scrollTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.scroll(scrollRequest));
  }

  @Test
  public void scrollEmptyHitsTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    queryResponse.setHits(new Hits());
    when(httpEntity.getContent())
        .thenReturn(new ByteArrayInputStream(
            objectMapper.writeValueAsBytes(queryResponse)));
    assertNotNull(searchService.scroll(scrollRequest));
  }

  @Test(expected = SearchException.class)
  public void scrollIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class)))
        .thenThrow(new IOException());
    assertNotNull(searchService.scroll(scrollRequest));
  }

  @Test
  public void searchQueryBooleanTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryBoolean));
  }

  @Test
  public void searchQueryMatchTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryMatch));
  }

  @Test
  public void searchQueryMultiMatchTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryMultiMatch));
  }

  @Test
  public void searchQueryTermTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryTerm));
  }

  @Test
  public void searchQueryTermNestedTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryTermNested));
  }

  @Test
  public void searchQueryWildcardTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryWildcard));
  }

  @Test
  public void searchQueryWildcardNestedTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryWildcardNested));
  }

  @Test
  public void searchQueryTermsTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryTerms));
  }

  @Test
  public void searchQueryTermsNestedTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryTermsNested));
  }

  @Test
  public void searchQueryRangeTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryRange));
  }

  @Test
  public void searchQueryStringSpecFieldTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryStringSpecField));
  }

  @Test
  public void searchQueryStringSpecFieldNestedTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryStringSpecFieldNested));
  }

  @Test
  public void searchSimpleQueryStringTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(simpleQueryString));
  }

  @Test
  public void searchEditAggregationTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    queryResponse.setAggregations(new Aggregations());
    when(httpEntity.getContent())
        .thenReturn(new ByteArrayInputStream(
            objectMapper.writeValueAsBytes(queryResponse)));
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchEditAggregationNullTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    queryResponse.setAggregations(null);
    when(httpEntity.getContent())
        .thenReturn(new ByteArrayInputStream(
            objectMapper.writeValueAsBytes(queryResponse)));
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchMultipleSortTest() throws IOException {
    querySpec.setQuerySort(querySpec.getQuerySort().setSort("field2", "desc"));
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test
  public void searchQueryMultiMatchMultipleFieldsTest() throws IOException {
    queryMultiMatch.setTerm(this, "field1", "field2");
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(queryMultiMatch));
  }

  @Test
  public void searchUnknownDtoInstanceTest() throws IOException {
    UnknownSearchDTO unknownSearchDTO = new UnknownSearchDTO();
    unknownSearchDTO.setQuerySort(initTestValues.createQuerySort());
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(unknownSearchDTO));
  }

}
