package com.eurodyn.qlack.fuse.mailing.mappers;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class InternalAttachmentMapperImplTest {

  @InjectMocks
  private InternalAttachmentMapperImpl internalAttachmentMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    InternalAttachment internalAttachment = initTestValues.createInternalAttachment();
    InternalAttachmentDTO internalAttachmentDTO = internalAttachmentMapper.mapToDTO(internalAttachment);
    assertEquals(internalAttachment.getFilename(),internalAttachmentDTO.getFilename());

  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null, internalAttachmentMapper.mapToDTO((InternalAttachment) null));

    List<InternalAttachmentDTO> internalAttachmentDTOS = internalAttachmentMapper.mapToDTO((List<InternalAttachment>) null);
    assertEquals(null, internalAttachmentDTOS);
  }
  @Test
  public void mapToDTOListTest() {
    List<InternalAttachment> internalAttachments = new ArrayList<>();
    internalAttachments.add(initTestValues.createInternalAttachment());
    List<InternalAttachmentDTO> internalAttachmentDTOS = internalAttachmentMapper.mapToDTO(internalAttachments);

    assertEquals(internalAttachments.size(), internalAttachmentDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    InternalAttachmentDTO internalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();
    InternalAttachment internalAttachment = internalAttachmentMapper.mapToEntity(internalAttachmentDTO);
    assertEquals(internalAttachment.getFilename(),internalAttachmentDTO.getFilename());

  }
  @Test
  public void mapToEntityNullTest() {
    assertEquals(null, internalAttachmentMapper.mapToEntity((InternalAttachmentDTO) null));

    InternalAttachment internalAttachment = internalAttachmentMapper.mapToEntity((InternalAttachmentDTO) null);
    assertEquals(null, internalAttachment);
  }


  @Test
  public void mapToEntityListTest() {
    List<InternalAttachmentDTO> internalAttachmentDTOS = new ArrayList<>();
    internalAttachmentDTOS.add(initTestValues.createFwdInternalAttachmentDTO());
    List<InternalAttachment> internalAttachments = internalAttachmentMapper.mapToEntity(internalAttachmentDTOS);

    assertEquals(internalAttachmentDTOS.size(), internalAttachments.size());
  }
  @Test
  public void mapToEntityListNullTest() {
    assertEquals(null, internalAttachmentMapper.mapToEntity((List<InternalAttachmentDTO> )null));
    List<InternalAttachment> internalAttachments = internalAttachmentMapper.mapToEntity((List<InternalAttachmentDTO> ) null);
    assertEquals(null, internalAttachments);

  }
  @Test
  public void testInternalAttachmentDTOToInternalAttachmentDataNotNull(){
    InternalAttachment internalAttachment = initTestValues.createInternalAttachment();
    internalAttachment.setData(null);
    assertNull(internalAttachmentMapper.mapToDTO(internalAttachment).getData());
  }

  @Test
  public void testInternalAttachmentToInternalAttachmentDTODataNotNull(){
    InternalAttachmentDTO internalAttachmentDTO = initTestValues.createFwdInternalAttachmentDTO();
    internalAttachmentDTO.setData(null);
    assertNull(internalAttachmentMapper.mapToEntity(internalAttachmentDTO).getData());
  }


}
