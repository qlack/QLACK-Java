package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
  public AdminService(ESClient esClient, ElasticsearchOperations elasticsearchOperations) {
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
      log.log(Level.WARNING, "Index already exists: {0}.", createIndexRequest.getName());
      return false;
    }

    boolean createdIndex = elasticsearchOperations.createIndex(createIndexRequest.getName());
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
    log.info(MessageFormat.format("Creating index from class {0}", clazz.getName()));
    if (indexExists(clazz)) {
      log.log(Level.WARNING, "Index for class {0} already exists.", clazz.getName());
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
    log.info(MessageFormat.format("Deleting index ", indexName));
    return !canPerformOperation(indexName) || elasticsearchOperations.deleteIndex(indexName);
  }

  /**
   * Deletes an Elasticsearch index from given class.
   *
   * @param clazz a class annotated with Elasticsearch annotations
   * @return true if deleted, false otherwise
   */
  public boolean deleteIndex(Class clazz) {
    log.info(MessageFormat.format("Deleting index for class {0}", clazz.getName()));
    if (!indexExists(clazz)) {
      log.log(Level.WARNING, "Index for class {0} does not exist.", clazz.getName());
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
   * @param clazz the class
   * @return true if it exists, false otherwise
   */
  public boolean indexExists(Class clazz) {
    log.info(MessageFormat.format("Checking if index for class {0} exists", clazz.getName()));
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
    log.info(MessageFormat.format("Updating type mapping for index {0} and type {1}",
        updateRequest.getIndexName(), updateRequest.getTypeName()));
    // If the index does not exist return without doing anything.
    if (!indexExists(updateRequest.getIndexName())) {
      log.log(Level.WARNING, "Index does not exist: {0}.", updateRequest.getIndexName());
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
  public boolean updateIndexSettings(String indexName, Map<String, String> settings,
      boolean preserveExisting) {
    log.info(MessageFormat.format("Updating settings of index {0}. Existing settings will be {1}",
        indexName, preserveExisting ? "preserved" : "overwritten"));
    if (!canPerformOperation(indexName)) {
      return false;
    }

    String endpoint = indexName + "/_settings";
    if (preserveExisting) {
      endpoint += "?preserve_existing=true";
    }
    closeIndex(indexName);
    NStringEntity entity = new NStringEntity(new JSONObject(settings).toString(),
        ContentType.APPLICATION_JSON);
    boolean changedIndexSettings = commonIndexingOperationWithEntity("PUT", endpoint,
        "Could not change index settings.", entity);
    openIndex(indexName);
    return changedIndexSettings;
  }

  /**
   * Closes an Elasticsearch index.
   *
   * @param indexName the name of the index to close
   * @return true if closed, false otherwise
   */
  public boolean closeIndex(String indexName) {
    log.info(MessageFormat.format("Closing index {0}", indexName));
    String endpoint = indexName + "/_close";
    return !canPerformOperation(indexName) || commonIndexingOperation("POST", endpoint,
        "Could not close index.");
  }

  /**
   * Opens an Elasticsearch index.
   *
   * @param indexName the name of the index to open
   * @return true if opened, false otherwise
   */
  public boolean openIndex(String indexName) {
    log.info(MessageFormat.format("Opening index {0}", indexName));
    String endpoint = indexName + "/_open";
    return !canPerformOperation(indexName) || commonIndexingOperation("POST", endpoint,
        "Could not open index.");
  }

  /**
   * Checks if connection to Elasticsearch cluster is up.
   *
   * @return true if up, false otherwise
   */
  public boolean checkIsUp() {
    log.info("Checking cluster health");
    try {
      Request request = new Request("GET", "_cluster/health");
      Response response = esClient.getClient().getLowLevelClient().performRequest(request);

      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not check cluster health", e);
      return false;
    }
  }

  /**
   * Checks if the provided class is annotated with @{@link Document}
   * @param clazz the class
   * @return true if the class is annotated, false otherwise
   */
  protected boolean isClassValid(Class clazz) {
    if (!clazz.isAnnotationPresent(Document.class)) {
      log.log(Level.SEVERE, "Unable to identify index name. " + clazz.getSimpleName() +
          " is not a Document. Make sure the document class is annotated with @Document(indexName=\"foo\")");
      return false;
    }
    return true;
  }

  /**
   * Checks if index exists
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

  /**
   * Executes a request on Elastic Search client
   * @param method an HTTP method (GET, POST etc.)
   * @param indexName an index name
   * @param errorMsg a custom error message
   * @return true if the response http status code is 200 (OK), false otherwise
   */
  private boolean commonIndexingOperation(String method, String indexName, String errorMsg) {
    return commonIndexingOperationWithEntity(method, indexName, errorMsg, null);
  }

  /**
   * Executes a request on Elastic Search client
   * @param method an HTTP method (GET, POST etc.)
   * @param endpoint an endpoint on which the request should be performed
   * @param errorMsg a custom error message
   * @param entity an HttpEntity
   * @return true if the response http status code is 200 (OK), false otherwise
   */
  private boolean commonIndexingOperationWithEntity(String method, String endpoint, String errorMsg,
      NStringEntity entity) {
    try {
      Request request = new Request(method, endpoint);
      if (entity != null) {
        request.setEntity(entity);
      }
      Response response = esClient.getClient().getLowLevelClient().performRequest(request);
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      log.log(Level.SEVERE, errorMsg, e);
      throw new SearchException(errorMsg, e);
    }
  }
}
