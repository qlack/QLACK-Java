package com.eurodyn.qlack.fuse.search.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import java.io.IOException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndexingServiceTest {

  @InjectMocks
  private IndexingService indexingService;

  @Mock
  private ESClient esClient;

  @Mock
  private RestHighLevelClient restHighLevelClient;

  @Mock
  private IndexResponse indexResponse;

  @Mock
  private DeleteResponse deleteResponse;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    indexingService = new IndexingService(esClient);

    when(esClient.getClient()).thenReturn(restHighLevelClient);
  }

  @Test
  public void indexDocumentTest() throws IOException {
    when(restHighLevelClient.index(any(IndexRequest.class), any(RequestOptions.class)))
        .thenReturn(indexResponse);
    indexingService.indexDocument(initTestValues.createIndexingDTO());
    verify(restHighLevelClient, times(1)).index(any(IndexRequest.class), any(RequestOptions.class));
  }

  @Test(expected = SearchException.class)
  public void indexDocumentIoExceptionTest() {
    IndexingDTO indexingDTO = initTestValues.createIndexingDTO();
    indexingDTO.setSourceObject(new Object());
    indexingService.indexDocument(indexingDTO);
  }

  @Test
  public void unindexDocumentTest() throws IOException {
    when(restHighLevelClient.delete(any(DeleteRequest.class), any(RequestOptions.class)))
        .thenReturn(deleteResponse);
    indexingService.unindexDocument(new ESDocumentIdentifierDTO());
    verify(restHighLevelClient, times(1))
        .delete(any(DeleteRequest.class), any(RequestOptions.class));
  }

  @Test(expected = SearchException.class)
  public void unindexDocumentIoExceptionTest() throws IOException {
    when(restHighLevelClient.delete(any(DeleteRequest.class), any(RequestOptions.class)))
        .thenThrow(new IOException());
    indexingService.unindexDocument(new ESDocumentIdentifierDTO());
    verify(restHighLevelClient, times(1))
        .delete(any(DeleteRequest.class), any(RequestOptions.class));
  }

}
