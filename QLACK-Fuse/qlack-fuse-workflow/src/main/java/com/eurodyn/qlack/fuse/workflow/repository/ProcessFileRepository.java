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
public interface ProcessFileRepository extends JpaRepository<ProcessFile, String>, QuerydslPredicateExecutor<ProcessFile> {

  ProcessFile findOneByFilename(String filename);

}
