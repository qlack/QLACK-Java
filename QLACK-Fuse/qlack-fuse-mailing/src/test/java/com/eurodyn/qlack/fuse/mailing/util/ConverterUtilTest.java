package com.eurodyn.qlack.fuse.mailing.util;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ConverterUtilTest {

  @InjectMocks private ConverterUtil converterUtil;

  private DistributionList distributionList;
  private DistributionListDTO distributionListDTO;
  private ContactDTO contactDTO;
  private InternalMessageDTO internalMessageDTO;
  private InternalMessage internalMessage;
  private Email email;
  private InternalAttachmentDTO internalAttachmentDTO;
  private Set<InternalAttachment> internalAttachmentWithMessagesSet;
  private List<InternalMessage> internalMessageListWithMessages;

  @Before
  public void init(){
    InitTestValues initTestValues = new InitTestValues();
    distributionList = initTestValues.createDistributionList();
    distributionListDTO = initTestValues.createDistributionListDTO();
    contactDTO = initTestValues.createContactDTO();
    internalMessageDTO = initTestValues.createInternalMessageDTO();
    internalMessage = initTestValues.createInternalMessage();
    email = initTestValues.createEmail();
    internalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();
    internalAttachmentWithMessagesSet = initTestValues.createInternalAttachmentsWithMessages();
    internalMessageListWithMessages =  initTestValues.createInternalMessagesWithMessages();
  }

  @Test
  public void dlistConvertNullTest(){
    DistributionList distributionList = null;
    DistributionListDTO result =
        converterUtil.dlistConvert(distributionList);
    assertNull(result);
  }

  @Test
  public void dlistConvertTest(){
    DistributionListDTO result =
        converterUtil.dlistConvert(distributionList);
    assertEquals(distributionList.getId(), result.getId());
  }

  @Test
  public void dlistConvertDTONullTest(){
    DistributionListDTO distributionListDTO = null;
    DistributionList result =
        converterUtil.dlistConvert(distributionListDTO);
    assertNull(result);
  }

  @Test
  public void dlistConvertDTOTest(){
    DistributionList result =
        converterUtil.dlistConvert(distributionListDTO);
    assertEquals(distributionListDTO.getName(), result.getName());
  }

  @Test
  public void contactConvertTest(){
    Contact result = converterUtil.contactConvert(contactDTO);
    assertEquals(contactDTO.getUserID(), result.getUserId());
  }

  @Test
  public void createRecepientListNullTest(){
    contactDTO.setEmail("");
    List<String> result = converterUtil.createRecepientlist(contactDTO.getEmail());
    assertNull(result);
  }

  @Test
  public void createRecepientListTest(){
    String[] expected = email.getToEmails().split(",");
        List<String> result = converterUtil.createRecepientlist(email.getToEmails());
    assertEquals(Arrays.asList(expected).size(), result.size());
  }

  @Test
  public void internalMessageConvertNullTest(){
    InternalMessageDTO internalMessageDTO = null;
    Object result = converterUtil.internalMessageConvert(internalMessageDTO);
    assertNull(result);
  }

  @Test
  public void internalMessageConvertDTONullTest(){
    InternalMessage internalMessage = null;
    Object result = converterUtil.internalMessageConvert(internalMessage);
    assertNull(result);
  }

  @Test
  public void internalMessageConvertTest(){
    InternalMessage  result = converterUtil.internalMessageConvert(internalMessageDTO);
    assertEquals(internalMessageDTO.getMessage(), result.getMessage());
  }

  @Test
  public void internalMessageConvertDTOTest(){
    internalMessage.setAttachments(internalAttachmentWithMessagesSet);
    InternalMessageDTO  result = converterUtil.internalMessageConvert(internalMessage);
    assertEquals(internalMessage.getMessage(), result.getMessage());
  }

  @Test
  public void internalAttachmentConvertNullTest(){
    InternalAttachmentDTO internalAttachmentDTO = null;
    Object result = converterUtil.internalAttachmentConvert(internalAttachmentDTO);
    assertNull(result);
  }

  @Test
  public void internalAttachmentConvertDTONullTest(){
    InternalAttachment internalAttachment = null;
    InternalAttachmentDTO result = converterUtil.internalAttachmentConvert(internalAttachment);
    assertNull(result);
  }

  @Test
  public void internalAttachmentConvertTest(){
    InternalAttachment result = converterUtil.internalAttachmentConvert(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getFilename(), result.getFilename());
  }

  @Test
  public void internalMessageConvertListTest(){
    List<InternalMessageDTO> result = converterUtil.internalMessageConvertList(
        internalMessageListWithMessages);
    assertEquals(internalMessageListWithMessages.size(), result.size());
  }

  @Test
  public void emailConvertNullTest(){
    Email email = null;
    EmailDTO result = converterUtil.emailConvert(email);
    assertNull(result);
  }

  @Test
  public void emailConvertTest(){
    EmailDTO result = converterUtil.emailConvert(email);
    assertEquals(email.getId(), result.getId());
  }
}
