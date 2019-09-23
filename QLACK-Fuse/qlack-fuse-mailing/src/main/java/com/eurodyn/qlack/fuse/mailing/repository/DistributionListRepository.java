package com.eurodyn.qlack.fuse.mailing.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.DistributionList;

/**
 * Repository interface for <tt>DistributionList</tt> entities
 *
 * @author European Dynamics SA.
 */
public interface DistributionListRepository extends MailingRepository<DistributionList, String> {

	List<DistributionList> findByName(String name);
}
