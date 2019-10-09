package com.eurodyn.qlack.fuse.mailing.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.AttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.monitor.MailQueueMonitor;
import com.eurodyn.qlack.fuse.mailing.repository.AttachmentRepository;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants;
import com.eurodyn.qlack.fuse.mailing.validators.EmailValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {

  @InjectMocks
  private MailService mailService;

  @Mock
  private MailQueueMonitor mailQueueMonitor;

  private EmailRepository emailRepository = mock(EmailRepository.class);
  private AttachmentRepository attachmentRepository = mock(AttachmentRepository.class);
  private EmailValidator emailValidator = new EmailValidator();

  @Spy
  private EmailMapper emailMapper;
  @Spy
  private AttachmentMapper attachmentMapper;

  private InitTestValues initTestValues;

  private Email email;
  private EmailDTO emailDTO;
  private List<Email> emails;
  private List<EmailDTO> emailsDTO;
  private Attachment attachment;
  private AttachmentDTO attachmentDTO;

  private final String emailId = "49f150a5-478b-440d-b29c-ff8bf54e7015";
  private final String emailName = "Qlack Test Distribution List";
  private final String distributionListId = "0f9a2472-cde0-44a6-ba3d-8e609929043d";

  private final long emailDate = 2121545432165L;
  private MailConstants.EMAIL_STATUS[] statuses = {MailConstants.EMAIL_STATUS.QUEUED};

  @Before
  public void init() {
    mailService = new MailService(mailQueueMonitor, emailMapper, emailRepository,
        attachmentMapper, attachmentRepository, emailValidator);
    initTestValues = new InitTestValues();
    email = initTestValues.createEmail();
    emailDTO = initTestValues.createEmailDTO();
    emails = initTestValues.createEmails();
    emailsDTO = initTestValues.createEmailsDTO();
    attachment = initTestValues.createAttachment();
    attachmentDTO = initTestValues.createAttachmentDTO();
  }

  @Test
  public void testQueueEmailWithAttachments() {
    when(emailMapper.mapToEntity(emailDTO)).thenReturn(email);

    for (AttachmentDTO attachmentDto : emailDTO.getAttachments()) {
      when(attachmentMapper.mapToEntity(attachmentDto)).thenReturn(attachment);
    }
    String queuedEmailId = mailService.queueEmail(emailDTO);

    verify(emailRepository, times(1)).save(email);
    for (AttachmentDTO attachmentDto : emailDTO.getAttachments()) {
      verify(attachmentRepository, times(1)).save(attachment);
    }
    assertEquals(emailDTO.getId(), queuedEmailId);
  }

  @Test
  public void testQueueEmailWithoutAttachments() {
    email.setTries((byte) 0);
    email.setStatus(MailConstants.EMAIL_STATUS.QUEUED.name());
    email.setAddedOnDate(System.currentTimeMillis());
    emailDTO.setAttachments(null);
    when(emailMapper.mapToEntity(emailDTO)).thenReturn(email);
    String queuedEmailId = mailService.queueEmail(emailDTO);
    verify(emailRepository, times(1)).save(email);
    assertEquals(emailDTO.getId(), queuedEmailId);
  }

  @Test
  public void testQueueEmailWithEmptyAttachments() {
    email.setTries((byte) 0);
    email.setStatus(MailConstants.EMAIL_STATUS.QUEUED.name());
    email.setAddedOnDate(System.currentTimeMillis());
    emailDTO.setAttachments(Collections.emptyList());
    when(emailMapper.mapToEntity(emailDTO)).thenReturn(email);
    String queuedEmailId = mailService.queueEmail(emailDTO);
    verify(emailRepository, times(1)).save(email);
    assertEquals(emailDTO.getId(), queuedEmailId);
  }

  @Test
  public void testQueueEmails() {
    for (EmailDTO e : emailsDTO) {
      e.setAttachments(null);
      when(emailMapper.mapToEntity(e))
          .thenReturn(emails.stream().filter(em -> em.getId().equals(e.getId())).findFirst()
              .get());
    }
    List<String> queuedEmailsIds = mailService.queueEmails(emailsDTO);
    assertEquals(emailsDTO.size(), queuedEmailsIds.size());
  }

  @Test
  public void testCleanup() {
    when(emailRepository.findByAddedOnDateAndStatus(emailDate, MailConstants.EMAIL_STATUS.QUEUED))
        .thenReturn(emails);
    mailService.cleanup(emailDate, statuses);
    for (Email email : emails) {
      verify(emailRepository, times(1)).delete(email);
    }
  }

  @Test
  public void testDeleteFromQueue() {
    mailService.deleteFromQueue(emailId);
    verify(emailRepository, times(1)).deleteById(emailId);
  }

  @Test
  public void testUpdateStatus() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    mailService.updateStatus(emailId, MailConstants.EMAIL_STATUS.SENT);
    verify(emailRepository, times(1)).save(email);
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
  }

  @Test
  public void testGetMail() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    EmailDTO actualDTO = mailService.getMail(emailId);
    assertEquals(emailDTO, actualDTO);
  }

  @Test
  public void testGetByStatus() {
    when(emailRepository.findByAddedOnDateAndStatus(null, MailConstants.EMAIL_STATUS.QUEUED))
        .thenReturn(emails);
    for (Email e : emails) {
      when(emailMapper.mapToDTO(e))
          .thenReturn(emailsDTO.stream().filter(em -> em.getId().equals(e.getId())).findFirst()
              .get());
    }
    List<EmailDTO> byStatus = mailService.getByStatus(MailConstants.EMAIL_STATUS.QUEUED);
    assertEquals(emails.size(), byStatus.size());
  }

  @Test
  public void testSendOne() {
    mailService.sendOne(emailId);
    verify(mailQueueMonitor, times(1)).sendOne(emailId);
  }

  @Test
  public void sendToDistributionList() {
    mailService.sendToDistributionList(emailId, distributionListId);
    verify(mailQueueMonitor, times(1)).sendToDistributionList(emailId, distributionListId);
  }

  @Test(expected = ValidationException.class)
  public void sendWithoutRecipients() {

    emailDTO.setToEmails(new ArrayList<>());
    emailDTO.setCcEmails(new ArrayList<>());
    emailDTO.setBccEmails(new ArrayList<>());
    mailService.queueEmail(emailDTO);
  }

}
