package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Application;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * An interface Repository provides abstract methods for crud operations
 *
 * @author European Dynamics SA
 */
@Repository
public interface ApplicationRepository extends QlackBaseRepository<Application, String> {

  /**
   * An abstract declaration method that is used to retrieve the symbolic name
   *
   * @param symbolicName the symbolic name
   * @return the symbolic name according to its name
   */
  List<Application> findBySymbolicName(String symbolicName);
}
