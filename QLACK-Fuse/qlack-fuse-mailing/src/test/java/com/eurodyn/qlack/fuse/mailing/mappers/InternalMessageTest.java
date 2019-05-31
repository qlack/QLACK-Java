package com.eurodyn.qlack.fuse.mailing.mappers;/**
 * @author European Dynamics
 */

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;

@RunWith(MockitoJUnitRunner.class)
public class InternalMessageTest {

  @InjectMocks
  private InternalMessageMapperImpl internalMessagesMapperImpl;

  private InitTestValues initTestValues;
  private InternalMessage internalMessage;
  private InternalMessageDTO internalMessageDTO;
  private List<InternalMessage> internalMessages;
  private List<InternalMessageDTO> internalMessagesDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    internalMessage = initTestValues.createInternalMessage();
    internalMessageDTO = initTestValues.createInternalMessageDTO();
    internalMessages = initTestValues.createInternalMessages();
    internalMessagesDTO = initTestValues.createInternalMessagesDTO();
  }

  @Test
  public void testMapToDTOId() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getId(), internalMessageDTO.getId());
  }

  @Test
  public void testMapToDTOSubject() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getSubject(), internalMessageDTO.getSubject());
  }

  @Test
  public void testMapToDTOMessage() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getMessage(), internalMessageDTO.getMessage());
  }

  @Test
  public void testMapToDTOMailFrom() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getMailFrom(), internalMessageDTO.getMailFrom());
  }

  @Test
  public void testMapToDTOMailTo() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getMailTo(), internalMessageDTO.getMailTo());
  }

  @Test
  public void testMapToDTODateSent() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getDateSent().longValue(), internalMessageDTO.getDateSent().getTime());
  }

  @Test
  public void testMapToDTODateReceived() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getDateReceived().longValue(), internalMessageDTO.getDateReceived().getTime());
  }

  @Test
  public void testMapToDTOStatus() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getStatus(), internalMessageDTO.getStatus());
  }

  @Test
  public void testMapToDTODeleteType() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getDeleteType(), internalMessageDTO.getDeleteType());
  }

  @Test
  public void testMapToDTOAttachments() {
    InternalMessageDTO internalMessageDTO = internalMessagesMapperImpl.mapToDTO(internalMessage);
    assertEquals(internalMessage.getAttachments().size(), internalMessageDTO.getAttachments().size());
  }

  @Test
  public void testMapToDTOList() {
    List<InternalMessage> internalMessagesList = internalMessages.stream().collect(Collectors.toList());
    internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessagesList);
    assertEquals(internalMessages.size(), internalMessagesDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getId(), internalMessage.getId());
  }

  @Test
  public void testMapToEntitySubject() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getSubject(), internalMessage.getSubject());
  }

  @Test
  public void testMapToEntityMessage() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getMessage(), internalMessage.getMessage());
  }

  @Test
  public void testMapToEntityMailFrom() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getMailFrom(), internalMessage.getMailFrom());
  }

  @Test
  public void testMapToEntityMailTo() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getMailTo(), internalMessage.getMailTo());
  }

  @Test
  public void testMapToEntityDateSent() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getDateSent().getTime(), internalMessage.getDateSent().longValue());
  }

  @Test
  public void testMapToEntityDateReceived() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getDateReceived().getTime(), internalMessage.getDateReceived().longValue());
  }

  @Test
  public void testMapToEntityStatus() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getStatus(), internalMessage.getStatus());
  }

  @Test
  public void testMapToEntityDeleteType() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getDeleteType(), internalMessage.getDeleteType());
  }

  @Test
  public void testMapToEntityAttachments() {
    InternalMessage internalMessage = internalMessagesMapperImpl.mapToEntity(internalMessageDTO);
    assertEquals(internalMessageDTO.getAttachments().size(), internalMessage.getAttachments().size());
  }

  @Test
  public void testMapToEntityList() {
    internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
    assertEquals(internalMessagesDTO.size(), internalMessages.size());
  }
}
