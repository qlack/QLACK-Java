package com.eurodyn.qlack.fuse.search.util;

import com.eurodyn.qlack.fuse.search.InitTestValues;
import org.elasticsearch.client.RestClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ESClientTest {

  @InjectMocks private ESClient esClient;
  @Mock private RestClient restClient;

  private Properties properties;

  @Before
  public void init(){
    InitTestValues initTestValues = new InitTestValues();
    properties = initTestValues.createProperties();
    esClient = new ESClient(properties);
  }

  @Test
  public void initTest(){
    esClient.init();
  }

  @Test
  public void shutdownTest() throws IOException {
    esClient.shutdown();
    assertNull(esClient.getClient());
  }

  @Test
  public void getRestClientTest(){
    //client is null because is not set
    assertNull(esClient.getRestClient());
  }

  @Test
  public void getClientTest(){
    //client is null because is not set
    assertNull(esClient.getClient());
  }
}
