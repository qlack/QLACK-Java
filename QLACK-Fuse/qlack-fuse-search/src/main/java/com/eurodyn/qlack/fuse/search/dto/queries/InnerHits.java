package com.eurodyn.qlack.fuse.search.dto.queries;

import java.util.ArrayList;
import java.util.List;

/**
 * This class requests information about inner hits.
 */
public class InnerHits {

  private final List<String> excludes = new ArrayList();
  private int size = 3;
  private QueryHighlight highlight;

  public int getSize() {
    return size;
  }

  public InnerHits setSize(int size) {
    this.size = size;
    return this;
  }

  /**
   * Exclude the given field from the inner hits source.
   *
   * @param field The field name.
   * @return This object.
   */
  public InnerHits exclude(String field) {
    excludes.add(field);
    return this;
  }

  public List<String> getExcludes() {
    return excludes;
  }

  public QueryHighlight getHighlight() {
    return highlight;
  }

  public InnerHits setHighlight(QueryHighlight highlight) {
    this.highlight = highlight;
    return this;
  }
}
