package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import lombok.extern.java.Log;
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
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Provides index administration functionality
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
@Log
public class AdminService {

  /**
   * Elastic Search client
   */
  private ESClient esClient;

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  @Autowired
  public AdminService(ESClient esClient,
      ElasticsearchOperations elasticsearchOperations) {
    this.esClient = esClient;
    this.elasticsearchOperations = elasticsearchOperations;
  }

  /**
   * Creates an Elasticsearch index from given index request.
   *
   * @param createIndexRequest object containing the index information
   * @return true if index was created, false otherwise
   */
  public boolean createIndex(CreateIndexRequest createIndexRequest) {
    log.info(MessageFormat.format("Creating index {0}", createIndexRequest));

    /** If the index already exists return without doing anything. */
    if (indexExists(createIndexRequest.getName())) {
      log.log(Level.WARNING, "Index already exists: {0}.",
          createIndexRequest.getName());
      return false;
    }

    //custom indexing settings
    Map<String, String> analysisSettings = new HashMap<>();

    //if an analyzer has been provided, include it in the settings
    if (!StringUtils.isEmpty(createIndexRequest.getAnalysis())) {
      analysisSettings.put("analysis.analyzer.custom_analysis.type", "custom");
      analysisSettings.put("analysis.analyzer.custom_analysis.tokenizer", "standard");
      analysisSettings
          .put("analysis.analyzer.custom_analysis.filter", createIndexRequest.getAnalysis());
    }

    //create index with name and settings (Java map) as parameters
    boolean createdIndex = elasticsearchOperations.createIndex(createIndexRequest.getName(),
        analysisSettings);

    //set alias name for the newly created index
    if (!StringUtils.isEmpty(createIndexRequest.getAliasName())) {
      AliasQuery aliasQuery = new AliasQuery();
      aliasQuery.setIndexName(createIndexRequest.getName());
      aliasQuery.setAliasName(createIndexRequest.getAliasName());
      elasticsearchOperations.addAlias(aliasQuery);
    }

    return createIndexRequest.getIndexMapping() == null ? createdIndex :
        elasticsearchOperations
            .putMapping(createIndexRequest.getName(), createIndexRequest.getType(),
                createIndexRequest.getIndexMapping());
  }

  /**
   * Creating an Elasticsearch index from given class.
   *
   * @param clazz a class annotated with Elasticsearch annotations
   * @return true if created, false otherwise
   */
  public boolean createIndex(Class clazz) {
    log.info(
        MessageFormat.format("Creating index from class {0}", clazz.getName()));
    if (indexExists(clazz)) {
      log.log(Level.WARNING, "Index for class {0} already exists.",
          clazz.getName());
      return false;
    }
    return !isClassValid(clazz) || (elasticsearchOperations.createIndex(clazz)
        && elasticsearchOperations.putMapping(clazz));
  }

  /**
   * Deletes an Elasticsearch index by given name.
   *
   * @param indexName the name of the index to delete
   * @return true if deleted, false otherwise
   */
  public boolean deleteIndex(String indexName) {
    log.info(MessageFormat.format("Deleting index {0}", indexName));
    return !canPerformOperation(indexName) || elasticsearchOperations
        .deleteIndex(indexName);
  }

  /**
   * Deletes an Elasticsearch index from given class.
   *
   * @param clazz a class annotated with Elasticsearch annotations
   * @return true if deleted, false otherwise
   */
  public boolean deleteIndex(Class clazz) {
    log.info(
        MessageFormat.format("Deleting index for class {0}", clazz.getName()));
    if (!indexExists(clazz)) {
      log.log(Level.WARNING, "Index for class {0} does not exist.",
          clazz.getName());
      return false;
    }
    return !isClassValid(clazz) || elasticsearchOperations.deleteIndex(clazz);
  }

  /**
   * Checks if an Elasticsearch index with the given name, exists.
   *
   * @param indexName the name of the index
   * @return true if exists, false otherwise
   */
  public boolean indexExists(String indexName) {
    log.info(MessageFormat.format("Checking if index {0} exists", indexName));
    return elasticsearchOperations.indexExists(indexName);
  }

  /**
   * Checks if an Elasticsearch index from given class exists.
   *
   * @param clazz the class
   * @return true if it exists, false otherwise
   */
  public boolean indexExists(Class clazz) {
    log.info(MessageFormat
        .format("Checking if index for class {0} exists", clazz.getName()));
    return !isClassValid(clazz) || elasticsearchOperations.indexExists(clazz);
  }

  /**
   * Checks if a document exists.
   *
   * @param indexName the name of the index that the document is part of
   * @param typeName the name of the index type that the document is part of
   * @param id the id of the document
   * @return true if exists, false otherwise
   */
  public boolean documentExists(String indexName, String typeName, String id) {
    ESDocumentIdentifierDTO dto = new ESDocumentIdentifierDTO(indexName, typeName, id);
    return documentExists(dto);
  }

  /**
   * Checks if a document exists.
   *
   * @param dto holds all the information needed to search for the document (index name, index id,
   * document id)
   * @return true if exists, false otherwise
   */
  public boolean documentExists(ESDocumentIdentifierDTO dto) {
    log.info(MessageFormat.format("Checking if document {0}exists", dto));
    try {
      GetRequest getRequest = new GetRequest(dto.getIndex(), dto.getType(), dto.getId());
      return esClient.getClient().exists(getRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      String errorMsg = MessageFormat
          .format("Could not check if document with id: {0} exists", dto.getId());
      log.log(Level.SEVERE, errorMsg, e);
      throw new SearchException(errorMsg, e);
    }
  }

  /**
   * Updates the type mapping of an Elasticsearch index.
   *
   * @param updateRequest holds all information needed to update the mapping
   * @return true if updated, false otherwise
   */
  public boolean updateTypeMapping(UpdateMappingRequest updateRequest) {
    log.info(
        MessageFormat.format("Updating type mapping for index {0} and type {1}",
            updateRequest.getIndexName(), updateRequest.getTypeName()));
    // If the index does not exist return without doing anything.
    if (!indexExists(updateRequest.getIndexName())) {
      log.log(Level.WARNING, "Index does not exist: {0}.",
          updateRequest.getIndexName());
      return false;
    }

    return elasticsearchOperations
        .putMapping(updateRequest.getIndexName(), updateRequest.getTypeName(),
            updateRequest.getIndexMapping());
  }

  /**
   * Updates the settings of given Elasticsearch index.
   *
   * @param indexName the name of the index to update
   * @param settings the updated settings
   * @param preserveExisting a flag to define whether the existing settings should be preserved or
   * overwritten
   * @return true if updated, false otherwise
   */
  public boolean updateIndexSettings(String indexName,
      Map<String, String> settings,
      boolean preserveExisting) {
    log.info(MessageFormat.format("Updating settings of index {0}. Existing settings will be {1}",
        indexName, preserveExisting ? "preserved" : "overwritten"));
    if (!canPerformOperation(indexName)) {
      return false;
    }

    UpdateSettingsRequest request = new UpdateSettingsRequest(indexName)
        .settings(settings)
        .setPreserveExisting(preserveExisting);

    closeIndex(indexName);
    try {
      UpdateSettingsResponse updateSettingsResponse =
          esClient.getClient().indices().putSettings(request, RequestOptions.DEFAULT);
      openIndex(indexName);
      return updateSettingsResponse.isAcknowledged();
    } catch (IOException e) {
      throw new SearchException("Could not change index settings.", e);
    }
  }

  /**
   * Closes an Elasticsearch index.
   *
   * @param indexName the name of the index to close
   * @return true if closed, false otherwise
   */
  @Async
  public boolean closeIndex(String indexName) {
    log.info(MessageFormat.format("Closing index {0}", indexName));
    CloseIndexRequest closeIndexRequest = new CloseIndexRequest(indexName);

    try {
      CloseIndexResponse closeIndexResponse = esClient.getClient().indices()
          .close(closeIndexRequest,
              RequestOptions.DEFAULT);
      return closeIndexResponse.isAcknowledged();
    } catch (IOException | ElasticsearchStatusException e) {
      throw new SearchException("Could not close index.", e);
    }
  }

  /**
   * Opens an Elasticsearch index.
   *
   * @param indexName the name of the index to open
   * @return true if opened, false otherwise
   */
  public boolean openIndex(String indexName) {
    log.info(MessageFormat.format("Opening index {0}", indexName));
    OpenIndexRequest openIndexRequest = new OpenIndexRequest(indexName);

    try {
      OpenIndexResponse resp = esClient.getClient().indices().open(openIndexRequest,
          RequestOptions.DEFAULT);
      return resp.isAcknowledged();
    } catch (IOException | ElasticsearchStatusException e) {
      throw new SearchException("Could not open index.", e);
    }
  }

  /**
   * Checks if connection to Elasticsearch cluster is up.
   *
   * @return true if up, false otherwise
   */
  public boolean checkIsUp() {
    log.info("Checking cluster health");
    ClusterHealthRequest clusterHealthRequest = new ClusterHealthRequest();
    try {
      ClusterHealthResponse health = esClient.getClient().cluster()
          .health(clusterHealthRequest, RequestOptions.DEFAULT);
      return health.status().getStatus() == 200;
    } catch (IOException ex) {
      log.log(Level.SEVERE, "Could not check cluster health", ex);
      return false;
    }
  }

  /**
   * Checks if the provided class is annotated with @{@link Document}
   *
   * @param clazz the class
   * @return true if the class is annotated, false otherwise
   */
  protected boolean isClassValid(Class clazz) {
    if (!clazz.isAnnotationPresent(Document.class)) {
      log.log(Level.SEVERE,
          "Unable to identify index name. " + clazz.getSimpleName() +
              " is not a Document. Make sure the document class is annotated with @Document(indexName=\"foo\")");
      return false;
    }
    return true;
  }

  /**
   * Checks if index exists
   *
   * @param indexName an index name
   * @return true if the index exists, false otherwise
   */
  private boolean canPerformOperation(String indexName) {
    if (!indexExists(indexName)) {
      log.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }
    return true;
  }
}
