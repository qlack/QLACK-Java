package com.eurodyn.qlack.fuse.mailing.monitor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO.EMAIL_TYPE;
import com.eurodyn.qlack.fuse.mailing.exception.MailingException;
import com.eurodyn.qlack.fuse.mailing.util.MailingProperties;
import java.util.Collections;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@RunWith(MockitoJUnitRunner.class)
public class MailQueueSenderTest {

  @InjectMocks
  private MailQueueSender mailQueueSender;

  @Mock
  private MailingProperties mailingProperties;

  @Mock
  private JavaMailSenderImpl javaMailSender;

  @Mock
  private MimeMessage mimeMessage;

  @Mock
  private MailException mailException;

  @Mock
  private Map<String, String> properties;

  private InitTestValues initTestValues;

  private EmailDTO emailDTO;

  @Before
  public void init() {
    mailQueueSender = new MailQueueSender(mailingProperties, javaMailSender);
    initTestValues = new InitTestValues();
    emailDTO = initTestValues.createEmailDTO();
  }

  @Test
  public void sendTest() {
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void sendOnlyToTest() {
    emailDTO.setCcEmails(Collections.emptyList());
    emailDTO.setBccEmails(Collections.emptyList());
    emailDTO.setReplyToEmails(Collections.emptyList());
    emailDTO.setAttachments(Collections.emptyList());
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void sendOnlyCCTest() {
    emailDTO.setToEmails(Collections.emptyList());
    emailDTO.setBccEmails(Collections.emptyList());
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void sendPlainTextTest() {
    when(mailingProperties.getProperties()).thenReturn(null);
    emailDTO.setEmailType(EMAIL_TYPE.TEXT);
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void sendPlainTextWithoutAttachmentsOnlyBccTest() {
    when(mailingProperties.getProperties()).thenReturn(properties);
    when(properties.get("mail.smtp.starttls.enable")).thenReturn("true");
    emailDTO.setEmailType(EMAIL_TYPE.TEXT);
    emailDTO.setAttachments(Collections.emptyList());
    emailDTO.setToEmails(Collections.emptyList());
    emailDTO.setBccEmails(Collections.emptyList());
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test
  public void sendPlainTextWithoutAttachmentsOnlyToTest() {
    when(mailingProperties.getProperties()).thenReturn(properties);
    when(properties.get("mail.smtp.starttls.enable")).thenReturn("false");
    emailDTO.setEmailType(EMAIL_TYPE.TEXT);
    emailDTO.setAttachments(Collections.emptyList());
    emailDTO.setCcEmails(Collections.emptyList());
    emailDTO.setBccEmails(Collections.emptyList());
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test
  public void sendPlainTextWithoutAttachmentsTest() {
    emailDTO.setEmailType(EMAIL_TYPE.TEXT);
    emailDTO.setAttachments(Collections.emptyList());
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    mailQueueSender.send(emailDTO);
    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test(expected = MailingException.class)
  public void sendMailingExceptionTest() {
    emailDTO.setEmailType(EMAIL_TYPE.TEXT);
    emailDTO.setAttachments(Collections.emptyList());
    doNothing().when(javaMailSender).setDefaultEncoding(emailDTO.getCharset());
    doThrow(mailException).when(javaMailSender)
      .send(any(SimpleMailMessage.class));
    mailQueueSender.send(emailDTO);
  }
}
