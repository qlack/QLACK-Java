package com.eurodyn.qlack.fuse.search.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ESClientTest {

  @InjectMocks private ESClient esClient;

  private Properties createProperties() {
    Properties properties = new Properties();
    properties.setEsHosts("http://localhost:9200");
    properties.setEsPassword("password");
    properties.setEsUsername("userName");
    properties.setVerifyHostname(true);
    return properties;
  }

  @Before
  public void init(){
    esClient = new ESClient(createProperties());
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
