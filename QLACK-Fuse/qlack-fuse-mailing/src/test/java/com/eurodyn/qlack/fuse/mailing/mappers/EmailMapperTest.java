package com.eurodyn.qlack.fuse.mailing.mappers;/**
 * @author European Dynamics
 */

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.model.Email;

@RunWith(MockitoJUnitRunner.class)
public class EmailMapperTest {

  @InjectMocks
  private EmailMapperImpl emailMapperImpl;

  private InitTestValues initTestValues;
  private Email email;
  private EmailDTO emailDTO;
  private List<Email> emails;
  private List<EmailDTO> emailsDTO;
  private final String EMAIL_DELIMITER = ",";

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    email = initTestValues.createEmail();
    emailDTO = initTestValues.createEmailDTO();
    emails = initTestValues.createEmails();
    emailsDTO = initTestValues.createEmailsDTO();
  }

  @Test
  public void testMapToDTOId() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getId(), emailDTO.getId());
  }

  @Test
  public void testMapToDTOSubject() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getSubject(), emailDTO.getSubject());
  }

  @Test
  public void testMapToDTOBody() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getBody(), emailDTO.getBody());
  }

  @Test
  public void testMapToDTOFromEmail() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getFromEmail(), emailDTO.getFromEmail());
  }

  @Test
  public void testMapToDTOToEmails() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getToEmails().split(EMAIL_DELIMITER).length, emailDTO.getToEmails().size());
  }

  @Test
  public void testMapToDTOCcEmails() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getCcEmails().split(EMAIL_DELIMITER).length, emailDTO.getCcEmails().size());
  }

  @Test
  public void testMapToDTOBccEmails() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getBccEmails().split(EMAIL_DELIMITER).length, emailDTO.getBccEmails().size());
  }

  @Test
  public void testMapToDTOReplyToEmails() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getReplyToEmails().split(EMAIL_DELIMITER).length, emailDTO.getReplyToEmails().size());
  }

  @Test
  public void testMapToDTOEmailType() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getEmailType(), emailDTO.getEmailType().name());
  }

  @Test
  public void testMapToDTOStatus() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getStatus(), emailDTO.getStatus());
  }

  @Test
  public void testMapToDTOAttachments() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getAttachments().size(), emailDTO.getAttachments().size());
  }

  @Test
  public void testMapToDTODateSent() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getDateSent(), emailDTO.getDateSent());
  }

  @Test
  public void testMapToDTOServerResponse() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getServerResponse(), emailDTO.getServerResponse());
  }

  @Test
  public void testMapToDTOServerResponseDate() {
    EmailDTO emailDTO = emailMapperImpl.mapToDTO(email);
    assertEquals(email.getServerResponseDate().longValue(), emailDTO.getServerResponseDate().getTime());
  }

  @Test
  public void testMapToDTOList() {
    emailsDTO = emailMapperImpl.mapToDTO(emails);
    assertEquals(emails.size(), emailsDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getId(), email.getId());
  }

  @Test
  public void testMapToEntitySubject() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getSubject(), email.getSubject());
  }

  @Test
  public void testMapToEntityBody() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getBody(), email.getBody());
  }

  @Test
  public void testMapToEntityFromEmail() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getFromEmail(), email.getFromEmail());
  }

  @Test
  public void testMapToEntityToEmails() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getToEmails().size(), email.getToEmails().split(EMAIL_DELIMITER).length);
  }

  @Test
  public void testMapToEntityCcEmails() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getCcEmails().size(), email.getCcEmails().split(EMAIL_DELIMITER).length);
  }

  @Test
  public void testMapToEntityBccEmails() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getBccEmails().size(), email.getBccEmails().split(EMAIL_DELIMITER).length);
  }

  @Test
  public void testMapToEntityReplyToEmails() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getReplyToEmails().size(), email.getReplyToEmails().split(EMAIL_DELIMITER).length);
  }

  @Test
  public void testMapToEntityEmailType() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getEmailType().name(), email.getEmailType());
  }

  @Test
  public void testMapToEntityStatus() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getStatus(), email.getStatus());
  }

  @Test
  public void testMapToEntityAttachments() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getAttachments().size(), email.getAttachments().size());
  }

  @Test
  public void testMapToEntityDateSent() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getDateSent().getTime(), email.getDateSent().longValue());
  }

  @Test
  public void testMapToEntityServerResponse() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getServerResponse(), email.getServerResponse());
  }

  @Test
  public void testMapToEntityServerResponseDate() {
    Email email = emailMapperImpl.mapToEntity(emailDTO);
    assertEquals(emailDTO.getServerResponseDate().getTime(), email.getServerResponseDate().longValue());
  }

  @Test
  public void testMapToEntityList() {
    emails = emailMapperImpl.mapToEntity(emailsDTO);
    assertEquals(emailsDTO.size(), emails.size());
  }
}
