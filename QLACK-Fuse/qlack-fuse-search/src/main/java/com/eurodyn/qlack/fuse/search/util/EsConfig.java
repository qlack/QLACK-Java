package com.eurodyn.qlack.fuse.search.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

  /**
   * Environment reference
   */
  @Autowired
  private Environment env;

  /**
   * Bean that creates a configuration for elastic search using the properties
   * acquired from the <b>application.properties</b> file
   * @return an Elastic search client
   * @throws UnknownHostException if the host cannot be found
   */
  @Bean
  public Client client() throws UnknownHostException {

    Settings settings = Settings.builder()
        .put("cluster.name", env.getProperty("qlack.fuse.search.cluster.name"))
        .put("client.transport.sniff", false)
        .put("transport.host", env.getProperty("qlack.fuse.search.host.name")).build();

    return new PreBuiltTransportClient(settings).addTransportAddress(new
        TransportAddress(InetAddress.getByName(env.getProperty("qlack.fuse.search.host.name")),
        Integer.parseInt(env.getProperty("qlack.fuse.search.host.port"))));

  }

  /**
   * Creates an Elastic search template by creating and using an Elastic search client
   * @return an {@link ElasticsearchOperations} object
   * @throws UnknownHostException if the host cannot be found
   * @see EsConfig#client()
   */
  @Bean
  public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
    return new ElasticsearchTemplate(client());
  }

}
