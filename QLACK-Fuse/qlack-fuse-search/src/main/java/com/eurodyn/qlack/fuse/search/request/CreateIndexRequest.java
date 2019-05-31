package com.eurodyn.qlack.fuse.search.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIndexRequest extends BaseRequest {

  private String name;
  private String type;
  private int shards = 5;
  private int replicas = 1;
  private String indexMapping;
  private List<String> stopwords = new ArrayList<>();

  public void addStopWords(String... words) {
    if (words == null) {
      return;
    }

    for (String word : words) {
      stopwords.add(word);
    }
  }
}
