package com.eurodyn.qlack.fuse.mailing.monitor;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.exception.MailingException;
import com.eurodyn.qlack.fuse.mailing.mappers.EmailMapper;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.QEmail;
import com.eurodyn.qlack.fuse.mailing.repository.DistributionListRepository;
import com.eurodyn.qlack.fuse.mailing.repository.EmailRepository;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants;
import com.eurodyn.qlack.fuse.mailing.util.MailingProperties;
import com.querydsl.core.types.Predicate;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class MailQueueMonitorTest {

  @Mock
  private MailQueueMonitor mailQueueMonitor;
  @Mock
  private MailQueueSender mailQueueSender;

  private EmailRepository emailRepository = mock(EmailRepository.class);
  private DistributionListRepository distributionListRepository = mock(DistributionListRepository.class);

  private MailingProperties mailingProperties = mock(MailingProperties.class);

  @Spy
  private EmailMapper emailMapper;

  private InitTestValues initTestValues;

  private Email email;
  private List<Email> emails;
  private EmailDTO emailDTO;
  private DistributionList distributionList;

  private static QEmail qEmail = QEmail.email;

  private final String emailId = "49f150a5-478b-440d-b29c-ff8bf54e7015";
  private final String distributionListId = "0f9a2472-cde0-44a6-ba3d-8e609929043d";

  @Before
  public void init() {
    mailQueueMonitor = new MailQueueMonitor(mailQueueSender, mailingProperties, emailRepository,
                                            distributionListRepository, emailMapper
    );
    initTestValues = new InitTestValues();
    email = initTestValues.createEmail();
    emails = initTestValues.createEmails();
    emailDTO = initTestValues.createEmailDTO();
    distributionList = initTestValues.createDistributionList();
  }

  @Test
  public void testSendOne() {
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    when(emailMapper.mapToDTOyWithRecipilents(email, true)).thenReturn(emailDTO);
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    mailQueueMonitor.sendOne(emailId);
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }

  @Test
  public void testSendToDistributionList() {
    when(emailMapper.mapToDTO(email)).thenReturn(emailDTO);
    when(emailMapper.mapToDTOyWithRecipilents(email, true)).thenReturn(emailDTO);
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    when(distributionListRepository.fetchById(distributionListId)).thenReturn(distributionList);
    mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
    assertNull(email.getToEmails());
    assertNull(email.getCcEmails());
    assertNotNull(email.getBccEmails());
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }

  @Test(expected = MailingException.class)
  public void testSendToNullDistributionList() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    mailQueueMonitor.sendToDistributionList(emailId, null);
    assertNull(email.getToEmails());
    assertNull(email.getCcEmails());
    assertNotNull(email.getBccEmails());
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }

  @Test(expected = MailingException.class)
  public void testSendToDistributionListWithEmptyContactsSet() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    distributionList.setContacts(new HashSet<>());
    when(distributionListRepository.fetchById(distributionListId)).thenReturn(distributionList);
    mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
    assertNull(email.getToEmails());
    assertNull(email.getCcEmails());
    assertNotNull(email.getBccEmails());
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }

  @Test(expected = MailingException.class)
  public void testSendToDistributionListWithNullBodyEmail() {
    when(emailRepository.fetchById(emailId)).thenReturn(email);
    distributionList.setContacts(new HashSet<>());
    when(distributionListRepository.fetchById(distributionListId)).thenReturn(distributionList);
    mailQueueMonitor.sendToDistributionList(emailId, distributionListId);
    assertNull(email.getToEmails());
    assertNull(email.getCcEmails());
    assertNotNull(email.getBccEmails());
    assertNotNull(email.getDateSent());
    assertEquals(email.getStatus(), MailConstants.EMAIL_STATUS.SENT.name());
    verify(emailRepository, times(1)).save(email);
  }

  @Test
  public void testCheckAndSendQueuedWithPollingEnabled() {
    Predicate predicate = qEmail.status.eq(MailConstants.EMAIL_STATUS.QUEUED.toString())
                                       .and(qEmail.tries.lt(mailingProperties.getMaxTries()));
    for (Email e : emails) {
      when(emailMapper.mapToDTO(e)).thenReturn(emailDTO);
      when(emailMapper.mapToDTOyWithRecipilents(e, true)).thenReturn(emailDTO);
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
}
