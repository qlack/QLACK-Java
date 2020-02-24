package com.eurodyn.qlack.fuse.aaa.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * A UserGroupSearchCriteria class that is used to specify the criteria to search for
 * a group
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class UserGroupSearchCriteria {

  private String name;
  private String nameLike;
  private String description;
  private Pageable pageable;
  private Collection<String> includeIds;
  private Collection<String> excludeIds;

  private UserGroupSearchCriteria() {
  }

  public static class UserGroupSearchCriteriaBuilder {

    private UserGroupSearchCriteria criteria;

    private UserGroupSearchCriteriaBuilder() {
      criteria = new UserGroupSearchCriteria();
    }

    public static UserGroupSearchCriteriaBuilder createCriteria() {
      return new UserGroupSearchCriteriaBuilder();
    }

    public UserGroupSearchCriteria build() {
      return criteria;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved groups
     * should be contained.
     *
     * @param ids the ids of the groups
     * @return UserSearchCriteriaBuilder
     */
    public UserGroupSearchCriteriaBuilder withIdIn(Collection<String> ids) {
      criteria.setIncludeIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved groups
     * should not be contained.
     *
     * @param ids the ids of the groups
     * @return UserSearchCriteriaBuilder
     */
    public UserGroupSearchCriteriaBuilder withIdNotIn(Collection<String> ids) {
      criteria.setExcludeIds(ids);
      return this;
    }

    /**
     * Specify a name for which to check
     *
     * @param name the name of the group
     * @return UserSearchCriteriaBuilder
     */
    public UserGroupSearchCriteriaBuilder withName(String name) {
      criteria.setName(name);
      return this;
    }

    /**
     * Specify a name to check for, with the operator like
     *
     * @param nameLike the name of the group
     * @return UserSearchCriteriaBuilder
     */
    public UserGroupSearchCriteriaBuilder withNameLike(String nameLike) {
      criteria.setNameLike(nameLike);
      return this;
    }

  }

}
