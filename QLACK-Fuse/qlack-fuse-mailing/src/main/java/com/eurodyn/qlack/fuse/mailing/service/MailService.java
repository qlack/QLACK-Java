package com.eurodyn.qlack.fuse.mailing.service;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.AttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.monitor.MailQueueMonitor;
import com.eurodyn.qlack.fuse.mailing.repository.AttachmentRepository;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Bean implementation for Send and Search Mail functionality
 *
 * @author European Dynamics SA.
 */
@Transactional
@Service
@Validated
public class MailService {

	private final MailQueueMonitor mailQueueMonitor;
	private final EmailRepository emailRepository;
	private final AttachmentRepository attachmentRepository;

	private EmailMapper emailMapper;
	private AttachmentMapper attachmentMapper;

	@Autowired
	public MailService(MailQueueMonitor mailQueueMonitor, EmailMapper emailMapper, EmailRepository emailRepository,
			AttachmentMapper attachmentMapper, AttachmentRepository attachmentRepository) {
		this.mailQueueMonitor = mailQueueMonitor;
		this.emailMapper = emailMapper;
		this.emailRepository = emailRepository;
		this.attachmentMapper = attachmentMapper;
		this.attachmentRepository = attachmentRepository;
	}

	/**
	 * Queue a list of Emails.
	 *
	 * @param dtos - list of email data transfer objects.
	 * @return List of email ids
	 */
	public List<String> queueEmails(List<EmailDTO> dtos) {
		List<String> emails = new ArrayList<>();
		for (EmailDTO dto : dtos) {
			emails.add(queueEmail(dto));
		}

		return emails;
	}

	/**
	 * Queue an email.
	 *
	 * @param emailDto - an email data transfer object.
	 * @return email id
	 */
	public String queueEmail(@Valid EmailDTO emailDto) {
		Email email;

		email = emailMapper.mapToEntity(emailDto);
		email.setTries((byte) 0);
		email.setStatus(EMAIL_STATUS.QUEUED.toString());
		email.setAddedOnDate(System.currentTimeMillis());
		emailRepository.save(email);

		// Process attachments.
		if (emailDto.getAttachments() != null && !emailDto.getAttachments().isEmpty()) {
			Set<Attachment> attachments = new HashSet<>();
			for (AttachmentDTO attachmentDto : emailDto.getAttachments()) {
				Attachment attachment = attachmentMapper.mapToEntity(attachmentDto);
				attachment.setEmail(email);

				attachment.setAttachmentSize(Long.valueOf(attachmentDto.getData().length));
				attachments.add(attachment);
				attachmentRepository.save(attachment);
			}
			email.setAttachments(attachments);
		}

		return email.getId();
	}

	/**
	 * Removes all e-mails prior to the specified date having the requested status.
	 * Warning: If you pass a <code>null</code> date all emails irrespectively of
	 * date will be removed.
	 *
	 * @param date - the date before which all e-mails will be removed.
	 * @param status - the status to be processed. Be cautious to not include e-mails of
	 * status QUEUED as such e-mails might not have been tried to be
	 * delivered yet.
	 */
	public void cleanup(Long date, EMAIL_STATUS[] status) {
		List<Email> emails = emailRepository.findByAddedOnDateAndStatus(date, status);
		for (Email email : emails) {
			emailRepository.delete(email);
		}
	}

	/**
	 * Delete an email from the queue.
	 *
	 * @param emailId - the email id.
	 */
	public void deleteFromQueue(String emailId) {
		emailRepository.deleteById(emailId);
	}

  /**
   * Update email status
   * @param emailId the email Id
   * @param status the new email status
   */
	public void updateStatus(String emailId, EMAIL_STATUS status) {
		Email email = emailRepository.fetchById(emailId);
		email.setStatus(status.toString());
		emailRepository.save(email);
	}

  /**
   * Get an email DTO by Id
   * @param emailId the email Id
   * @return an email DTO object
   */
	public EmailDTO getMail(String emailId) {
		Email email = emailRepository.fetchById(emailId);
		return emailMapper.mapToDTO(email);
	}

  /**
   * Get all email of the provided status
   * @param status the provided status
   * @return a list of email with the provided status
   */
	public List<EmailDTO> getByStatus(EMAIL_STATUS status) {
		return emailRepository.findByAddedOnDateAndStatus(null, status).stream().map(o ->
		emailMapper.mapToDTO(o)).collect(Collectors.toList());
	}

  /**
   * Send an email
   * @param emailId the emailId
   */
	public void sendOne(String emailId) {
		mailQueueMonitor.sendOne(emailId);
	}

	/**
	 * Sends email to a mail distribution lists recipients.
	 * @param emailId the email to send.
	 * @param distributionListId the mail distribution list.
	 */
	public void sendToDistributionList(String emailId, String distributionListId) {
		mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
	}
}
