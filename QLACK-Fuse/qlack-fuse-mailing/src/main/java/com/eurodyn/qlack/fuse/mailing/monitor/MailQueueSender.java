package com.eurodyn.qlack.fuse.mailing.monitor;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO.EMAIL_TYPE;
import com.eurodyn.qlack.fuse.mailing.exception.MailingException;
import com.eurodyn.qlack.fuse.mailing.util.MailingProperties;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Sender email queue.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional(noRollbackFor = {MailingException.class})
public class MailQueueSender {

  // Logger reference
  private static final Logger LOGGER = Logger.getLogger(MailQueueSender.class.getName());

  private static final String EMAIL_NOTIFICATION = "Sending email {0} to {1} with TLS={2}.";

  // Service refs.
  private MailingProperties mailingProperties;
  private JavaMailSenderImpl emailSender;

  @Autowired
  public MailQueueSender(MailingProperties mailingProperties, JavaMailSenderImpl emailSender) {
    this.mailingProperties = mailingProperties;
    this.emailSender = emailSender;
  }

  /**
   * Setup commons attributes of the email
   *
   * @param email The Email to set the attributes to.
   * @param vo The DTO with the attributes to set.
   * @throws MessagingException Indicating an error while setting recipients.
   */
  private void setupCommons(MimeMessageHelper email, @Valid EmailDTO vo) throws MessagingException {

    email.setFrom(vo.getFromEmail());
    email.setSubject(vo.getSubject());
    email.setSentDate(new Date());

    if (CollectionUtils.isNotEmpty(vo.getToEmails())) {
      for (String recipient : vo.getToEmails()) {
        email.addTo(recipient);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getCcEmails())) {
      for (String recipient : vo.getCcEmails()) {
        email.addCc(recipient);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getBccEmails())) {
      for (String recipient : vo.getBccEmails()) {
        email.addBcc(recipient);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getReplyToEmails())) {
      for (String recipient : vo.getReplyToEmails()) {
        email.setReplyTo(recipient);
      }
    }
  }

  /**
   * Attaches email attachments.
   *
   * @param email The email to attach the attachments to.
   * @param vo The DTO with the attachments to attach.
   * @throws MessagingException Indicating an error while processing attachments.
   */
  private void setupAttachments(MimeMessageHelper email, EmailDTO vo) throws MessagingException {
    if (!CollectionUtils.isEmpty(vo.getAttachments())) {
      for (AttachmentDTO attachmentDTO : vo.getAttachments()) {
        DataSource source = new ByteArrayDataSource(attachmentDTO.getData(),
            attachmentDTO.getContentType());
        email.addAttachment(attachmentDTO.getFilename(), source);
      }
    }
  }

  /**
   * Send an Email.
   *
   * @param vo the email to be send
   */
  public void send(EmailDTO vo) {

    try {
      //set email encoding
      emailSender.setDefaultEncoding(vo.getCharset());

      if (vo.getEmailType().equals(EMAIL_TYPE.HTML)) { // HTML email
        prepareAndSendMimeMessage(emailSender.createMimeMessage(), vo, true);
      } else {  // Plaintext email
        if (!CollectionUtils.isEmpty(vo.getAttachments())) {
          prepareAndSendMimeMessage(emailSender.createMimeMessage(), vo, false);
        } else {
          SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
          simpleMailMessage.setText(vo.getBody());
          setupSimpleMailCommons(simpleMailMessage, vo);

          LOGGER.log(Level.FINEST, EMAIL_NOTIFICATION, new Object[]{
              vo.getSubject(), Arrays.asList(vo.getToEmails()), isTlsEnabled()
          });

          //Send simple mail
          emailSender.send(simpleMailMessage);
        }
      }

      LOGGER.log(Level.FINEST, EMAIL_NOTIFICATION, new Object[]{
          vo.getSubject(), Arrays.asList(vo.getToEmails()), isTlsEnabled()
      });

    } catch (Exception e) {
      throw new MailingException("There was a problem sending email.", e);
    }
  }

  /**
   * Setup commons attributes of the simple email.
   *
   * @param simpleMailMessage The Email to set the attributes to.
   * @param vo The DTO with the attributes to set.
   */
  private void setupSimpleMailCommons(SimpleMailMessage simpleMailMessage, @Valid EmailDTO vo) {
    simpleMailMessage.setFrom(vo.getFromEmail());
    simpleMailMessage.setSubject(vo.getSubject());
    simpleMailMessage.setSentDate(new Date());

    if (CollectionUtils.isNotEmpty(vo.getToEmails())) {
      for (String toEmail : vo.getToEmails()) {
        simpleMailMessage.setTo(toEmail);
      }
    }

    if (CollectionUtils.isNotEmpty(vo.getCcEmails())) {
      for (String ccEmail : vo.getCcEmails()) {
        simpleMailMessage.setCc(ccEmail);
      }
    }
    if (CollectionUtils.isNotEmpty(vo.getBccEmails())) {
      for (String bccEmail : vo.getBccEmails()) {
        simpleMailMessage.setBcc(bccEmail);
      }
    }
  }

  /***
   *
   * @param message The MimeMessage object
   * @param vo The DTO with email properties.
   * @param isHtmlEmail Whether the email body content is html
   * @throws MessagingException Indicating an error while processing attachments, recipients etc.
   */
  public void prepareAndSendMimeMessage(MimeMessage message, EmailDTO vo, boolean isHtmlEmail)
      throws MessagingException {
    MimeMessageHelper email = new MimeMessageHelper(message, true);
    setupAttachments(email, vo);
    email.setText(vo.getBody(), isHtmlEmail);
    setupCommons(email, vo);

    LOGGER.log(Level.FINEST, EMAIL_NOTIFICATION, new Object[]{
        vo.getSubject(), Arrays.asList(vo.getToEmails()), isTlsEnabled()
    });

    emailSender.send(message);
  }

  /**
   * Checks if TLS protocol is enabled for the STMP mail server
   *
   * @return true if TLS is enabled, false otherwise
   */
  private boolean isTlsEnabled() {
    return (this.mailingProperties.getProperties() != null
        && !this.mailingProperties.getProperties().isEmpty())
        && this.mailingProperties.getProperties().get("mail.smtp.starttls.enable").equals("true");
  }
}
