package com.eurodyn.qlack.fuse.mailing.repository;

import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
import java.util.List;

/**
 * Repository interface for <tt>Email</tt> entities
 *
 * @author European Dynamics SA.
 */
public interface EmailRepository extends MailingRepository<Email, String> {

  List<Email> findByAddedOnDateAndStatusIn(Long addedOnDate, List<String> status);

  List<Email> findByAddedOnDateBeforeAndStatusIn(Long addedOnDate, List<String> status);
}
