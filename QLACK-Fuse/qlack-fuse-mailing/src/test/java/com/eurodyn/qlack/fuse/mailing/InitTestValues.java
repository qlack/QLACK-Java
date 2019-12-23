package com.eurodyn.qlack.fuse.mailing;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO.EMAIL_TYPE;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author European Dynamics
 */
public class InitTestValues {

  /**
   * This byte array stands for an attachment actual data for testing
   * purposes
   */
  byte[] data = {80, 65, 78, 75, 65, 74};
  @Autowired
  private JavaMailSender emailSender;

  public Attachment createAttachment() {
    Attachment attachment = new Attachment();

    attachment.setId("0f9a2472-cde0-44a6-ba3d-8e60992904fb");
    attachment.setFilename("pic.jpg");
    attachment.setContentType("image/jpeg");
    attachment.setData(Arrays.copyOf(data, data.length));

    return attachment;
  }

  public AttachmentDTO createAttachmentDTO() {
    AttachmentDTO attachmentDTO = new AttachmentDTO();

    attachmentDTO.setId("0f9a2472-cde0-44a6-ba3d-8e60992904fb");
    attachmentDTO.setFilename("pic.jpg");
    attachmentDTO.setContentType("image/jpeg");
    attachmentDTO.setData(Arrays.copyOf(data, data.length));

    return attachmentDTO;
  }

  public Set<Attachment> createAttachments() {
    Set<Attachment> attachments = new HashSet<>();
    attachments.add(createAttachment());
    return attachments;
  }

  public List<AttachmentDTO> createAttachmentsDTO() {
    List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
    attachmentDTOs.add(createAttachmentDTO());
    return attachmentDTOs;
  }

  public Contact createContact() {
    Contact contact = new Contact();

    contact.setId("0f9a2472-cde0-44a6-ba3d-8e609929043d");
    contact.setFirstName("European");
    contact.setLastName("Dynamics");
    contact.setEmail("test@eurodyn.com");
    contact.setLocale("el");

    return contact;
  }

  public Contact createContact2() {
    Contact contact = new Contact();

    contact.setId("0f932472-cde0-44a6-ba3d-8e6099234043d");
    contact.setFirstName("ED");
    contact.setLastName("USer");
    contact.setEmail("eduser@eurodyn.com");
    contact.setLocale("el");

    return contact;
  }

  public ContactDTO createContactDTO() {
    ContactDTO contactDTO = new ContactDTO();

    contactDTO.setId("0f9a2472-cde0-44a6-ba3d-8e609929043d");
    contactDTO.setFirstName("European");
    contactDTO.setLastName("Dynamics");
    contactDTO.setEmail("test@eurodyn.com");
    contactDTO.setLocale("el");

    return contactDTO;
  }

  public Set<Contact> createContacts() {
    Set<Contact> contacts = new HashSet<>();
    contacts.add(createContact());
    contacts.add(createContact2());

    return contacts;
  }

  public List<ContactDTO> createContactsDTO() {
    List<ContactDTO> contactDTOs = new ArrayList<>();
    contactDTOs.add(createContactDTO());
    return contactDTOs;
  }

  public DistributionList createDistributionList() {
    DistributionList distributionList = new DistributionList();
    distributionList.setId("49f150a5-478b-440d-b29c-ff8bf54e7015");
    distributionList.setName("Qlack Test Distribution List");
    distributionList.setDescription("Distribution list for Qlack users");
    distributionList.setCreatedBy(" ");
    distributionList.setCreatedOn(new Long("2121545432165"));
    distributionList.setContacts(createContacts());

    return distributionList;
  }

  public DistributionListDTO createDistributionListDTO() {
    DistributionListDTO distributionList = new DistributionListDTO();
    distributionList.setId("49f150a5-478b-440d-b29c-ff8bf54e7015");
    distributionList.setName("Qlack Test Distribution List");
    distributionList.setDescription("Distribution list for Qlack users");
    distributionList.setCreatedBy(" ");
    distributionList.setCreatedOn(new Long("2121545432165"));
    distributionList.setContacts(createContactsDTO());

    return distributionList;
  }

  public List<DistributionList> createDistributionLists() {
    List<DistributionList> dList = new ArrayList<>();
    dList.add(createDistributionList());

    return dList;
  }

  public List<DistributionListDTO> createDistributionListsDTO() {
    List<DistributionListDTO> dListDTO = new ArrayList<>();
    dListDTO.add(createDistributionListDTO());

    return dListDTO;
  }

  public Email createEmail() {

    Email email = new Email();

    email.setId("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
    email.setToEmails("test@eurodyn.com, test2@eurodyn.com");
    email.setCcEmails("test3@eurodyn.com, test4@eurodyn.com");
    email.setBccEmails("test5@eurodyn.com, test6@eurodyn.com");
    email.setReplyToEmails("test@eurodyn.com, test2@eurodyn.com");
    email.setAddedOnDate(new Long("2121545432165"));
    email.setAttachments(createAttachments());
    email.setBody("This a test body");
    email.setEmailType(EMAIL_TYPE.HTML.name());
    email.setSubject("This is a test subject");
    email.setTries(new Byte("1"));
    email.setStatus(EMAIL_STATUS.QUEUED.name());
    email.setServerResponse("204");
    email.setServerResponseDate(new Long("2121545432165"));

    return email;
  }

  public EmailDTO createEmailDTO() {
    EmailDTO emailDTO = new EmailDTO();
    List<String> toEmails = Arrays
      .asList("test@eurodyn.com", "test2@eurodyn.com");
    List<String> ccEmails = Arrays
      .asList("test3@eurodyn.com", "test4@eurodyn.com");
    List<String> bccEmails = Arrays
      .asList("test4@eurodyn.com", "test5@eurodyn.com");
    List<String> replyToEmails = Arrays
      .asList("test@eurodyn.com", "test2@eurodyn.com");

    emailDTO.setId("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
    emailDTO.setFromEmail("from@eurodyn.com");
    emailDTO.setToEmails(toEmails);
    emailDTO.setCcEmails(ccEmails);
    emailDTO.setBccEmails(bccEmails);
    emailDTO.setReplyToEmails(replyToEmails);
    emailDTO.setDateSent(new Long("2121545432165"));
    emailDTO.setAttachments(createAttachmentsDTO());
    emailDTO.setBody("This a test body");
    emailDTO.setEmailType(EMAIL_TYPE.HTML);
    emailDTO.setSubject("This is a test subject");
    emailDTO.setStatus(EMAIL_STATUS.QUEUED.name());
    emailDTO.setServerResponse("204");
    emailDTO.setServerResponseDate(Calendar.getInstance().getTime());

    return emailDTO;

  }

  public List<Email> createEmails() {
    List<Email> emails = new ArrayList<>();
    emails.add(createEmail());
    return emails;
  }

  public List<EmailDTO> createEmailsDTO() {
    List<EmailDTO> emailDTOs = new ArrayList<>();
    emailDTOs.add(createEmailDTO());
    return emailDTOs;
  }


  public InternalAttachment createInternalAttachment() {

    InternalAttachment attachment = new InternalAttachment();
    byte[] data = {80, 65, 78, 75, 65, 74};

    attachment.setId("0f9a2472-cde0-44a6-ba3d-8e60992904fb");
    attachment.setFilename("pic.jpg");
    attachment.setContentType("image/jpeg");
    attachment.setData(Arrays.copyOf(data, data.length));

    return attachment;
  }

  public InternalAttachment createInternalAttachmentWIthMessages() {

    InternalAttachment attachment = new InternalAttachment();
    byte[] data = {80, 65, 78, 75, 65, 74};

    attachment.setId("0f9a2472-cde0-44a6-ba3d-8e60992904fb");
    attachment.setFilename("pic.jpg");
    attachment.setContentType("image/jpeg");
    attachment.setData(Arrays.copyOf(data, data.length));
    attachment.setMessages(createInternalMessage());

    return attachment;
  }

  public InternalAttachmentDTO createFwdInternalAttachmentDTO() {
    InternalAttachmentDTO internalAttachmentDTO = new InternalAttachmentDTO();
    byte[] data = {80, 65, 78, 75, 65, 74};

    internalAttachmentDTO.setId("0f9a2472-cde0-44a6-ba3d-8e60992904fb");
    internalAttachmentDTO.setFilename("pic.jpg");
    internalAttachmentDTO.setContentType("image/jpeg");
    internalAttachmentDTO.setData(Arrays.copyOf(data, data.length));

    return internalAttachmentDTO;
  }

  public Set<InternalAttachment> createInternalAttachments() {
    Set<InternalAttachment> internalAttachments = new HashSet<>();
    internalAttachments.add(createInternalAttachment());

    return internalAttachments;
  }

  public Set<InternalAttachment> createInternalAttachmentsWithMessages() {
    Set<InternalAttachment> internalAttachments = new HashSet<>();
    internalAttachments.add(createInternalAttachmentWIthMessages());

    return internalAttachments;
  }

  public List<InternalAttachmentDTO> createInternalAttachmentsDTO() {

    List<InternalAttachmentDTO> internalAttachmentsDTO = new ArrayList<>();
    internalAttachmentsDTO.add(createFwdInternalAttachmentDTO());

    return internalAttachmentsDTO;
  }

  public InternalMessage createInternalMessage() {
    InternalMessage internalMessage = new InternalMessage();
    internalMessage.setId("7087e822-c559-42a1-a328-598d490440de");
    internalMessage.setSubject("Test Subject");
    internalMessage.setStatus(EMAIL_STATUS.QUEUED.name());
    internalMessage.setMessage("This is a test internal message");
    internalMessage.setMailFrom("test@eurodyn.com");
    internalMessage.setMailTo("test2@eurodyn.com");
    internalMessage.setAttachments(createInternalAttachments());
    internalMessage.setDateReceived(new Long("2121545432165"));
    internalMessage.setDateSent(new Long("2121545432165"));
    internalMessage.setDeleteType("I");

    return internalMessage;
  }

  public InternalMessage createInternalMessageWithMessages() {
    InternalMessage internalMessage = new InternalMessage();
    internalMessage.setId("7087e822-c559-42a1-a328-598d490440de");
    internalMessage.setSubject("Test Subject");
    internalMessage.setStatus(EMAIL_STATUS.QUEUED.name());
    internalMessage.setMessage("This is a test internal message");
    internalMessage.setMailFrom("test@eurodyn.com");
    internalMessage.setMailTo("test2@eurodyn.com");
    internalMessage.setAttachments(createInternalAttachmentsWithMessages());
    internalMessage.setDateReceived(new Long("2121545432165"));
    internalMessage.setDateSent(new Long("2121545432165"));
    internalMessage.setDeleteType("I");

    return internalMessage;
  }

  public InternalMessageDTO createInternalMessageDTO() {
    InternalMessageDTO internalMessageDTO = new InternalMessageDTO();
    internalMessageDTO.setId("7087e822-c559-42a1-a328-598d490440de");
    internalMessageDTO.setSubject("Test Subject");
    internalMessageDTO.setStatus(EMAIL_STATUS.QUEUED.name());
    internalMessageDTO.setMessage("This is a test internal message");
    internalMessageDTO.setMailFrom("test@eurodyn.com");
    internalMessageDTO.setMailTo("test2@eurodyn.com");
    internalMessageDTO.setAttachments(createInternalAttachmentsDTO());
    internalMessageDTO.setDateReceived(new Long("2121545432165"));
    internalMessageDTO.setDateSent(new Long("2121545432165"));
    internalMessageDTO.setDeleteType("I");

    return internalMessageDTO;
  }

  public List<InternalMessage> createInternalMessages() {
    List<InternalMessage> internalMessages = new ArrayList<>();
    internalMessages.add(createInternalMessage());
    return internalMessages;
  }

  public List<InternalMessage> createInternalMessagesWithMessages() {
    List<InternalMessage> internalMessages = new ArrayList<>();
    internalMessages.add(createInternalMessageWithMessages());
    return internalMessages;
  }

  public List<InternalMessageDTO> createInternalMessagesDTO() {
    List<InternalMessageDTO> internalMessagesDTO = new ArrayList<>();
    internalMessagesDTO.add(createInternalMessageDTO());
    return internalMessagesDTO;
  }

  public InternalAttachmentDTO createInternalAttachmentDTOBasedWithForwardId() {
    InternalAttachmentDTO internalAttachmentDTO = new InternalAttachmentDTO();
    byte[] data = {80, 65, 78, 75, 11, 99};

    internalAttachmentDTO.setId("0f9a2472-cde0-44a6-ba3d-9e60492902fb");
    internalAttachmentDTO.setFilename("document.pdf");
    internalAttachmentDTO.setContentType("application/pdf");
    internalAttachmentDTO.setData(Arrays.copyOf(data, data.length));

    return internalAttachmentDTO;
  }

  public InternalAttachment createFwdInternalAttachment() {
    InternalAttachment internalAttachment = new InternalAttachment();
    byte[] data = {80, 65, 78, 75, 11, 99};

    internalAttachment.setId("0f9a2472-cde0-44a6-ba3d-9e60492902fb");
    internalAttachment.setFilename("document.pdf");
    internalAttachment.setContentType("application/pdf");
    internalAttachment.setData(Arrays.copyOf(data, data.length));

    return internalAttachment;
  }

  public MimeMessage createMimeMessage() {
    return emailSender.createMimeMessage();
  }

  public MimeMessageHelper createMimeMessageHelper() {
    MimeMessageHelper mimeMessageHelper = null;
    try {
      mimeMessageHelper = new MimeMessageHelper(createMimeMessage(), true);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    return mimeMessageHelper;
  }
}
