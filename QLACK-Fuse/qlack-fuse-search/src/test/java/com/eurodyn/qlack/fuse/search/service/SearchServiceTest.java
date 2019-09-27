package com.eurodyn.qlack.fuse.search.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySpec;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.mappers.response.QueryResponse;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
  private AdminService adminService;

  @Mock
  private Response response;

  @Mock
  private HttpEntity httpEntity;

  private InitTestValues initTestValues;

  private QuerySpec querySpec;

  private QueryResponse queryResponse;

  private ObjectMapper objectMapper;

  @Before
  public void init() throws IOException {
    searchService = new SearchService(esClient, adminService);
    initTestValues = new InitTestValues();
    querySpec = initTestValues.createQuerySpec();
    queryResponse = initTestValues.createQueryResponse();
    objectMapper = new ObjectMapper();

    when(esClient.getClient()).thenReturn(restHighLevelClient);
    when(restHighLevelClient.getLowLevelClient()).thenReturn(restClient);
    when(response.getEntity()).thenReturn(httpEntity);
    when(httpEntity.getContent())
        .thenReturn(new ByteArrayInputStream(objectMapper.writeValueAsBytes(queryResponse)));
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
    querySpec.setCountOnly(true);
    querySpec.includeAllSources();
    querySpec.includeAllSources();
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    assertNotNull(searchService.search(querySpec));
  }

  @Test(expected = SearchException.class)
  public void searchIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenThrow(new IOException());
    searchService.search(querySpec);
  }

  @Test(expected = SearchException.class)
  public void searchMapperIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getEntity().getContent()).thenThrow(new IOException());
    searchService.search(querySpec);
  }
}
