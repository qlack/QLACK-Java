package com.eurodyn.qlack.fuse.workflow.repository;

import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * The Repository interface for the ProcessFile entity.
 *
 * @author European Dynamics SA
 */
@Repository
public interface ProcessFileRepository extends JpaRepository<ProcessFile, String>,
    QuerydslPredicateExecutor<ProcessFile> {

  /**
   * An abstract method that the usage of it is to retrieve the file name
   *
   * @param filename the name of the file
   * @return the file name
   */
  ProcessFile findOneByFilename(String filename);

}
