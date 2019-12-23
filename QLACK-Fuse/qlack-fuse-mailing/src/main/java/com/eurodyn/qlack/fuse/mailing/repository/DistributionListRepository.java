package com.eurodyn.qlack.fuse.mailing.repository;

import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import java.util.List;

/**
 * Repository interface for <tt>DistributionList</tt> entities
 *
 * @author European Dynamics SA.
 */
public interface DistributionListRepository extends
  MailingRepository<DistributionList, String> {

  List<DistributionList> findByName(String name);
}
