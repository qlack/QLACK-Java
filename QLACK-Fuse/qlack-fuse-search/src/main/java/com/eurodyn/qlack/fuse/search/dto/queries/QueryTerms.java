package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The term query finds documents that contain the exact terms specified in the inverted index.
 * Example:
 */
@Getter
public class QueryTerms extends QuerySpec {

  private String field;
  private List<String> values = new ArrayList<>();


  public QueryTerms setTerm(String field, List<String> values) {
    this.field = field;
    this.values = values;

    return this;
  }
}
