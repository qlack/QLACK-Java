package com.eurodyn.qlack.fuse.mailing.mappers;/**
 * @author European Dynamics
 */

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;

@RunWith(MockitoJUnitRunner.class)
public class InternalAttachmentTest {

  @InjectMocks
  private InternalAttachmentMapperImpl internalAttachmentMapperImpl;

  private InitTestValues initTestValues;
  private com.eurodyn.qlack.fuse.mailing.model.InternalAttachment internalAttachment;
  private com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO internalAttachmentDTO;
  private Set<InternalAttachment> internalAttachments;
  private List<InternalAttachmentDTO> internalAttachmentsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    internalAttachment = initTestValues.createInternalAttachment();
    internalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();
    internalAttachments = initTestValues.createInternalAttachments();
    internalAttachmentsDTO = initTestValues.createInternalAttachmentsDTO();
  }

  @Test
  public void testMapToDTOId() {
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getId(), internalAttachmentDTO.getId());
  }

  @Test
  public void testMapToDTOMessagesId() {
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getMessages(), internalAttachmentDTO.getMessagesId());
  }

  @Test
  public void testMapToDTOFilename() {
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getFilename(), internalAttachmentDTO.getFilename());
  }

  @Test
  public void testMapToDTOContentType() {
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getContentType(), internalAttachmentDTO.getContentType());
  }

  @Test
  public void testMapToDTOData() {
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachment);
    assertArrayEquals(internalAttachment.getData(), internalAttachmentDTO.getData());
  }

  @Test
  public void testMapToDTOFormat() {
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getFormat(), internalAttachmentDTO.getFormat());
  }

  @Test
  public void testMapToDTOList() {
    List<InternalAttachment> internalAttachmentsList = internalAttachments.stream().collect(Collectors.toList());
    internalAttachmentsDTO = internalAttachmentMapperImpl.mapToDTO(internalAttachmentsList);
    assertEquals(internalAttachments.size(), internalAttachmentsDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    InternalAttachment internalAttachment = internalAttachmentMapperImpl.mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getId(), internalAttachment.getId());
  }

  @Test
  public void testMapToEntityMessagesId() {
    InternalAttachment internalAttachment = internalAttachmentMapperImpl.mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getMessagesId(), internalAttachment.getMessages());
  }

  @Test
  public void testMapToEntityFilename() {
    InternalAttachment internalAttachment = internalAttachmentMapperImpl.mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getFilename(), internalAttachment.getFilename());
  }

  @Test
  public void testMapToEntityContentType() {
    InternalAttachment internalAttachment = internalAttachmentMapperImpl.mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getContentType(), internalAttachment.getContentType());
  }

  @Test
  public void testMapToEntityData() {
    InternalAttachment internalAttachment = internalAttachmentMapperImpl.mapToEntity(internalAttachmentDTO);
    assertArrayEquals(internalAttachmentDTO.getData(), internalAttachment.getData());
  }

  @Test
  public void testMapToEntityFormat() {
    InternalAttachment internalAttachment = internalAttachmentMapperImpl.mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachmentDTO.getFormat(), internalAttachment.getFormat());
  }

  @Test
  public void testMapToEntityList() {
    internalAttachments = internalAttachmentMapperImpl.mapToEntity(internalAttachmentsDTO).stream().collect(
      Collectors.toSet());
    assertEquals(internalAttachmentsDTO.size(), internalAttachments.size());
  }

}
