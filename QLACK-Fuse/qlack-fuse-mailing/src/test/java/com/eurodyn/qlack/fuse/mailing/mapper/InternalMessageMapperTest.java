package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InternalMessageMapperTest {

  @InjectMocks
  private InternalMessageMapperImpl internalMessageMapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    InternalMessage internalMessage = initTestValues.createInternalMessage();
    InternalMessageDTO internalMessageDTO = internalMessageMapper
      .mapToDTO(internalMessage);
    assertEquals(internalMessage.getMessage(), internalMessageDTO.getMessage());

  }

  @Test
  public void mapToDTONullTest() {
    assertNull(internalMessageMapper.mapToDTO((InternalMessage) null));
    List<InternalMessageDTO> internalMessageDTOS = internalMessageMapper
      .mapToDTO(
        (List<InternalMessage>) null);
    assertNull(internalMessageDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<InternalMessage> internalMessages = new ArrayList<>();
    internalMessages.add(initTestValues.createInternalMessage());
    List<InternalMessageDTO> internalMessageDTOS = internalMessageMapper
      .mapToDTO(internalMessages);

    assertEquals(internalMessages.size(), internalMessageDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    InternalMessageDTO internalMessageDTO = initTestValues
      .createInternalMessageDTO();
    InternalMessage internalMessage = internalMessageMapper
      .mapToEntity(internalMessageDTO);
    assertEquals(internalMessage.getMessage(), internalMessageDTO.getMessage());

  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(internalMessageMapper.mapToEntity((InternalMessageDTO) null));

    InternalMessage internalMessage = internalMessageMapper
      .mapToEntity((InternalMessageDTO) null);
    assertNull(internalMessage);
  }


  @Test
  public void mapToEntityListTest() {
    List<InternalMessageDTO> internalMessageDTOS = new ArrayList<>();
    internalMessageDTOS.add(initTestValues.createInternalMessageDTO());
    List<InternalMessage> internalMessages = internalMessageMapper
      .mapToEntity(internalMessageDTOS);

    assertEquals(internalMessageDTOS.size(), internalMessages.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(internalMessageMapper.mapToEntity((List<InternalMessageDTO>) null));
    List<InternalMessage> internalMessages = internalMessageMapper
      .mapToEntity((List<InternalMessageDTO>) null);
    assertNull(internalMessages);

  }

  @Test
  public void testInternalAttachmentDTOToInternalAttachmentNull() {
    assertNull(
      internalMessageMapper.internalAttachmentDTOToInternalAttachment(null));
  }

  @Test
  public void testInternalAttachmentDTOToInternalAttachmentDataNotNull() {
    InternalAttachment internalAttachment = initTestValues
      .createInternalAttachment();
    internalAttachment.setData(null);
    assertNull(internalMessageMapper
      .internalAttachmentToInternalAttachmentDTO(internalAttachment)
      .getData());
  }

  @Test
  public void testInternalAttachmentToInternalAttachmentDTODataNotNull() {
    InternalAttachmentDTO internalAttachmentDTO = initTestValues
      .createFwdInternalAttachmentDTO();
    internalAttachmentDTO.setData(null);
    assertNull(
      internalMessageMapper
        .internalAttachmentDTOToInternalAttachment(internalAttachmentDTO)
        .getData());
  }


  @Test
  public void testInternalAttachmentΤoInternalAttachmentDTONull() {
    assertNull(
      internalMessageMapper.internalAttachmentToInternalAttachmentDTO(null));
  }

  @Test
  public void testInternalAttachmentSetToInternalAttachmentDTOList() {
    assertNull(internalMessageMapper
      .internalAttachmentSetToInternalAttachmentDTOList(null));
  }

  @Test
  public void testInternalAttachmentDTOListToInternalAttachmentSet() {
    assertNull(internalMessageMapper
      .internalAttachmentDTOListToInternalAttachmentSet(null));
  }


}
