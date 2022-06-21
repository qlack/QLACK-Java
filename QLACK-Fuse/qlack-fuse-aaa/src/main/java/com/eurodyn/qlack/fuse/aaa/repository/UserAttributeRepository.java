package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.QUserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;

/**
 * A repository interface for UserAttribute.It is used to define a number of
 * crud methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface UserAttributeRepository extends
  AAARepository<UserAttribute, String> , QuerydslPredicateExecutor<UserAttribute>, UserAttributeRepositoryExt {

  /**
   * A method that retrieves the relative {@link UserAttribute} object
   *
   * @param userId the user id
   * @param name the name
   * @return the {@link UserAttribute} object
   */
  UserAttribute findByUserIdAndName(String userId, String name);

  Collection<UserAttribute> findAllByUserIdInAndName(Collection<String> userIds, String name);

}

interface UserAttributeRepositoryExt {

  /**
   * A method that retrieves all distinct values in data field from attribute filtered by name
   *
   * @param attributeName the name of attribute to search by
   * @return List with unique values (data field) from attribute filtered by name.
   */
  List<String> findDistinctDataByName(String attributeName);
}

class UserAttributeRepositoryImpl implements UserAttributeRepositoryExt{
  @PersistenceContext
  private EntityManager em;
  private static final QUserAttribute userAttribute =QUserAttribute.userAttribute;

  @Override
  public List<String> findDistinctDataByName(String attributeName) {
    return new JPAQuery<UserAttribute>(em)
            .select(userAttribute.data)
            .from(userAttribute)
            .where(userAttribute.name.eq(attributeName))
            .distinct()
            .fetch();
  }
}
