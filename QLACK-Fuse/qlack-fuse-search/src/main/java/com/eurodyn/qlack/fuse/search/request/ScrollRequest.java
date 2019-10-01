package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;

/**
 * Request for scrolls
 *
 * @author European Dynamics SA.
 */
@Getter
public class ScrollRequest extends BaseRequest {

  /**
   * The scroll size
   */
  private Integer scroll;

  /**
   * The scroll id
   */
  private String scrollId;

  /**
   * Sets the scroll size
   *
   * @param scroll the scroll size
   * @return a {@link ScrollRequest}
   */
  public ScrollRequest setScroll(Integer scroll) {
    this.scroll = scroll;
    return this;
  }

  /**
   * Sets the Scroll Id
   *
   * @param scrollId the Scroll Id
   * @return a {@link ScrollRequest}
   */
  public ScrollRequest setScrollId(String scrollId) {
    this.scrollId = scrollId;
    return this;
  }
}
