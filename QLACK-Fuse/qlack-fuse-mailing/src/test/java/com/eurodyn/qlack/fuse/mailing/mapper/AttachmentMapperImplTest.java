package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AttachmentMapperImplTest {

  @InjectMocks
  private AttachmentMapperImpl attachmentMapper;

  private InitTestValues initTestValues;

  @Before
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
    assertEquals(null, attachmentMapper.mapToDTO((Attachment) null));

    List<AttachmentDTO> attachmentDTOS = attachmentMapper.mapToDTO((List<Attachment>) null);
    assertEquals(null, attachmentDTOS);
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
    assertEquals(null, attachmentMapper.mapToEntity((AttachmentDTO) null));

    Attachment attachment = attachmentMapper.mapToEntity((AttachmentDTO) null);
    assertEquals(null, attachment);
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
    assertEquals(null, attachmentMapper.mapToEntity((List<AttachmentDTO>) null));
    List<Attachment> attachments = attachmentMapper.mapToEntity((List<AttachmentDTO>) null);
    assertEquals(null, attachments);

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
