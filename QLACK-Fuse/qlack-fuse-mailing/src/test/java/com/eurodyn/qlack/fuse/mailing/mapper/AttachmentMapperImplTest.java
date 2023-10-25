package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachmentMapperImplTest {

  @InjectMocks
  private AttachmentMapperImpl attachmentMapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Attachment attachment = initTestValues.createAttachment();
    AttachmentDTO attachmentDTO = attachmentMapper.mapToDTO(attachment);
    assertEquals(attachment.getFilename(), attachmentDTO.getFilename());

  }

  @Test
  public void mapToDTONullTest() {
    assertNull(attachmentMapper.mapToDTO((Attachment) null));
    List<AttachmentDTO> attachmentDTOS = attachmentMapper
      .mapToDTO((List<Attachment>) null);
    assertNull(attachmentDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<Attachment> attachments = new ArrayList<>();
    attachments.add(initTestValues.createAttachment());
    List<AttachmentDTO> attachmentDTOS = attachmentMapper.mapToDTO(attachments);

    assertEquals(attachments.size(), attachmentDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    AttachmentDTO attachmentDTO = initTestValues.createAttachmentDTO();
    Attachment attachment = attachmentMapper.mapToEntity(attachmentDTO);
    assertEquals(attachment.getFilename(), attachmentDTO.getFilename());

  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(attachmentMapper.mapToEntity((AttachmentDTO) null));
    Attachment attachment = attachmentMapper.mapToEntity((AttachmentDTO) null);
    assertNull(attachment);
  }


  @Test
  public void mapToEntityListTest() {
    List<AttachmentDTO> attachmentDTOS = new ArrayList<>();
    attachmentDTOS.add(initTestValues.createAttachmentDTO());
    List<Attachment> attachments = attachmentMapper.mapToEntity(attachmentDTOS);

    assertEquals(attachmentDTOS.size(), attachments.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(attachmentMapper.mapToEntity((List<AttachmentDTO>) null));
    List<Attachment> attachments = attachmentMapper
      .mapToEntity((List<AttachmentDTO>) null);
    assertNull(attachments);

  }

  @Test
  public void testAttachmentDTOToAttachmentDataNotNull() {
    Attachment attachment = initTestValues.createAttachment();
    attachment.setData(null);
    assertNull(attachmentMapper.mapToDTO(attachment).getData());
  }

  @Test
  public void testAttachmentToAttachmentDTODataNotNull() {
    AttachmentDTO attachmentDTO = initTestValues.createAttachmentDTO();
    attachmentDTO.setData(null);
    assertNull(attachmentMapper.mapToEntity(attachmentDTO).getData());
  }


}
