package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
   * List of stopwords
   */
  private List<String> stopwords = new ArrayList<>();

  /**
   * Adds stopwords to the list
   * @param words
   */
  public void addStopWords(String... words) {
    if (words == null) {
      return;
    }

    for (String word : words) {
      stopwords.add(word);
    }
  }
}
