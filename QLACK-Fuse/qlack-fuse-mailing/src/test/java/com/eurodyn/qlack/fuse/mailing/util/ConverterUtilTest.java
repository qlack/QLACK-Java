package com.eurodyn.qlack.fuse.mailing.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConverterUtilTest {

  @InjectMocks
  private ConverterUtil converterUtil;

  private DistributionList distributionList;
  private DistributionListDTO distributionListDTO;
  private ContactDTO contactDTO;
  private InternalMessageDTO internalMessageDTO;
  private InternalMessage internalMessage;
  private Email email;
  private InternalAttachmentDTO internalAttachmentDTO;
  private Set<InternalAttachment> internalAttachmentWithMessagesSet;
  private List<InternalMessage> internalMessageListWithMessages;

  private MailConstants mailConstants;

  @Before
  public void init() {
    InitTestValues initTestValues = new InitTestValues();
    distributionList = initTestValues.createDistributionList();
    distributionListDTO = initTestValues.createDistributionListDTO();
    contactDTO = initTestValues.createContactDTO();
    internalMessageDTO = initTestValues.createInternalMessageDTO();
    internalMessage = initTestValues.createInternalMessage();
    email = initTestValues.createEmail();
    internalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();
    internalAttachmentWithMessagesSet = initTestValues.createInternalAttachmentsWithMessages();
    internalMessageListWithMessages = initTestValues.createInternalMessagesWithMessages();
    mailConstants = new MailConstants();
  }

  @Test
  public void dlistConvertNullTest() {
    DistributionList distributionList = null;
    DistributionListDTO result =
        ConverterUtil.dlistConvert(distributionList);
    assertNull(result);
  }

  @Test
  public void dlistConvertTest() {
    DistributionListDTO result =
        ConverterUtil.dlistConvert(distributionList);
    assertEquals(distributionList.getId(), result.getId());
  }

  @Test
  public void dlistConvertDTONullTest() {
    DistributionListDTO distributionListDTO = null;
    DistributionList result =
        ConverterUtil.dlistConvert(distributionListDTO);
    assertNull(result);
  }

  @Test
  public void dlistConvertDTOTest() {
    DistributionList result =
        ConverterUtil.dlistConvert(distributionListDTO);
    assertEquals(distributionListDTO.getName(), result.getName());
  }

  @Test
  public void contactConvertTest() {
    Contact result = ConverterUtil.contactConvert(contactDTO);
    assertEquals(contactDTO.getUserID(), result.getUserId());
  }

  @Test
  public void createRecepientListNullTest() {
    contactDTO.setEmail("");
    List<String> result = ConverterUtil.createRecepientlist(contactDTO.getEmail());
    assertNull(result);
  }

  @Test
  public void createRecepientListTest() {
    String[] expected = email.getToEmails().split(",");
    List<String> result = ConverterUtil.createRecepientlist(email.getToEmails());
    assertEquals(Arrays.asList(expected).size(), result.size());
  }

  @Test
  public void internalMessageConvertNullTest() {
    InternalMessageDTO internalMessageDTO = null;
    Object result = ConverterUtil.internalMessageConvert(internalMessageDTO);
    assertNull(result);
  }

  @Test
  public void internalMessageConvertDTONullTest() {
    InternalMessage internalMessage = null;
    Object result = ConverterUtil.internalMessageConvert(internalMessage);
    assertNull(result);
  }

  @Test
  public void internalMessageConvertTest() {
    InternalMessage result = ConverterUtil.internalMessageConvert(internalMessageDTO);
    assertEquals(internalMessageDTO.getMessage(), result.getMessage());
  }

  @Test
  public void internalMessageConvertDTOTest() {
    internalMessage.setAttachments(internalAttachmentWithMessagesSet);
    InternalMessageDTO result = ConverterUtil.internalMessageConvert(internalMessage);
    assertEquals(internalMessage.getMessage(), result.getMessage());
  }

  @Test
  public void internalAttachmentConvertNullTest() {
    InternalAttachmentDTO internalAttachmentDTO = null;
    Object result = ConverterUtil.internalAttachmentConvert(internalAttachmentDTO);
    assertNull(result);
  }

  @Test
  public void internalAttachmentConvertDTONullTest() {
    InternalAttachment internalAttachment = null;
    InternalAttachmentDTO result = ConverterUtil.internalAttachmentConvert(internalAttachment);
    assertNull(result);
  }

  @Test
  public void internalAttachmentConvertTest() {
    InternalAttachment result = ConverterUtil.internalAttachmentConvert(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getFilename(), result.getFilename());
  }

  @Test
  public void internalMessageConvertListTest() {
    List<InternalMessageDTO> result = ConverterUtil.internalMessageConvertList(
        internalMessageListWithMessages);
    assertEquals(internalMessageListWithMessages.size(), result.size());
  }

  @Test
  public void emailConvertNullTest() {
    Email email = null;
    EmailDTO result = ConverterUtil.emailConvert(email);
    assertNull(result);
  }

  @Test
  public void emailConvertTest() {
    EmailDTO result = ConverterUtil.emailConvert(email);
    assertEquals(email.getId(), result.getId());
  }

  @Test
  public void internalMessageConvertNullAttachmentsTest() {
    internalMessage.setAttachments(null);
    InternalMessageDTO result = ConverterUtil.internalMessageConvert(internalMessage);
    assertEquals(internalMessage.getMessage(), result.getMessage());
  }

  @Test
  public void internalMessageConvertEmptyAttachmentsTest() {
    internalMessage.setAttachments(Collections.emptySet());
    InternalMessageDTO result = ConverterUtil.internalMessageConvert(internalMessage);
    assertEquals(internalMessage.getMessage(), result.getMessage());
  }
}
