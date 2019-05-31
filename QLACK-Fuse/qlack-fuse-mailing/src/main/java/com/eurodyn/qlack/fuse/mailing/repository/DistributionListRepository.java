package com.eurodyn.qlack.fuse.mailing.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.DistributionList;

public interface DistributionListRepository extends MailingRepository<DistributionList, String> {

	List<DistributionList> findByName(String name);
}
