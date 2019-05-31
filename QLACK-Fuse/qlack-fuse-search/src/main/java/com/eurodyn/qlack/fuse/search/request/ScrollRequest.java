package com.eurodyn.qlack.fuse.search.request;

public class ScrollRequest extends BaseRequest {

  private Integer scroll;
  private String scrollId;

  public Integer getScroll() {
    return scroll;
  }

  public ScrollRequest setScroll(Integer scroll) {
    this.scroll = scroll;
    return this;
  }

  public String getScrollId() {
    return scrollId;
  }

  public ScrollRequest setScrollId(String scrollId) {
    this.scrollId = scrollId;
    return this;
  }
}
