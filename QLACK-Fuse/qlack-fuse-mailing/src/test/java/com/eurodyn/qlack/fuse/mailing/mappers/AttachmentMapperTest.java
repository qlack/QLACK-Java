package com.eurodyn.qlack.fuse.mailing.mappers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AttachmentMapperTest {

    @InjectMocks
    private AttachmentMapperImpl attachmentMapperImpl;

    private InitTestValues initTestValues;
    private Attachment attachment;
    private AttachmentDTO attachmentDTO;
    private Set<Attachment> attachments;
    private List<AttachmentDTO> attachmentsDTO;

    private String id;
    private String filename;
    private String contentType;
    private byte[] data;

    @Before
    public void init() {
        initTestValues = new InitTestValues();

        attachment = initTestValues.createAttachment();
        attachmentDTO = initTestValues.createAttachmentDTO();
        attachments = initTestValues.createAttachments();
        attachmentsDTO = initTestValues.createAttachmentsDTO();
    }

    @Test
    public void testMapToDTOId() {
        AttachmentDTO attachmentDTO = attachmentMapperImpl.mapToDTO(attachment);
        assertEquals(attachment.getId(), attachmentDTO.getId());
    }

    @Test
    public void testMapToDTOFilename() {
        AttachmentDTO attachmentDTO = attachmentMapperImpl.mapToDTO(attachment);
        assertEquals(attachment.getFilename(), attachmentDTO.getFilename());
    }

    @Test
    public void testMapToDTOContentType() {
        AttachmentDTO attachmentDTO = attachmentMapperImpl.mapToDTO(attachment);
        assertEquals(attachment.getContentType(), attachmentDTO.getContentType());
    }

    @Test
    public void testMapToDTOData() {
        AttachmentDTO attachmentDTO = attachmentMapperImpl.mapToDTO(attachment);
        assertArrayEquals(attachment.getData(), attachmentDTO.getData());
    }

    @Test
    public void testMapToEntityId() {
        Attachment attachment = attachmentMapperImpl.mapToEntity(attachmentDTO);
        assertEquals(attachmentDTO.getId(), attachment.getId());
    }

    @Test
    public void testMapToEntityFilename() {
        Attachment attachment = attachmentMapperImpl.mapToEntity(attachmentDTO);
        assertEquals(attachmentDTO.getFilename(), attachment.getFilename());
    }

    @Test
    public void testMapToEntityContentType() {
        Attachment attachment = attachmentMapperImpl.mapToEntity(attachmentDTO);
        assertEquals(attachmentDTO.getContentType(), attachment.getContentType());
    }

    @Test
    public void testMapToEntityData() {
        Attachment attachment = attachmentMapperImpl.mapToEntity(attachmentDTO);
        assertArrayEquals(attachmentDTO.getData(), attachment.getData());
    }
}
