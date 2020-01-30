package com.eurodyn.qlack.fuse.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.UnknownSearchDTO;
import com.eurodyn.qlack.fuse.search.dto.SearchHitDTO;
import com.eurodyn.qlack.fuse.search.dto.SearchHitsDTO;
import com.eurodyn.qlack.fuse.search.dto.SearchResultDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.HighlightField;
import com.eurodyn.qlack.fuse.search.dto.queries.InnerHits;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryExists;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryHighlight;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
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
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.ScrollRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregatorFactories.Builder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

  private final String TYPE_NAME = "typeName";
  private final String ID = "id";
  private final String INDEX_NAME = "indexName";

  @InjectMocks
  private SearchService searchService;

  @Captor
  private ArgumentCaptor<SearchRequest> searchRequestArgumentCaptor;
  @Captor
  private ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor;

  @Mock
  private RestHighLevelClient restHighLevelClient;
  @Mock
  private ESClient esClient;
  @Mock
  private ObjectMapper mappedObjectMapper;
  @Mock
  private SearchResponse searchResponse;
  @Mock
  private SearchHits searchHits;
  @Mock
  private SearchHit searchHit;

  private SearchHit[] hits;
  private InitTestValues initTestValues;
  private QueryExists queryExists;
  private QueryString queryString;
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
  private QuerySpec queryExistsNested;
  private QueryHighlight queryHighlight;

  @Before
  public void init() throws IOException {
    searchService = new SearchService(esClient);
    initTestValues = new InitTestValues();

    objectMapper = new ObjectMapper();

    queryString = initTestValues.createQueryString();
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
    queryStringSpecFieldNested = initTestValues.createQueryStringSpecFieldNested();
    simpleQueryString = initTestValues.createSimpleQueryString();
    queryExists = initTestValues.createQueryExists();
    queryExistsNested = initTestValues.createQueryExistsNested();
    queryHighlight = initTestValues.createQueryHighlight();

    hits = new SearchHit[1];
    hits[0] = searchHit;

    ReflectionTestUtils.setField(searchService, "mapper", mappedObjectMapper);

    when(esClient.getClient()).thenReturn(restHighLevelClient);
    when(restHighLevelClient.search(any(SearchRequest.class), any(RequestOptions.class)))
        .thenReturn(searchResponse);
    when(searchResponse.getHits()).thenReturn(searchHits);
    when(searchResponse.getTook()).thenReturn(TimeValue.ZERO);
    when(searchHits.getHits()).thenReturn(hits);
    when(mappedObjectMapper.writeValueAsString(any(SearchResponse.class))).thenReturn("mapped");
  }

  @Test
  public void searchTest() {
    assertNotNull(searchService.search(queryString));
  }

  @Test
  public void searchCountOnlyTest() {
    queryString.setCountOnly(true);
    SearchResultDTO search = searchService.search(queryString);
    assertNotNull(search);
  }

  @Test(expected = SearchException.class)
  public void searchExceptionWhenIOExceptionTest() throws IOException {
    when(restHighLevelClient.search(any(SearchRequest.class), any(RequestOptions.class)))
        .thenThrow(new IOException());
    searchService.search(queryBoolean);
  }

  @Test
  public void findByIdTest() throws IOException {
    GetResponse getResponse = mock(GetResponse.class);
    when(restHighLevelClient.get(any(GetRequest.class), any(RequestOptions.class)))
        .thenReturn(getResponse);
    when(getResponse.getType()).thenReturn(TYPE_NAME);
    when(getResponse.getId()).thenReturn(ID);

    SearchHitDTO byId = searchService.findById(INDEX_NAME, TYPE_NAME, ID);

    assertNotNull(byId);
    assertEquals(byId.getId(), ID);
    assertEquals(byId.getType(), TYPE_NAME);
  }

  @Test
  public void findByIdNullTest() throws IOException {
    when(restHighLevelClient.get(any(GetRequest.class), any(RequestOptions.class)))
        .thenThrow(new IOException());
    assertNull(searchService.findById(INDEX_NAME, TYPE_NAME, ID));
  }

  @Test
  public void prepareScrollTest() throws IOException {
    when(restHighLevelClient.search(any(SearchRequest.class), any(
        RequestOptions.class))).thenReturn(searchResponse);
    when(searchResponse.getScrollId()).thenReturn("scrollId");
    ScrollRequest scrollRequest = searchService.prepareScroll(INDEX_NAME, queryMatch, 10);
    assertNotNull(scrollRequest);
    assertEquals("scrollId", scrollRequest.getScrollId());
  }

  @Test
  public void prepareScrollIoExceptionTest() throws IOException {
    when(restHighLevelClient.search(any(SearchRequest.class), any(
        RequestOptions.class))).thenThrow(new IOException());
    ScrollRequest scrollRequest = searchService.prepareScroll(INDEX_NAME, queryMatch, 10);
    assertNotNull(scrollRequest);
    assertNull(scrollRequest.getScrollId());
  }

  @Test
  public void scrollTest() throws IOException {
    when(restHighLevelClient.scroll(any(SearchScrollRequest.class), any(RequestOptions.class)))
        .thenReturn(searchResponse);
    assertNotNull(searchService.scroll(scrollRequest));
  }


  @Test(expected = SearchException.class)
  public void scrollIoExceptionTest() throws IOException {
    when(restHighLevelClient.scroll(any(SearchScrollRequest.class), any(RequestOptions.class)))
        .thenThrow(new IOException());
    assertNotNull(searchService.scroll(scrollRequest));
  }

  @Test
  public void searchQueryBooleanTest() throws IOException {
    assertNotNull(searchService.search(queryBoolean));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(searchRequestArgumentCaptor.getValue().source().query() instanceof BoolQueryBuilder);
  }

  @Test
  public void searchQueryExistsTest() throws IOException {
    assertNotNull(searchService.search(queryExists));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof ExistsQueryBuilder);
  }

  @Test
  public void searchQueryExistsNestedTest() throws IOException {
    assertNotNull(searchService.search(queryExistsNested));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof NestedQueryBuilder);

    NestedQueryBuilder parentQuery = (NestedQueryBuilder) searchRequestArgumentCaptor.getValue()
        .source().query();
    assertTrue(parentQuery.query() instanceof ExistsQueryBuilder);
  }

  @Test
  public void searchQueryMatchTest() throws IOException {
    assertNotNull(searchService.search(queryMatch));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof MatchQueryBuilder);
  }

  @Test
  public void searchQueryMultiMatchTest() throws IOException {
    assertNotNull(searchService.search(queryMultiMatch));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof MultiMatchQueryBuilder);
  }

  @Test
  public void searchQueryTermTest() throws IOException {
    assertNotNull(searchService.search(queryTerm));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(searchRequestArgumentCaptor.getValue().source().query() instanceof TermQueryBuilder);
  }

  @Test
  public void searchQueryTermNestedTest() throws IOException {
    assertNotNull(searchService.search(queryTermNested));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof NestedQueryBuilder);

    NestedQueryBuilder parentQuery = (NestedQueryBuilder) searchRequestArgumentCaptor.getValue()
        .source().query();
    assertTrue(parentQuery.query() instanceof TermQueryBuilder);
  }

  @Test
  public void searchQueryWildcardTest() throws IOException {
    assertNotNull(searchService.search(queryWildcard));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof WildcardQueryBuilder);
  }

  @Test
  public void searchQueryWildcardNestedTest() throws IOException {
    assertNotNull(searchService.search(queryWildcardNested));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof NestedQueryBuilder);

    NestedQueryBuilder parentQuery = (NestedQueryBuilder) searchRequestArgumentCaptor.getValue()
        .source().query();

    assertTrue(parentQuery.query() instanceof WildcardQueryBuilder);
  }

  @Test
  public void searchQueryTermsTest() throws IOException {
    assertNotNull(searchService.search(queryTerms));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());

    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof TermsQueryBuilder);
  }

  @Test
  public void searchQueryTermsNestedTest() throws IOException {
    assertNotNull(searchService.search(queryTermsNested));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof NestedQueryBuilder);

    NestedQueryBuilder parentQuery = (NestedQueryBuilder) searchRequestArgumentCaptor.getValue()
        .source().query();

    assertTrue(parentQuery.query() instanceof TermsQueryBuilder);
  }

  @Test
  public void searchQueryRangeTest() throws IOException {
    assertNotNull(searchService.search(queryRange));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof RangeQueryBuilder);
  }

  @Test
  public void searchQueryStringSpecFieldTest() throws IOException {
    assertNotNull(searchService.search(queryStringSpecField));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof QueryStringQueryBuilder);
  }

  @Test
  public void searchQueryStringSpecFieldNestedTest() throws IOException {
    assertNotNull(searchService.search(queryStringSpecFieldNested));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof NestedQueryBuilder);

    NestedQueryBuilder parentQuery = (NestedQueryBuilder) searchRequestArgumentCaptor.getValue()
        .source().query();

    assertTrue(parentQuery.query() instanceof QueryStringQueryBuilder);
  }

  @Test
  public void searchSimpleQueryStringTest() throws IOException {
    assertNotNull(searchService.search(simpleQueryString));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source()
            .query() instanceof SimpleQueryStringBuilder);
  }

  @Test
  public void searchMultipleSortTest() throws IOException {
    queryString.setQuerySort(queryString.getQuerySort().setSort("field2", SortOrder.DESC));

    assertNotNull(searchService.search(queryString));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());

    assertEquals(2, searchRequestArgumentCaptor.getValue().source().sorts().size());
  }

  @Test
  public void searchUnknownDtoInstanceTest() throws IOException {
    UnknownSearchDTO unknownSearchDTO = new UnknownSearchDTO();
    unknownSearchDTO.setQuerySort(initTestValues.createQuerySort());

    assertNotNull(searchService.search(unknownSearchDTO));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    assertTrue(
        searchRequestArgumentCaptor.getValue().source().query() instanceof MatchAllQueryBuilder);
  }

  @Test
  public void searchIncludesExcludesTest() throws IOException {
    queryBoolean.include("field2");
    queryBoolean.exclude("field1");

    assertNotNull(searchService.search(queryBoolean));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());
    FetchSourceContext fetchSourceContext = searchRequestArgumentCaptor.getValue().source()
        .fetchSource();

    assertEquals("field2", fetchSourceContext.includes()[0]);
    assertEquals("field1", fetchSourceContext.excludes()[0]);
  }

  @Test
  public void searchAggregationTest() throws IOException {
    queryBoolean.setAggregate("field1");
    queryBoolean.setAggregateSize(30);

    assertNotNull(searchService.search(queryBoolean));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());

    Builder aggregations = searchRequestArgumentCaptor.getValue().source().aggregations();
    List<AggregationBuilder> aggregatorFactories = aggregations.getAggregatorFactories();
    AggregationBuilder aggregationBuilder = aggregatorFactories.get(0);

    assertEquals("agg", aggregationBuilder.getName());
  }

  @Test
  public void searchWithHightlightingTest() throws IOException {

    queryTerm.setHighlight(queryHighlight);

    assertNotNull(searchService.search(queryTerm));
    verify(restHighLevelClient).search(searchRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());

    HighlightBuilder highlighter = searchRequestArgumentCaptor.getValue().source().highlighter();
    HighlightField highlightField = queryHighlight.getFields().get(0);

    assertEquals(highlightField.getType(), highlighter.highlighterType());
    assertEquals(highlightField.isForceSource(), highlighter.forceSource());
    assertTrue(Arrays.asList(highlighter.preTags()).contains(queryHighlight.getPreTag()));
    assertTrue(Arrays.asList(highlighter.postTags()).contains(queryHighlight.getPostTag()));
    assertTrue(highlighter.highlightQuery() instanceof BoolQueryBuilder);
  }

  @Test
  public void innerHitsTest() {
    SearchHits nestedSearchHits = mock(SearchHits.class);
    SearchHit nestedSearchHit = mock(SearchHit.class);
    SearchHit[] nestedSearchHitArray = new SearchHit[1];
    nestedSearchHitArray[0] = nestedSearchHit;

    queryTermNested.setInnerHits(
        new InnerHits().setSize(10).setHighlight(queryHighlight)
    );

    Map<String, SearchHits> innerHits = new HashMap<>();
    innerHits.put("nestedName", nestedSearchHits);

    when(searchHit.getInnerHits()).thenReturn(innerHits);
    when(nestedSearchHits.getHits()).thenReturn(nestedSearchHitArray);

    SearchResultDTO search = searchService.search(queryTermNested);
    SearchHitDTO searchHitDTO = search.getHits().get(0);
    Map<String, SearchHitsDTO> innerHitsDTO = searchHitDTO.getInnerHits();

    assertNotNull(search);
    assertNotNull(innerHitsDTO);
    assertEquals(innerHits.size(), innerHitsDTO.size());
  }

}