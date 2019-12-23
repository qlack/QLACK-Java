package com.eurodyn.qlack.fuse.lexicon.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


/**
 * A class that specifies the criteria that is used in order to search for a
 * specific Key
 *
 * @author European Dynamics SA
 */
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

  /**
   * A Criteria Builder class that is used to construct CriteriaQuery for
   * searching a specific key object and its expression that is used to
   * achieve it.
   */
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

    /**
     * A method that is used to retrieve a specific key by its name
     *
     * @param name the name of the key
     * @return the key by its name
     */
    public KeySearchCriteriaBuilder withNameLike(String name) {
      criteria.setKeyName(name);
      return this;
    }

    /**
     * A Criteria query that searches the key using expression by inGroup
     *
     * @param groupId the group id
     * @return the key
     */
    public KeySearchCriteriaBuilder inGroup(String groupId) {
      criteria.setGroupId(groupId);
      return this;
    }

    /**
     * A Criteria Query to search for a specific key using criteria such as
     * number of page and the size of the page
     *
     * @param pageSize the page size
     * @param page the page number
     * @return the key
     */
    public KeySearchCriteriaBuilder setPageSizeWithPageNum(int pageSize,
      int page) {
      pageable = PageRequest.of(page, pageSize);
      return this;
    }

  }
}
