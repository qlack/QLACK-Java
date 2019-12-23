package com.eurodyn.qlack.fuse.settings.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for <tt>Setting</tt> entities
 *
 * @author European Dynamics SA.
 */
@Repository
public interface SettingRepository extends
  QlackBaseRepository<Setting, String> {

  /**
   * Finds and returns a list of all the eligible {@link Setting} objects
   * according to the predicate
   *
   * @param predicate the predicate that holds the querying criteria
   * @return a list of {@link Setting} objects
   */
  @Override
  @NonNull
  List<Setting> findAll(@NonNull Predicate predicate);
}
