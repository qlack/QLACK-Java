package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InternalAttachmentMapperImplTest {

  @InjectMocks
  private InternalAttachmentMapperImpl internalAttachmentMapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    InternalAttachment internalAttachment = initTestValues
      .createInternalAttachment();
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapper
      .mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getFilename(),
      internalAttachmentDTO.getFilename());

  }

  @Test
  public void mapToDTONullTest() {
    assertNull(internalAttachmentMapper.mapToDTO((InternalAttachment) null));

    List<InternalAttachmentDTO> internalAttachmentDTOS = internalAttachmentMapper
      .mapToDTO((List<InternalAttachment>) null);
    assertNull(internalAttachmentDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<InternalAttachment> internalAttachments = new ArrayList<>();
    internalAttachments.add(initTestValues.createInternalAttachment());
    List<InternalAttachmentDTO> internalAttachmentDTOS = internalAttachmentMapper
      .mapToDTO(internalAttachments);

    assertEquals(internalAttachments.size(), internalAttachmentDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    InternalAttachmentDTO internalAttachmentDTO = initTestValues
      .createFwdInternalAttachmentDTO();
    InternalAttachment internalAttachment = internalAttachmentMapper
      .mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachment.getFilename(),
      internalAttachmentDTO.getFilename());

  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(internalAttachmentMapper.mapToEntity((InternalAttachmentDTO) null));

    InternalAttachment internalAttachment = internalAttachmentMapper
      .mapToEntity((InternalAttachmentDTO) null);
    assertNull(internalAttachment);
  }


  @Test
  public void mapToEntityListTest() {
    List<InternalAttachmentDTO> internalAttachmentDTOS = new ArrayList<>();
    internalAttachmentDTOS.add(initTestValues.createFwdInternalAttachmentDTO());
    List<InternalAttachment> internalAttachments = internalAttachmentMapper
      .mapToEntity(internalAttachmentDTOS);

    assertEquals(internalAttachmentDTOS.size(), internalAttachments.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(internalAttachmentMapper.mapToEntity((List<InternalAttachmentDTO>) null));
    List<InternalAttachment> internalAttachments = internalAttachmentMapper
      .mapToEntity((List<InternalAttachmentDTO>) null);
    assertNull(internalAttachments);

  }

  @Test
  public void testInternalAttachmentDTOToInternalAttachmentDataNotNull() {
    InternalAttachment internalAttachment = initTestValues
      .createInternalAttachment();
    internalAttachment.setData(null);
    assertNull(internalAttachmentMapper.mapToDTO(internalAttachment).getData());
  }

  @Test
  public void testInternalAttachmentToInternalAttachmentDTODataNotNull() {
    InternalAttachmentDTO internalAttachmentDTO = initTestValues
      .createFwdInternalAttachmentDTO();
    internalAttachmentDTO.setData(null);
    assertNull(
      internalAttachmentMapper.mapToEntity(internalAttachmentDTO).getData());
  }


}
