package com.eurodyn.qlack.fuse.search.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.search.ESSerializableClass;
import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.SerializableClass;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.ClusterClient;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.AliasQuery;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
  private IndicesClient indicesClient;

  @Mock
  private ClusterClient clusterClient;

  @Mock
  private RestStatus restStatus;

  @Mock
  private CloseIndexResponse closeIndexResponse;

  @Mock
  private OpenIndexResponse openIndexResponse;

  @Mock
  private UpdateSettingsResponse updateSettingsResponse;

  @Mock
  private ClusterHealthResponse clusterHealthResponse;

  @Captor
  private ArgumentCaptor<UpdateSettingsRequest> updateSettingsRequestArgumentCaptor;

  @Captor
  private ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor;

  @Captor
  private ArgumentCaptor<AliasQuery> aliasQueryArgumentCaptor;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  @Captor
  private ArgumentCaptor<HashMap> hashMapArgumentCaptor;

  private InitTestValues initTestValues;
  private CreateIndexRequest createIndexRequest;
  private UpdateMappingRequest updateMappingRequest;
  private Map<String, String> settings;

  @Before
  public void init() throws IOException {
    adminService = new AdminService(esClient, elasticsearchOperations);
    initTestValues = new InitTestValues();
    createIndexRequest = initTestValues.createIndexRequest();
    updateMappingRequest = initTestValues.updateMappingRequest();
    settings = initTestValues.createSettings();

    when(esClient.getClient()).thenReturn(restHighLevelClient);
    when(restHighLevelClient.indices()).thenReturn(indicesClient);
    when(restHighLevelClient.cluster()).thenReturn(clusterClient);

    when(indicesClient.close(any(CloseIndexRequest.class), any(RequestOptions.class)))
        .thenReturn(closeIndexResponse);
    when(indicesClient.open(any(OpenIndexRequest.class), any(RequestOptions.class)))
        .thenReturn(openIndexResponse);
    when(indicesClient.putSettings(any(UpdateSettingsRequest.class), any(RequestOptions.class)))
        .thenReturn(updateSettingsResponse);
    when(clusterClient.health(any(ClusterHealthRequest.class), any(RequestOptions.class)))
        .thenReturn(clusterHealthResponse);
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
  public void createIndexWithAnalysis() {
    createIndexRequest.setAnalysis("someCoolAnalyzer");

    when(elasticsearchOperations
        .putMapping(createIndexRequest.getName(), createIndexRequest.getType(),
            createIndexRequest.getIndexMapping())).thenReturn(true);
    assertTrue(adminService.createIndex(createIndexRequest));

    verify(elasticsearchOperations).createIndex(stringArgumentCaptor.capture(),
        hashMapArgumentCaptor.capture());

    Map<String, String> analysisSettings = new HashMap<>();

    analysisSettings.put("analysis.analyzer.custom_analysis.type", "custom");
    analysisSettings.put("analysis.analyzer.custom_analysis.tokenizer", "standard");
    analysisSettings
        .put("analysis.analyzer.custom_analysis.filter", createIndexRequest.getAnalysis());

    assertEquals(analysisSettings, hashMapArgumentCaptor.getValue());
  }

  @Test
  public void createIndexWithAlias() {
    createIndexRequest.setAliasName("An alias of the index");

    when(elasticsearchOperations
        .putMapping(createIndexRequest.getName(), createIndexRequest.getType(),
            createIndexRequest.getIndexMapping())).thenReturn(true);
    assertTrue(adminService.createIndex(createIndexRequest));
    verify(elasticsearchOperations).addAlias(aliasQueryArgumentCaptor.capture());

    AliasQuery value = aliasQueryArgumentCaptor.getValue();
    assertEquals(createIndexRequest.getName(), value.getIndexName());
    assertEquals(createIndexRequest.getAliasName(), value.getAliasName());
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
    when(elasticsearchOperations.indexExists("indexName")).thenReturn(true);
    when(closeIndexResponse.isAcknowledged()).thenReturn(true);
    when(openIndexResponse.isAcknowledged()).thenReturn(true);
    when(updateSettingsResponse.isAcknowledged()).thenReturn(true);

    assertTrue(adminService.updateIndexSettings("indexName", settings, false));
    verify(indicesClient).putSettings(updateSettingsRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());

    UpdateSettingsRequest usr = updateSettingsRequestArgumentCaptor.getValue();
    assertEquals(false, usr.isPreserveExisting());
  }

  @Test
  public void updateIndexSettingsPreserveExistingTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName")).thenReturn(true);
    when(closeIndexResponse.isAcknowledged()).thenReturn(true);
    when(openIndexResponse.isAcknowledged()).thenReturn(true);
    when(updateSettingsResponse.isAcknowledged()).thenReturn(true);

    assertTrue(adminService.updateIndexSettings("indexName", settings, true));
    verify(indicesClient).putSettings(updateSettingsRequestArgumentCaptor.capture(),
        requestOptionsArgumentCaptor.capture());

    UpdateSettingsRequest usr = updateSettingsRequestArgumentCaptor.getValue();
    assertEquals(true, usr.isPreserveExisting());
  }

  @Test(expected = SearchException.class)
  public void updateIndexSettingsEXceptionTest() throws IOException {
    when(elasticsearchOperations.indexExists("indexName")).thenReturn(true);
    when(indicesClient.putSettings(any(UpdateSettingsRequest.class), any(RequestOptions.class)))
        .thenThrow(IOException.class);
    when(closeIndexResponse.isAcknowledged()).thenReturn(true);

    assertTrue(adminService.updateIndexSettings("indexName", settings, false));
  }

  @Test
  public void updateIndexSettingsUnexistedTest() {
    when(elasticsearchOperations.indexExists("indexName")).thenReturn(false);
    assertFalse(adminService.updateIndexSettings("indexName", settings, true));
  }

  @Test
  public void closeIndexTest() {
    when(closeIndexResponse.isAcknowledged()).thenReturn(true);
    assertTrue(adminService.closeIndex("indexName"));
  }

  @Test
  public void closeIndexFailedResponseTest() {
    when(closeIndexResponse.isAcknowledged()).thenReturn(false);
    assertFalse(adminService.closeIndex("indexName"));
  }

  @Test(expected = SearchException.class)
  public void closeIndexIOExceptionTest() throws IOException {
    when(indicesClient.close(any(CloseIndexRequest.class), any(RequestOptions.class)))
        .thenThrow(IOException.class);
    adminService.closeIndex("indexName");
  }

  @Test(expected = SearchException.class)
  public void closeIndexNonExistingIndex() throws IOException {
    when(indicesClient.close(any(CloseIndexRequest.class), any(RequestOptions.class)))
        .thenThrow(ElasticsearchStatusException.class);
    adminService.closeIndex("indexName");
  }

  @Test
  public void openIndexTest() {
    when(openIndexResponse.isAcknowledged()).thenReturn(true);
    assertTrue(adminService.openIndex("indexName"));
  }

  @Test
  public void openIndexFailedTest() {
    when(openIndexResponse.isAcknowledged()).thenReturn(false);
    assertFalse(adminService.openIndex("indexName"));
  }

  @Test(expected = SearchException.class)
  public void openIndexIOExceptionTest() throws IOException {
    when(indicesClient.open(any(OpenIndexRequest.class), any(RequestOptions.class)))
        .thenThrow(IOException.class);
    adminService.openIndex("indexName");
  }

  @Test(expected = SearchException.class)
  public void openIndexNonExistingIndex() throws IOException {
    when(indicesClient.open(any(OpenIndexRequest.class), any(RequestOptions.class)))
        .thenThrow(ElasticsearchStatusException.class);
    adminService.openIndex("indexName");
  }

  @Test
  public void checkIsUpTest() {
    when(clusterHealthResponse.status()).thenReturn(restStatus);
    when(restStatus.getStatus()).thenReturn(200);
    assertTrue(adminService.checkIsUp());
  }

  @Test
  public void checkIsDownTest() {
    when(clusterHealthResponse.status()).thenReturn(restStatus);
    when(restStatus.getStatus()).thenReturn(403);
    assertFalse(adminService.checkIsUp());
  }

  @Test
  public void checkIsUpIoExceptionTest() throws IOException {
    when(clusterClient.health(any(ClusterHealthRequest.class), any(RequestOptions.class)))
        .thenThrow(new IOException());
    assertFalse(adminService.checkIsUp());
  }
}