package com.eurodyn.qlack.fuse.lexicon.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Getter
@Setter
public class KeySearchCriteria {

  private String groupId;
  private String keyName;
  private boolean ascending = true;
  private Pageable pageable;

  public enum SortType {
    ASCENDING,
    DESCENDING
  }

  public static class KeySearchCriteriaBuilder {

    private KeySearchCriteria criteria;
    private Pageable pageable;

    private KeySearchCriteriaBuilder() {
      criteria = new KeySearchCriteria();
    }

    public static KeySearchCriteriaBuilder createCriteria() {
      return new KeySearchCriteriaBuilder();
    }

    public KeySearchCriteria build() {
      criteria.setPageable(pageable);
      return criteria;
    }

    public KeySearchCriteriaBuilder withNameLike(String name) {
      criteria.setKeyName(name);
      return this;
    }

    public KeySearchCriteriaBuilder inGroup(String groupId) {
      criteria.setGroupId(groupId);
      return this;
    }

    public KeySearchCriteriaBuilder setPageSizeWithPageNum(int pageSize, int page) {
      pageable = PageRequest.of(page, pageSize);
      return this;
    }

  }
}
