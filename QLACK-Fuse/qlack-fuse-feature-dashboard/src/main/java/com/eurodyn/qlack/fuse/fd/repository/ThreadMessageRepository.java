package com.eurodyn.qlack.fuse.fd.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.fd.model.ThreadMessage;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * An interface ThreadMessageRepository that is used to declare crud methods
 *
 * @author European Dynamics SA
 */
@Repository
public interface ThreadMessageRepository extends QlackBaseRepository<ThreadMessage, String> {


  /**
   * A method that retrieves a List of {@link ThreadMessage} objects
   *
   * @param predicate the predicate object
   */
  @NonNull
  List<ThreadMessage> findAll(@NonNull Predicate predicate);


  /**
   * A method that retrieves a List of {@link ThreadMessage} objects
   *
   * @param predicate the predicate object
   * @param sort a sorting object
   * @return a List of generic objects
   */
  @NonNull
  List<ThreadMessage> findAll(@NonNull Predicate predicate, @NonNull Sort sort);

}
