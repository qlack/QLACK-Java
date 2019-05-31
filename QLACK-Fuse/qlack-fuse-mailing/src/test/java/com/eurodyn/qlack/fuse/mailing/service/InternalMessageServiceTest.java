package com.eurodyn.qlack.fuse.mailing.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.InternalAttachmentMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.InternalMessageMapper;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;
import com.eurodyn.qlack.fuse.mailing.model.QInternalMessage;
import com.eurodyn.qlack.fuse.mailing.repository.InternalAttachmentRepository;
import com.eurodyn.qlack.fuse.mailing.repository.InternalMessagesRepository;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class InternalMessageServiceTest {

  @InjectMocks
  private InternalMessageService internalMessageService;

  private InternalMessagesRepository internalMessageRepository = mock(InternalMessagesRepository.class);
  private InternalAttachmentRepository internalAttachmentRepository = mock(InternalAttachmentRepository.class);

  @Spy
  private InternalMessageMapper internalMessageMapper;
  @Spy
  private InternalAttachmentMapper internalAttachmentMapper;

  private QInternalMessage qInternalMessage;
  private InitTestValues initTestValues;

  private InternalMessage internalMessage;
  private InternalMessage internalMessageWithoutAttachments;
  private InternalMessageDTO internalMessageDTO;
  private InternalMessageDTO internalMessageDTOWithoutAttachments;
  private List<InternalMessage> internalMessages;
  private List<InternalMessageDTO> internalMessagesDTO;
  private InternalAttachment internalAttachment;
  private Set<InternalAttachment> internalAttachments;
  private List<InternalAttachmentDTO> internalAttachmentsDTO;
  private InternalAttachmentDTO internalAttachmentDTO;
  private InternalAttachment fwdInternalAttachment;
  private InternalAttachmentDTO fwdInternalAttachmentDTO;

  private final String internalMessageId = "7087e822-c559-42a1-a328-598d490440de";
  private final String internalAttachmentId = "0f9a2472-cde0-44a6-ba3d-8e60992904fb";
  private final String userId = "7087e822-c559-42a1-a328-598d490440de";

  @Before
  public void init() {
    internalMessageService = new InternalMessageService(internalMessageRepository, internalAttachmentRepository,
                                                        internalMessageMapper, internalAttachmentMapper
    );
    qInternalMessage = new QInternalMessage("internalMessage");

    initTestValues = new InitTestValues();
    internalMessage = initTestValues.createInternalMessage();
    internalMessageWithoutAttachments = initTestValues.createInternalMessage();
    internalMessageWithoutAttachments.setAttachments(null);
    internalMessageDTO = initTestValues.createInternalMessageDTO();
    internalMessageDTOWithoutAttachments = initTestValues.createInternalMessageDTO();
    internalMessageDTOWithoutAttachments.setAttachments(null);
    internalMessages = initTestValues.createInternalMessages();
    internalMessagesDTO = initTestValues.createInternalMessagesDTO();
    internalAttachment = initTestValues.createInternalAttachment();
    internalAttachments = initTestValues.createInternalAttachments();
    internalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();
    internalAttachmentsDTO = initTestValues.createInternalAttachmentsDTO();
    fwdInternalAttachment = initTestValues.createFwdInternalAttachment();
    fwdInternalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();

  }


  @Test
  public void testSendInternalMailWithoutAttachment() {
    when(internalMessageMapper.mapToEntity(internalMessageDTOWithoutAttachments)).thenReturn(
      internalMessageWithoutAttachments);
    String internalMessageId = internalMessageService.sendInternalMail(internalMessageDTOWithoutAttachments);
    verify(internalMessageRepository, times(1)).save(internalMessageWithoutAttachments);
    assertEquals(internalMessageDTOWithoutAttachments.getId(), internalMessageId);
  }

  @Test
  public void testSendInternalMailWithAttachment() {
    when(internalMessageMapper.mapToEntity(internalMessageDTO)).thenReturn(internalMessage);

    int index = 0;
    for (Iterator<InternalAttachment> iter = internalMessage.getAttachments().iterator(); iter.hasNext(); ) {
      InternalAttachment internalAttachmentIter = iter.next();
      when(internalAttachmentMapper.mapToEntity(internalMessageDTO.getAttachments().get(index))).thenReturn(
        internalAttachmentIter);
      index++;
    }

    String internalMessageId = internalMessageService.sendInternalMail(internalMessageDTO);
    verify(internalMessageRepository, times(1)).save(internalMessage);
    for (Iterator<InternalAttachment> iter = internalMessage.getAttachments().iterator(); iter.hasNext(); ) {
      InternalAttachment internalAttachmentIter = iter.next();
      verify(internalAttachmentRepository, times(1)).save(internalAttachmentIter);
    }
    assertEquals(internalMessage.getId(), internalMessageId);
  }

  @Test
  public void testSendInternalMailWithForwardAttachmentId() {
    final String fwdAttachmentId = "0f9a2472-cde0-44a6-ba3d-9e60492902fb";
    when(internalAttachmentRepository.fetchById(fwdAttachmentId)).thenReturn(fwdInternalAttachment);
    when(internalAttachmentMapper.mapToDTO(fwdInternalAttachment)).thenReturn(fwdInternalAttachmentDTO);
    when(internalAttachmentMapper.mapToEntity(fwdInternalAttachmentDTO)).thenReturn(fwdInternalAttachment);

    internalMessageDTO.setFwdAttachmentId(fwdAttachmentId);
    when(internalMessageMapper.mapToEntity(internalMessageDTO)).thenReturn(internalMessage);

    int index = 0;
    for (Iterator<InternalAttachment> iter = internalMessage.getAttachments().iterator(); iter.hasNext(); ) {
      InternalAttachment internalAttachmentIter = iter.next();
      when(internalAttachmentMapper.mapToEntity(internalMessageDTO.getAttachments().get(index))).thenReturn(
        internalAttachmentIter);
      index++;
    }

    String internalMessageId = internalMessageService.sendInternalMail(internalMessageDTO);
    verify(internalMessageRepository, times(1)).save(internalMessage);
    for (Iterator<InternalAttachment> iter = internalMessage.getAttachments().iterator(); iter.hasNext(); ) {
      InternalAttachment internalAttachmentIter = iter.next();
      verify(internalAttachmentRepository, times(1)).save(internalAttachmentIter);
    }
    verify(internalAttachmentRepository, times(1)).save(fwdInternalAttachment);
    assertEquals(internalMessage.getId(), internalMessageId);
  }

  @Test
  public void testMarkMessageAsRead() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessageService.markMessageAsRead(internalMessageId);
    verify(internalMessageRepository, times(1)).save(internalMessage);
  }

  @Test
  public void testMarkMessageAsReplied() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessageService.markMessageAsReplied(internalMessageId);
    verify(internalMessageRepository, times(1)).save(internalMessage);
  }

  @Test
  public void testMarkMessageAsUnread() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessageService.markMessageAsUnread(internalMessageId);
    verify(internalMessageRepository, times(1)).save(internalMessage);
  }

  @Test
  public void testInternalMessage() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    when(internalMessageMapper.mapToDTO(internalMessage)).thenReturn(internalMessageDTO);
    InternalMessageDTO imDTO = internalMessageService.getInternalMessage(internalMessageId);
    assertEquals(imDTO, internalMessageDTO);
  }

  @Test
  public void testInternalAttachment() {
    when(internalAttachmentRepository.fetchById(internalAttachmentId)).thenReturn(internalAttachment);
    when(internalAttachmentMapper.mapToDTO(internalAttachment)).thenReturn(internalAttachmentDTO);
    InternalAttachmentDTO iaDTO = internalMessageService.getInternalAttachment(internalAttachmentId);
    assertEquals(iaDTO, internalAttachmentDTO);
  }

  @Test
  public void testDeleteMessageWithInboxFolderTypeAndDeleteTypeS() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessage.setDeleteType("S");
    internalMessageService.deleteMessage(internalMessageId, MailConstants.INBOX_FOLDER_TYPE);
    verify(internalMessageRepository, times(1)).delete(internalMessage);
  }

  @Test
  public void testDeleteMessageWithInboxFolderTypeAndDeleteTypeI() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessage.setDeleteType("TestDeleteType");
    internalMessageService.deleteMessage(internalMessageId, MailConstants.INBOX_FOLDER_TYPE);
    verify(internalMessageRepository, times(1)).save(internalMessage);
  }

  @Test
  public void testDeleteMessageWithSentFolderTypeAndDeleteTypeS() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessage.setDeleteType("I");
    internalMessageService.deleteMessage(internalMessageId, MailConstants.SENT_FOLDER_TYPE);
    verify(internalMessageRepository, times(1)).delete(internalMessage);
  }

  @Test
  public void testDeleteMessageWithSentFolderTypeAndDeleteTypeI() {
    when(internalMessageRepository.fetchById(internalMessageId)).thenReturn(internalMessage);
    internalMessage.setDeleteType("TestDeleteType");
    internalMessageService.deleteMessage(internalMessageId, MailConstants.SENT_FOLDER_TYPE);
    verify(internalMessageRepository, times(1)).save(internalMessage);
  }

  @Test
  public void testGetInternalInboxFolder() {
    Predicate predicate =
      qInternalMessage.mailTo.eq(userId).and(qInternalMessage.deleteType.notEqualsIgnoreCase(
        "I"));
    when(internalMessageRepository.findAll(predicate)).thenReturn(internalMessages);
    when(internalMessageMapper.mapToDTO(internalMessages)).thenReturn(internalMessagesDTO);
    List<InternalMessageDTO> intMessagesDTO = internalMessageService.getInternalInboxFolder(userId);
    assertEquals(intMessagesDTO.size(), internalMessagesDTO.size());
  }

  @Test
  public void testGetInternalSentFolder() {
    Predicate predicate = qInternalMessage.mailTo.eq(userId)
                                                 .and(qInternalMessage.deleteType.notEqualsIgnoreCase("S"));
    when(internalMessageRepository.findAll(predicate)).thenReturn(internalMessages);
    when(internalMessageMapper.mapToDTO(internalMessages)).thenReturn(internalMessagesDTO);
    List<InternalMessageDTO> intMessagesDTO = internalMessageService.getInternalSentFolder(userId);
    assertEquals(intMessagesDTO.size(), internalMessagesDTO.size());
  }

  @Test
  public void testGetMailCountWithUserdId() {
    when(internalMessageRepository.findAll(any(Predicate.class))).thenReturn(internalMessages);
    long expected = (long) internalMessages.size();
    long actual = internalMessageService.getMailCount(userId, null);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMailCountWithStatusId() {
    when(internalMessageRepository.findAll(any(Predicate.class))).thenReturn(internalMessages);
    long expected = (long) internalMessages.size();
    long actual = internalMessageService.getMailCount(null, MailConstants.EMAIL_STATUS.QUEUED.name());
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMailCountWithUserIdAndStatusId() {
    when(internalMessageRepository.findAll(any(Predicate.class))).thenReturn(internalMessages);
    long expected = (long) internalMessages.size();
    long actual = internalMessageService.getMailCount(userId, MailConstants.EMAIL_STATUS.QUEUED.name());
    assertEquals(expected, actual);

  }

  @Test
  public void testGetMailCount() {
    Predicate predicate = new BooleanBuilder();
    when(internalMessageRepository.findAll(predicate)).thenReturn(internalMessages);
    long expected = (long) internalMessages.size();
    long actual = internalMessageService.getMailCount(null, null);
    assertEquals(expected, actual);
  }

  @Test
  public void testInternalMessageAttachments() {
    when(internalAttachmentRepository.findByMessagesId(internalMessageId)).thenReturn(
      internalAttachments.stream().collect(Collectors.toList()));
    for (InternalAttachment intAttachment : internalAttachments) {
      when(internalAttachmentMapper.mapToDTO(intAttachment)).thenReturn(internalAttachmentsDTO.stream().filter(
        ia -> ia.getId().equals(intAttachment.getId())).findFirst().get());
    }
    List<InternalAttachmentDTO> intMessagesDTO =
      internalMessageService.getInternalMessageAttachments(internalMessageId);
    assertEquals(intMessagesDTO.size(), internalAttachmentsDTO.size());
  }
}
