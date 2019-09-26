package com.eurodyn.qlack.fuse.search.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a specific hit within a set of hits held by {@link SearchResultDTO}
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class SearchHitDTO {

  /**
   * The source representation of this hit as a JSON object. This property holds the properties and
   * values of the original document that was indexed, therefore it is a good candidate to be
   * deserialized to get a a concrete objects out of a search result hit.
   */
  private String source;

  /**
   * the inner hits for the nested Objects
   */
  private String innerHits;

  /**
   * The score of this hit.
   */
  private float score;

  /**
   * The type of the document that was found. Combine this property with
   * <i>source</i>, so that you know to which object you should deserialise
   * a search hit.
   */
  private String type;

  /**
   * Id
   */
  private String id;

  @Override
  public String toString() {
    return "SearchHitDTO [source=" + source + ", score=" + score + ", type=" + type + ", innerHits="
        + innerHits + "]";
  }
}
