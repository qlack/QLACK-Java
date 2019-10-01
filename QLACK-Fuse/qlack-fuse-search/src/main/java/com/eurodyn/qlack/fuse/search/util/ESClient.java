package com.eurodyn.qlack.fuse.search.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * A client to communicate with ES. This client is using the {@link RestClient} implementation of
 * the ES Java client.
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
public class ESClient {

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(ESClient.class.getName());

  /**
   * Elastic search RestHighLevelClient
   */
  private RestHighLevelClient rhClient;

  /**
   * properties
   */
  private Properties properties;

  @Value("${qlack.fuse.search.host.name}")
  private String hostNameProperty;

  @Autowired
  public ESClient(Properties properties) {
    this.properties = properties;
  }

  /**
   * Post-construct client initialization method. It initializes a REST High level client using
   * properties provided in the <b>application.properties</b> file.
   */
  @PostConstruct
  public void init() {
    LOGGER.log(Level.WARNING, "Initialising connection to ES: {0}", properties.getEsHosts());
    // WARN Initialising connection to ES: http://localhost:9200 -
    // c.e.qlack.fuse.search.util.ESClient
    /** Process Http hosts for ES */

    final HttpHost[] httpHosts = Arrays.stream(properties.getEsHosts().split(","))
        .map(host -> new HttpHost(host.split(":")[1], Integer.parseInt(host.split(":")[2]),
            host.split(":")[0]))
        .collect(Collectors.toList())
        .toArray(new HttpHost[properties.getEsHosts().split(",").length]);

    rhClient = new RestHighLevelClient(
        RestClient.builder(httpHosts)
            .setHttpClientConfigCallback(httpClientBuilder -> {
              if (StringUtils.isNotEmpty(properties.getEsUsername())
                  && StringUtils.isNotEmpty(properties.getEsPassword())) {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                    properties.getEsUsername(), properties.getEsPassword()));

                httpClientBuilder = httpClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider);
              }

              return httpClientBuilder;
            }));
  }

  /**
   * Default shutdown hook.
   *
   * @throws IOException If client can not be closed.
   */
  public void shutdown() throws IOException {
    LOGGER.log(Level.WARNING, "Shutting down connection to ES.");
  }

  /**
   * Returns the RestHighLevelClient.
   *
   * @return A {@link RestClient} instance.
   */
  public RestHighLevelClient getClient() {
    return rhClient;
  }

}
