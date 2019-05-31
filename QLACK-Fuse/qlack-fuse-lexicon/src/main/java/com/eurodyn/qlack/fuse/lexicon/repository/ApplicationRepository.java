package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Application;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends QlackBaseRepository<Application, String> {

  List<Application> findBySymbolicName(String symbolicName);
}
