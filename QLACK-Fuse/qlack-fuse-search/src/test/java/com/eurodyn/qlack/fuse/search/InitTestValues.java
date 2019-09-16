package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.util.Properties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;

import java.util.ArrayList;
import java.util.List;

public class InitTestValues {

  public Properties createProperties() {
    Properties properties = new Properties();
    properties.setEsHosts("http://localhost:9200");
    properties.setEsPassword("password");
    properties.setEsUsername("userName");
    properties.setVerifyHostname(true);
    return properties;
  }
}
