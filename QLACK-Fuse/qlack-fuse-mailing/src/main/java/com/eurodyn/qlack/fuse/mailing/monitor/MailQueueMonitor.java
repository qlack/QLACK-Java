package com.eurodyn.qlack.fuse.mailing.monitor;

import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.exception.MailingException;
import com.eurodyn.qlack.fuse.mailing.mappers.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.QEmail;
import com.eurodyn.qlack.fuse.mailing.repository.DistributionListRepository;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
import com.eurodyn.qlack.fuse.mailing.util.MailingProperties;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


/**
 * Monitor email queue.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional(noRollbackFor = { MailingException.class })
public class MailQueueMonitor {

	/**
	 * Logger reference
	 */
	private static final Logger LOGGER = Logger.getLogger(MailQueueMonitor.class.getName());

	// Service references.
	private MailQueueSender mailQueueSender;
	private MailingProperties mailingProperties;
	private EmailMapper emailMapper;

	private final EmailRepository emailRepository;
	private final DistributionListRepository distributionListRepository;

	private static QEmail qEmail = QEmail.email;

	@Autowired
	public MailQueueMonitor(MailQueueSender mailQueueSender, MailingProperties mailingProperties,
			EmailRepository emailRepository,
			DistributionListRepository distributionListRepository,
      EmailMapper emailMapper) {
		this.mailQueueSender = mailQueueSender;
		this.mailingProperties = mailingProperties;
		this.emailRepository = emailRepository;
		this.distributionListRepository = distributionListRepository;
		this.emailMapper = emailMapper;
	}

	private void send(Email email) {
		/** Create a DTO for the email about to be sent */
		EmailDTO dto = emailMapper.mapToDTOyWithRecipilents(email, true) ;

		/**
		 * Update email's tries and date sent in the database, irrespectively of the
		 * outcome of the sendig process.
		 */
		email.setTries((byte) (email.getTries() + 1));

		/** Try to send the email */
		try {
			mailQueueSender.send(dto);
			email.setDateSent(System.currentTimeMillis());

			/**
			 * If the email was sent successfully, we can update its status to Sent, so that
			 * the scheduler does not try to resend it.
			 */
			email.setStatus(EMAIL_STATUS.SENT.toString());
		} catch (MailingException ex) {
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			/** Set the reason for failure in the database */
			Throwable t = ex.getCause() != null ? ex.getCause() : ex;
			email.setServerResponse(t.getLocalizedMessage());
			email.setServerResponseDate(System.currentTimeMillis());
			/**
			 * If anything went wrong during delivery check if the maximum attempts have
			 * been reached and mark the email as Failed in that case.
			 */
			if (email.getTries() >= mailingProperties.getMaxTries()) {
				email.setStatus(EMAIL_STATUS.FAILED.toString());
			}
		}
		emailRepository.save(email);
	}

	public void sendOne(String emailId) {
		send(emailRepository.fetchById(emailId));
	}

	/**
	 * Sends email to a mail distribution lists recipients.
	 * @param emailId the email to send.
	 * @param distributionListId the mail distribution list.
	 */
	public void sendToDistributionList(String emailId, String distributionListId) {
	    Email email = emailRepository.fetchById(emailId);

	    email.setToEmails(null);
	    email.setCcEmails(null);
	    email.setBccEmails(getContactEmailsFromDistributionList(distributionListId));

	    send(email);
	}

	/**
	 * Retrieves mails from a distribution list contacts in a CSV format
	 * @param distributionListId the mail distribution list.
	 * @return distribution list contacts mails in CSV format
	 * @throws MailingException Indicating no distributionListId was provided or no recipients in the distribution list
	 */
	private String getContactEmailsFromDistributionList(String distributionListId) throws MailingException {
      if(distributionListId==null || distributionListId.isEmpty()) {
          throw new MailingException("No distribution list was provided. The email cannot be sent.");
      }

      DistributionList dlist = distributionListRepository.fetchById(distributionListId);

      return dlist.getContacts().stream()
          .map(Contact::getEmail).reduce((t, u) -> t + ", " + u).orElseThrow(() -> new MailingException(
              String.format("The distribution list \"%s\" has no recipients. Please add recipients first, then try again",
                  dlist.getName())));
  }

	/**
	 * Check for QUEUED emails and send them.
	 */
	@Scheduled(initialDelay = 30000, fixedDelay = 5000)
	public void checkAndSendQueued() {
		if (mailingProperties.isPolling()) {

			Predicate predicate = qEmail.status.eq(EMAIL_STATUS.QUEUED.toString())
					.and(qEmail.tries.lt(mailingProperties.getMaxTries()));

			List<Email> emails = emailRepository.findAll(predicate);

			LOGGER.log(Level.FINEST, "Found {0} email(s) to be sent.", emails.size());

			for (Email email : emails) {
				send(email);
			}
		}
	}

}
