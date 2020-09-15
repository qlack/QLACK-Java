package com.eurodyn.qlack.util.data.rest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RestResponseErrorHandler extends DefaultResponseErrorHandler {

  // JUL reference.
  private static final Logger LOGGER = Logger
    .getLogger(RestResponseErrorHandler.class.getName());

  public static int truncateLimit = 1024;

  @Override
  public void handleError(URI url, HttpMethod method, ClientHttpResponse response)
    throws IOException {
    // Display the http status code.
    LOGGER.log(Level.SEVERE, MessageFormat.format("{0}: {1}", url, response.getStatusCode()));

    // Get any content available in the body.
    String body = "";

    if (response.getBody() != null) {
      body = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
      StringUtils.abbreviate(body, truncateLimit);
    }

    // Log an exception for this error.
    LOGGER.log(Level.SEVERE, MessageFormat.format("{0}", body));
    super.handleError(url, method, response);
  }
}
