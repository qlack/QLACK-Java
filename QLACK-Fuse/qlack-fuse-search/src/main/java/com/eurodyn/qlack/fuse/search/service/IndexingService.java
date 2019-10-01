package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Provides indexing functionality
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
@Log
public class IndexingService {

  private static ObjectMapper mapper = new ObjectMapper();
  // The ES client injected by blueprint.
  private ESClient esClient;

  @Autowired
  public IndexingService(ESClient esClient) {
    this.esClient = esClient;
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  /**
   * Creates index for given document.
   *
   * @param dto holds the document to index, as well as the information about the index and it's
   * type
   */
  public void indexDocument(IndexingDTO dto) {
    try {
      log.info(MessageFormat.format("Indexing document {0}", dto));
      IndexRequest indexRequest = new IndexRequest(dto.getIndex(), dto.getType(), dto.getId())
          .source(mapper.writeValueAsString(dto.getSourceObject()), XContentType.JSON);

      IndexResponse response = esClient.getClient().index(indexRequest, RequestOptions.DEFAULT);

      log.log(Level.INFO,
          MessageFormat.format("Index document created with id: {0}, {1}", dto.getId(), response));

    } catch (IOException e) {
      log.log(Level.SEVERE,
          MessageFormat.format("Could not index document with id: {0}", dto.getId()), e);
      throw new SearchException(
          MessageFormat.format("Could not index document with id: {0}", dto.getId()));
    }
  }

  /**
   * Deletes index for given document.
   *
   * @param dto holds the document to delete it's index, as well as information about the index and
   * it's type
   */
  public void unindexDocument(ESDocumentIdentifierDTO dto) {
    try {
      log.info(MessageFormat.format("Deleting index of document {0}", dto));
      DeleteRequest request = new DeleteRequest(dto.getIndex(), dto.getType(), dto.getId());
      DeleteResponse response = esClient.getClient().delete(request, RequestOptions.DEFAULT);

      log.log(Level.INFO, MessageFormat.format("Index document deleted with id: {0}", dto.getId()),
          response);

    } catch (IOException e) {
      log.log(Level.SEVERE,
          MessageFormat.format("Could not delete document with id: {0}", dto.getId()), e);
      throw new SearchException(
          MessageFormat.format("Could not delete document with id: {0}", dto.getId()));
    }
  }

}
