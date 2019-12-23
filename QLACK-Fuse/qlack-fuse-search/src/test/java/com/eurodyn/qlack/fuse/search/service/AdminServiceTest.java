package com.eurodyn.qlack.fuse.search.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.search.ESSerializableClass;
import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.SerializableClass;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import java.io.IOException;
import java.util.Map;
import org.apache.http.StatusLine;
import org.elasticsearch.action.get.GetRequest;
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
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

  @InjectMocks
  private AdminService adminService;

  @Mock
  private ElasticsearchOperations elasticsearchOperations;

  @Mock
  private ESClient esClient;

  @Mock
  private RestHighLevelClient restHighLevelClient;

  @Mock
  private RestClient restClient;

  @Mock
  private Response response;

  @Mock
  private StatusLine statusLine;

  private InitTestValues initTestValues;

  private CreateIndexRequest createIndexRequest;

  private UpdateMappingRequest updateMappingRequest;

  private Map<String, String> settings;

  @Before
  public void init() {
    adminService = new AdminService(esClient, elasticsearchOperations);
    initTestValues = new InitTestValues();
    createIndexRequest = initTestValues.createIndexRequest();
    updateMappingRequest = initTestValues.updateMappingRequest();
    settings = initTestValues.createSettings();

    when(esClient.getClient()).thenReturn(restHighLevelClient);
    when(restHighLevelClient.getLowLevelClient()).thenReturn(restClient);
  }

  @Test
  public void createIndexTest() {
    when(elasticsearchOperations
      .putMapping(createIndexRequest.getName(), createIndexRequest.getType(),
        createIndexRequest.getIndexMapping())).thenReturn(true);
    assertTrue(adminService.createIndex(createIndexRequest));
  }

  @Test
  public void createIndexNullIndexMappingTest() {
    createIndexRequest.setIndexMapping(null);
    assertFalse(adminService.createIndex(createIndexRequest));
  }

  @Test
  public void createIndexExistedIndexTest() {
    when(elasticsearchOperations.indexExists(createIndexRequest.getName()))
      .thenReturn(true);
    assertFalse(adminService.createIndex(createIndexRequest));
  }

  @Test
  public void createIndexFromClassTest() {
    when(elasticsearchOperations.createIndex(ESSerializableClass.class))
      .thenReturn(true);
    when(elasticsearchOperations.putMapping(ESSerializableClass.class))
      .thenReturn(true);
    assertTrue(adminService.createIndex(ESSerializableClass.class));
  }

  @Test
  public void createIndexFromClassNonDocumentTest() {
    assertFalse(adminService.createIndex(SerializableClass.class));
  }

  @Test
  public void createIndexFromClassExistedInjectTest() {
    when(elasticsearchOperations.indexExists(ESSerializableClass.class))
      .thenReturn(true);
    assertFalse(adminService.createIndex(ESSerializableClass.class));
  }

  @Test
  public void createIndexFromClassUncreatedIndexUnputMappingTest() {
    assertFalse(adminService.createIndex(ESSerializableClass.class));
  }

  @Test
  public void createIndexFromClassExistedClassCreatedIndexUnputMappingTest() {
    when(elasticsearchOperations.createIndex(ESSerializableClass.class))
      .thenReturn(true);
    when(elasticsearchOperations.putMapping(ESSerializableClass.class))
      .thenReturn(false);
    assertFalse(adminService.createIndex(ESSerializableClass.class));
  }

  @Test
  public void createIndexFromClassExistedClassCreatedIndexInvalidClassTest() {
    AdminService adminService2 = spy(adminService);
    when(adminService2.isClassValid(SerializableClass.class)).thenReturn(true)
      .thenReturn(false);
    assertTrue(adminService2.createIndex(SerializableClass.class));
  }

  @Test
  public void deleteIndexTest() {
    when(elasticsearchOperations.indexExists("indexName")).thenReturn(true);
    when(elasticsearchOperations.deleteIndex("indexName")).thenReturn(true);
    assertTrue(adminService.deleteIndex("indexName"));
  }

  @Test
  public void deleteIndexUndeletedTest() {
    when(elasticsearchOperations.indexExists("indexName")).thenReturn(true);
    when(elasticsearchOperations.deleteIndex("indexName")).thenReturn(false);
    assertFalse(adminService.deleteIndex("indexName"));
  }

  @Test
  public void deleteIndexUnexistedUndeletedTest() {
    assertTrue(adminService.deleteIndex("indexName"));
  }

  @Test
  public void deleteIndexClassTest() {
    when(elasticsearchOperations.indexExists(ESSerializableClass.class))
      .thenReturn(true);
    when(elasticsearchOperations.deleteIndex(ESSerializableClass.class))
      .thenReturn(true);
    assertTrue(adminService.deleteIndex(ESSerializableClass.class));
  }

  @Test
  public void deleteIndexClassUnExistedTest() {
    assertFalse(adminService.deleteIndex(ESSerializableClass.class));
  }

  @Test
  public void deleteIndexClassUndeletedTest() {
    when(elasticsearchOperations.indexExists(ESSerializableClass.class))
      .thenReturn(true);
    assertFalse(adminService.deleteIndex(ESSerializableClass.class));
  }

  @Test
  public void deleteIndexClassUndeletedInvalidClassTest() {
    assertTrue(adminService.deleteIndex(SerializableClass.class));
  }

  @Test
  public void documentExistsTest() throws IOException {
    when(restHighLevelClient
      .exists(any(GetRequest.class), any(RequestOptions.class)))
      .thenReturn(true);
    assertTrue(adminService.documentExists("indexName", "typeName", "id"));
  }

  @Test(expected = SearchException.class)
  public void documentExistsIoExceptionTest() throws IOException {
    when(restHighLevelClient
      .exists(any(GetRequest.class), any(RequestOptions.class)))
      .thenThrow(new IOException());
    adminService.documentExists("indexName", "typeName", "id");
  }

  @Test
  public void updateTypeMappingTest() {
    when(
      elasticsearchOperations.indexExists(updateMappingRequest.getIndexName()))
      .thenReturn(true);
    when(elasticsearchOperations
      .putMapping(updateMappingRequest.getIndexName(),
        updateMappingRequest.getTypeName(),
        updateMappingRequest.getIndexMapping())).thenReturn(true);
    assertTrue(adminService.updateTypeMapping(updateMappingRequest));
  }

  @Test
  public void updateTypeMappingUnexistedTest() {
    assertFalse(adminService.updateTypeMapping(updateMappingRequest));
  }

  @Test
  public void updateTypeMappingUnupdatedTest() {
    when(
      elasticsearchOperations.indexExists(updateMappingRequest.getIndexName()))
      .thenReturn(true);
    when(elasticsearchOperations
      .putMapping(updateMappingRequest.getIndexName(),
        updateMappingRequest.getTypeName(),
        updateMappingRequest.getIndexMapping())).thenReturn(false);
    assertFalse(adminService.updateTypeMapping(updateMappingRequest));
  }

  @Test
  public void updateIndexSettingsTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(true);
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);
    assertTrue(adminService.updateIndexSettings("indexName", settings, false));
  }

  @Test
  public void updateIndexSettingsPreserveExistingTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(true);
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);
    assertTrue(adminService.updateIndexSettings("indexName", settings, true));
  }

  @Test
  public void updateIndexSettingsUnexistedTest() {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(false);
    assertFalse(adminService.updateIndexSettings("indexName", settings, true));
  }

  @Test
  public void closeIndexWrongResponseTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(true);
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(403);
    assertFalse(adminService.closeIndex("indexName"));
  }

  @Test
  public void closeIndexUnexistedTest() {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(false);
    assertTrue(adminService.closeIndex("indexName"));
  }

  @Test
  public void openIndexWrongResponseTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(true);
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(403);
    assertFalse(adminService.openIndex("indexName"));
  }

  @Test
  public void openIndexUnexistedTest() {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(false);
    assertTrue(adminService.openIndex("indexName"));
  }

  @Test
  public void checkIsUpTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);
    assertTrue(adminService.checkIsUp());
  }

  @Test
  public void checkIsUpWrongAnswerTest() throws IOException {
    when(restClient.performRequest(any(Request.class))).thenReturn(response);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(403);
    assertFalse(adminService.checkIsUp());
  }

  @Test
  public void checkIsUpIoExceptionTest() throws IOException {
    when(restClient.performRequest(any(Request.class)))
      .thenThrow(new IOException());
    assertFalse(adminService.checkIsUp());
  }

  @Test(expected = SearchException.class)
  public void canPerformOperationIoExceptionTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName"))
      .thenReturn(true);
    when(restClient.performRequest(any(Request.class)))
      .thenThrow(new IOException());
    adminService.updateIndexSettings("indexName", settings, true);
  }
}
