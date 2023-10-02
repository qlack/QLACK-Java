package com.eurodyn.qlack.fuse.fd.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.fd.InitTestValues;
import com.eurodyn.qlack.fuse.fd.dto.ThreadMessageDTO;
import com.eurodyn.qlack.fuse.fd.model.ThreadMessage;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThreadMessageMapperTest {

  @InjectMocks
  private ThreadMessageMapperImpl mapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    ThreadMessage message = initTestValues.createThreadMessage();
    ThreadMessageDTO messageDTO = mapper.mapToDTO(message);

    assertEquals(message.getId(), messageDTO.getId());
    assertEquals(message.getTitle(), messageDTO.getTitle());
    assertEquals(message.getBody(), messageDTO.getBody());
    assertEquals(message.getAuthor(), messageDTO.getAuthor());
    assertEquals(message.getStatus(), messageDTO.getStatus());
    assertEquals(message.getOwnershipMask(), messageDTO.getOwnershipMask());
    assertEquals(message.getAttributesMask(), messageDTO.getAttributesMask());
  }

  @Test
  public void mapToDTOListTest() {
    List<ThreadMessage> messages = new ArrayList<>(initTestValues.create10Messages());
    List<ThreadMessageDTO> messageDTOS = mapper.mapToDTO(messages);
    assertEquals(messages.size(), messageDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertNull(mapper.mapToDTO((ThreadMessage) null));
    List<ThreadMessageDTO> messageDTOS = mapper.mapToDTO(
        (List<ThreadMessage>) null);
    assertNull(messageDTOS);
  }

  @Test
  public void mapToEntityTest() {
    ThreadMessageDTO messageDTO = initTestValues.createThreadMessageDTO();
    ThreadMessage message = mapper.mapToEntity(messageDTO);

    assertEquals(messageDTO.getId(), message.getId());
    assertEquals(messageDTO.getTitle(), message.getTitle());
    assertEquals(messageDTO.getBody(), message.getBody());
    assertEquals(messageDTO.getCreatedOn(), message.getCreatedOn());
    assertEquals(messageDTO.getAttributesMask(), message.getAttributesMask());
  }

  @Test
  public void mapToEntityListTest() {
    List<ThreadMessageDTO> messageDTOS = new ArrayList<>(initTestValues.create10DTOMessages());
    List<ThreadMessage> messages = mapper.mapToEntity(messageDTOS);
    assertEquals(messageDTOS.size(), messages.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(mapper.mapToEntity((ThreadMessageDTO) null));

    List<ThreadMessage> messages = mapper.mapToEntity((List<ThreadMessageDTO>) null);
    assertNull(messages);
  }

}
