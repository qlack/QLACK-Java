package com.eurodyn.qlack.fuse.mailing.monitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.exception.MailingException;
import com.eurodyn.qlack.fuse.mailing.mapper.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.QEmail;
import com.eurodyn.qlack.fuse.mailing.repository.DistributionListRepository;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants;
import com.eurodyn.qlack.fuse.mailing.util.MailingProperties;

import java.util.HashSet;
import java.util.List;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author European Dynamics
 */
@ExtendWith(MockitoExtension.class)
public class MailQueueMonitorTest {

  @Mock
  private MailQueueMonitor mailQueueMonitor;
  @Mock
  private MailQueueSender mailQueueSender;

  final private EmailRepository emailRepository = mock(EmailRepository.class);
  final private DistributionListRepository distributionListRepository = mock(
    DistributionListRepository.class);

  final private MailingProperties mailingProperties = mock(MailingProperties.class);

  @Spy
  private EmailMapper emailMapper;

  private InitTestValues initTestValues;

  private Email email;
  private List<Email> emails;
  private EmailDTO emailDTO;
  private DistributionList distributionList;

  final private static QEmail qEmail = QEmail.email;

  private final String emailId = "49f150a5-478b-440d-b29c-ff8bf54e7015";
  private final String distributionListId = "0f9a2472-cde0-44a6-ba3d-8e609929043d";

  @BeforeEach
  public void init() {
    mailQueueMonitor = new MailQueueMonitor(mailQueueSender, mailingProperties,
      emailRepository,
      distributionListRepository, emailMapper
    );
    initTestValues = new InitTestValues();
    email = initTestValues.createEmail();
    emails = initTestValues.createEmails();
    emailDTO = initTestValues.createEmailDTO();
    distributionList = initTestValues.createDistributionList();
  }

  @Test
  public void sendOneTest() {
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    when(emailMapper.mapToDTOWithRecipients(email, true)).thenReturn(emailDTO);
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    mailQueueMonitor.sendOne(emailId);
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }
/*
  @Test
  public void sendOneCauseExceptionTest() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    when(emailMapper.mapToDTOWithRecipients(email, true)).thenReturn(emailDTO);
    doThrow(new MailingException("ex")).when(mailQueueSender).send(emailDTO);
    mailQueueMonitor.sendOne(emailId);
    assertNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.FAILED.name());
    verify(emailRepository, times(1)).save(email);
  }

  @Test
  public void sendOneCauseExceptionSecondScenarioTest() {
    email.setTries((byte) 0);
    when(mailingProperties.getMaxTries()).thenReturn((byte) 3);
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    when(emailMapper.mapToDTOWithRecipients(email, true)).thenReturn(emailDTO);
    doThrow(new MailingException("ex", new Throwable())).when(mailQueueSender)
      .send(emailDTO);
    mailQueueMonitor.sendOne(emailId);
    assertNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.QUEUED.name());
    verify(emailRepository, times(1)).save(email);
  }
*/
  @Test
  public void testSendToDistributionList() {
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    when(emailMapper.mapToDTOWithRecipients(email, true)).thenReturn(emailDTO);
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    when(distributionListRepository.fetchById(distributionListId))
      .thenReturn(distributionList);
    mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
    assertNull(email.getToEmails());
    assertNull(email.getCcEmails());
    assertNotNull(email.getBccEmails());
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }
/*
  @Test
  public void testSendToNullDistributionList() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    mailQueueMonitor.sendToDistributionList(emailId, null);
    assertThrows(MailingException.class, () -> {
      assertNull(email.getToEmails());
      assertNull(email.getCcEmails());
      assertNotNull(email.getBccEmails());
      assertNotNull(email.getDateSent());
      assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
      verify(emailRepository, times(1)).save(email);
    });
  }
*/
  @Test
  public void testSendToDistributionListWithEmptyContactsSet() {
    assertThrows(MailingException.class, () -> {
      when(emailRepository.fetchById(emailId)).thenReturn(email);
      distributionList.setContacts(new HashSet<>());
      when(distributionListRepository.fetchById(distributionListId))
              .thenReturn(distributionList);
      mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
      assertNull(email.getToEmails());
      assertNull(email.getCcEmails());
      assertNotNull(email.getBccEmails());
      assertNotNull(email.getDateSent());
      assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
      verify(emailRepository, times(1)).save(email);
    });
  }

  @Test
  public void testSendToDistributionListWithNullBodyEmail() {
    assertThrows(MailingException.class, () -> {
      when(emailRepository.fetchById(emailId)).thenReturn(email);
      distributionList.setContacts(new HashSet<>());
      when(distributionListRepository.fetchById(distributionListId))
              .thenReturn(distributionList);
      mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
      assertNull(email.getToEmails());
      assertNull(email.getCcEmails());
      assertNotNull(email.getBccEmails());
      assertNotNull(email.getDateSent());
      assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
      verify(emailRepository, times(1)).save(email);
    });
  }

  @Test
  public void testCheckAndSendQueuedWithPollingEnabled() {
    Predicate predicate = qEmail.status
      .eq(MailConstants.EMAIL_STATUS.QUEUED.toString())
      .and(qEmail.tries.lt(mailingProperties.getMaxTries()));
    for (Email e : emails) {
      when(emailMapper.mapToDTO(e)).thenReturn(emailDTO);
      when(emailMapper.mapToDTOWithRecipients(e, true)).thenReturn(emailDTO);
    }
    when(emailRepository.findAll(predicate)).thenReturn(emails);
    when(mailingProperties.isPolling()).thenReturn(true);
    mailQueueMonitor.checkAndSendQueued();
    for (Email e : emails) {
      assertNotNull(e.getDateSent());
      assertEquals(e.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
      verify(emailRepository, times(1)).save(e);
    }
  }

  @Test
  public void testCheckAndSendQueuedWithPollingDisabled() {
    when(mailingProperties.isPolling()).thenReturn(false);
    mailQueueMonitor.checkAndSendQueued();
    verify(emailRepository, times(0)).save(any());
  }

  @Test
  public void sendToDistributionListEmptyListTest() {
    assertThrows(MailingException.class, () -> {
      when(emailRepository.fetchById(emailId)).thenReturn(email);
      mailQueueMonitor.sendToDistributionList(emailId, "");
    });
  }
}
