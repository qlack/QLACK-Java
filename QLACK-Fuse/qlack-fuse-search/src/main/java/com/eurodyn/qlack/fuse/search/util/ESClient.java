package com.eurodyn.qlack.fuse.search.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * A client to communicate with ES. This client is using the {@link RestClient} implementation of the ES Java client.
 *
 * //TODO support credentials as well as SSL: https://qbox.io/blog/rest-calls-new-java-elasticsearch-client-tutorial
 */
@Service
@Validated
public class ESClient {

  // Logger.
  private static final Logger LOGGER = Logger.getLogger(ESClient.class.getName());

  // Service references.
  private RestClient restClient;
  private RestHighLevelClient rhClient;
  private Properties properties;

  @Autowired
  public ESClient(Properties properties) {
    this.properties = properties;
  }

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
      RestClient.builder(httpHosts).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

        @Override
        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
          if (StringUtils.isNotEmpty(properties.getEsUsername())
            && StringUtils.isNotEmpty(properties.getEsPassword())) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
              properties.getEsUsername(), properties.getEsPassword()));

            httpClientBuilder = httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
          }

          if (!properties.isVerifyHostname()) {
            httpClientBuilder = httpClientBuilder.setSSLHostnameVerifier(new HostnameVerifier() {

              @Override
              public boolean verify(String hostname, SSLSession session) {
                return true;
              }
            });
          }

          return httpClientBuilder;
        }
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
   * Returns the client.
   *
   * @return A {@link RestClient} instance.
   */
  public RestClient getRestClient() {
    return restClient;
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
