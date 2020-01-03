package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request for creating indexes
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class CreateIndexRequest extends BaseRequest {

  /**
   * Name of the index request
   */
  private String name;

  /**
   * Name of the index type
   */
  private String type;

  /**
   * Shards
   */
  private int shards = 5;

  /**
   * Replicas
   */
  private int replicas = 1;

  /**
   * Mapping for the index
   */
  private String indexMapping;

  /**
   * Alias name of the index
   */
  private String aliasName;

  /**
   * Custom analyzer
   */
  private String analysis;

  /**
   * List of stopwords
   */
  private List<String> stopwords = new ArrayList<>();

  /**
   * Adds stopwords to the list
   *
   * @param words The stopwords
   */
  public void addStopWords(String... words) {
    if (words == null) {
      return;
    }

    Collections.addAll(this.stopwords, words);
  }
}
