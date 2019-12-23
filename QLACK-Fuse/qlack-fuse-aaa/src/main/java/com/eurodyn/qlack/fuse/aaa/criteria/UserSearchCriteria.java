package com.eurodyn.qlack.fuse.aaa.criteria;

import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria.UserAttributeCriteria.Type;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import java.util.Arrays;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

/**
 * A UserSearchCriteria class that is used to specify the criteria to search for
 * a User
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class UserSearchCriteria {

  private Collection<String> includeIds;
  private Collection<String> excludeIds;
  private Collection<String> includeGroupIds;
  private Collection<String> excludeGroupIds;
  private Collection<Byte> includeStatuses;
  private Collection<Byte> excludeStatuses;
  private String username;
  private UserAttributeCriteria attributeCriteria;
  private Boolean superadmin;
  private Pageable pageable;

  private UserSearchCriteria() {
  }

  public static class UserSearchCriteriaBuilder {

    private UserSearchCriteria criteria;

    private UserSearchCriteriaBuilder() {
      criteria = new UserSearchCriteria();
    }

    public static UserSearchCriteriaBuilder createCriteria() {
      return new UserSearchCriteriaBuilder();
    }

    public static UserAttributeCriteria and(UserAttributeDTO... attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.AND);
      return retVal;
    }

    public static UserAttributeCriteria and(
      Collection<UserAttributeDTO> attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.AND);
      return retVal;
    }

    public static UserAttributeCriteria or(UserAttributeDTO... attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.OR);
      return retVal;
    }

    public static UserAttributeCriteria or(
      Collection<UserAttributeDTO> attributes) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttributes(attributes);
      retVal.setType(Type.OR);
      return retVal;
    }

    public UserSearchCriteria build() {
      return criteria;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users
     * should be contained.
     *
     * @param ids the ids of the users
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withIdIn(Collection<String> ids) {
      criteria.setIncludeIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users
     * should not be contained.
     *
     * @param ids the ids of the users
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withIdNotIn(Collection<String> ids) {
      criteria.setExcludeIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users
     * userGroups should be contained.
     *
     * @param ids the ids of the user groups
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withGroupIdIn(Collection<String> ids) {
      criteria.setIncludeGroupIds(ids);
      return this;
    }

    /**
     * Specify a collection of IDs in which the IDs of the retrieved users
     * userGroups should not be contained.
     *
     * @param ids the ids of the user groups
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withGroupIdNotIn(Collection<String> ids) {
      criteria.setExcludeGroupIds(ids);
      return this;
    }

    /**
     * Specify a collection of statuses in which the status of the retrieved
     * users should be contained.
     *
     * @param statuses The list of statuses.
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withStatusIn(Collection<Byte> statuses) {
      criteria.setIncludeStatuses(statuses);
      return this;
    }

    /**
     * Specify a collection of statuses in which the status of the retrieved
     * users should not be contained.
     *
     * @param statuses The list of statuses.
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withStatusNotIn(
      Collection<Byte> statuses) {
      criteria.setExcludeStatuses(statuses);
      return this;
    }

    /**
     * Spacify a username for which to check
     *
     * @param username the username of the user
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withUsernameLike(String username) {
      criteria.setUsername(username);
      return this;
    }

    /**
     * Specify the attributes the retrieved users should have. This method
     * is intended to be called in conjuction with the and and or static
     * methods which are used to specify the relationship between the
     * different criteria, for example: UserSearchCriteriaBuilder.createCriteria().withAttributes(and(and(att1,
     * att2, att3), or (att4, att5), or(att6, att7)))
     *
     * @param attCriteria attributes
     * @return UserSearchCriteriaBuilder
     */
    public UserSearchCriteriaBuilder withAttributes(
      UserAttributeCriteria attCriteria) {
      criteria.setAttributeCriteria(attCriteria);
      return this;
    }

    public UserSearchCriteriaBuilder withSuperadmin(boolean superadmin) {
      criteria.setSuperadmin(superadmin);
      return this;
    }

    public UserAttributeCriteria and(UserAttributeCriteria... attCriteria) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttCriteria(attCriteria);
      retVal.setType(Type.AND);
      return retVal;
    }

    public UserAttributeCriteria or(UserAttributeCriteria... attCriteria) {
      UserAttributeCriteria retVal = new UserAttributeCriteria();
      retVal.setAttCriteria(attCriteria);
      retVal.setType(Type.OR);
      return retVal;
    }
  }

  public static class UserAttributeCriteria {

    private Type type;
    private Collection<UserAttributeDTO> attributes;
    private Collection<UserAttributeCriteria> attCriteria;
    private boolean useLike;

    private UserAttributeCriteria() {
    }

    public Type getType() {
      return type;
    }

    private void setType(Type type) {
      this.type = type;
    }

    public Collection<UserAttributeDTO> getAttributes() {
      return attributes;
    }

    private void setAttributes(Collection<UserAttributeDTO> attributes) {
      this.attributes = attributes;
    }

    private void setAttributes(UserAttributeDTO[] attributes) {
      this.attributes = Arrays.asList(attributes);
    }

    public Collection<UserAttributeCriteria> getAttCriteria() {
      return attCriteria;
    }

    private void setAttCriteria(UserAttributeCriteria[] attCriteria) {
      this.attCriteria = Arrays.asList(attCriteria);
    }

    public boolean isUseLike() {
      return useLike;
    }

    public void setUseLike(boolean useLike) {
      this.useLike = useLike;
    }

    public enum Type {
      AND, OR
    }
  }
}
