package com.eurodyn.qlack.fuse.search.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * Elastic search configuration class
 *
 * @author European Dynamics SA.
 */
@Configuration
@EnableElasticsearchRepositories(queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
public class EsConfig {

  @Value("${qlack.fuse.search.cluster.name}")
  private String clusterName;

  @Value("${qlack.fuse.search.host.name}")
  private String hostName;

  @Value("${qlack.fuse.search.host.port}")
  private String hostPort;

  /**
   * Bean that creates a configuration for elastic search using the properties
   * acquired from the
   * <b>application.properties</b> file
   *
   * @return an Elastic search client
   * @throws UnknownHostException if the host cannot be found
   */
  @Bean
  @SuppressWarnings("squid:S2095")
  public TransportClient client() throws UnknownHostException {
    Settings settings = Settings.builder()
      .put("cluster.name", clusterName)
      .put("client.transport.sniff", false)
      .put("transport.host", hostName).build();

    return new PreBuiltTransportClient(settings).addTransportAddress(new
      TransportAddress(InetAddress.getByName(hostName),
      Integer.parseInt(hostPort)));
  }

  /**
   * Creates an Elastic search template by creating and using an Elastic
   * search client
   *
   * @return an {@link ElasticsearchOperations} object
   * @throws UnknownHostException if the host cannot be found
   * @see EsConfig#client()
   */
  @Bean
  public ElasticsearchOperations elasticsearchTemplate()
    throws UnknownHostException {
    return new ElasticsearchTemplate(client());
  }

}
