package com.eurodyn.qlack.fuse.fd.repository;


import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.fd.model.Vote;
import org.springframework.stereotype.Repository;

/**
 * An interface VoteRepository that is used to declare crud methods
 *
 * @author European Dynamics SA
 */
@Repository
public interface VoteRepository extends QlackBaseRepository<Vote, String> {

}
