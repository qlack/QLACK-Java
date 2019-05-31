package com.eurodyn.qlack.fuse.mailing.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;

public interface EmailRepository extends MailingRepository<Email, String> {

	List<Email> findByAddedOnDateAndStatus(Long date, EMAIL_STATUS... statuses);

}
