package com.eurodyn.qlack.fuse.fd.service;

import static com.eurodyn.qlack.fuse.fd.util.ThreadStatus.DRAFT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.fd.InitTestValues;
import com.eurodyn.qlack.fuse.fd.dto.ThreadMessageDTO;
import com.eurodyn.qlack.fuse.fd.mapper.ThreadMessageMapperImpl;
import com.eurodyn.qlack.fuse.fd.model.QThreadMessage;
import com.eurodyn.qlack.fuse.fd.model.ThreadMessage;
import com.eurodyn.qlack.fuse.fd.repository.ThreadMessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

/**
 * @author European Dynamics
 */

@Log
@ExtendWith(MockitoExtension.class)
public class ThreadServiceTest {

  @InjectMocks
  private ThreadService threadService;

  @Mock
  private EntityManager entityManager;

  @Mock
  private ThreadMessage childMessage;

  private final EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);

  private final Query mockedQuery = mock(Query.class);

  private final ThreadMessageRepository repository = mock(ThreadMessageRepository.class);

  @Spy
  private ThreadMessageMapperImpl mapper;

  private ThreadMessage threadMessage;

  @Mock
  private ThreadMessageDTO threadMessageDTO;
  private List<ThreadMessage> threadMessages;
  private List<ThreadMessageDTO> threadMessageDTOS;

  private QThreadMessage qThreadMessage;


  @BeforeEach
  public void init() {
    threadService = new ThreadService(repository, mapper, entityManager);

    InitTestValues initTestValues = new InitTestValues();
    threadMessage = initTestValues.createThreadMessage();
    childMessage = initTestValues.createChildThreadMessage();
    threadMessageDTO = initTestValues.createThreadMessageDTO();
    threadMessages = initTestValues.create10Messages();
    threadMessageDTOS = initTestValues.create10DTOMessages();

    qThreadMessage = new QThreadMessage("threadMessage");
  }

  @Test
  public void testCreateThreadMessage() {
    when(mapper.mapToEntity(threadMessageDTO)).thenReturn(threadMessage);
    when(mapper.mapToDTO(threadMessage)).thenReturn(threadMessageDTO);
    when(repository.save(any())).thenReturn(threadMessage);

    ThreadMessageDTO dto = threadService.create(threadMessageDTO);
    verify(repository, times(1)).save(threadMessage);

    assertEquals(threadMessageDTO.getId(), dto.getId());
  }

  @Test
  public void testUpdateThreadMessage() {
    threadMessageDTO.setTitle("Updated title");
    threadMessageDTO.setBody("Updated message");
    when(mapper.mapToDTO(threadMessage)).thenReturn(threadMessageDTO);
    when(mapper.mapToEntity(threadMessageDTO)).thenReturn(threadMessage);
    when(repository.save(any())).thenReturn(threadMessage);

    var result = threadService.update(threadMessageDTO);

    verify(repository, times(1)).save(threadMessage);
    assertEquals(threadMessageDTO.getTitle(), result.getTitle());
    assertEquals(threadMessageDTO.getBody(), result.getBody());
  }

  @Test
  public void testDelete() {
    threadService.delete(threadMessage.getId());
    verify(repository, times(1)).deleteById(threadMessage.getId());
  }

  @Test
  public void testGetMessage() {
    when(repository.findById(threadMessageDTO.getId())).thenReturn(
        Optional.ofNullable(threadMessage));
    when(mapper.mapToDTO(threadMessage)).thenReturn(threadMessageDTO);

    ThreadMessageDTO foundMessageDTO = threadService.findById(this.threadMessage.getId());
    assertEquals(threadMessageDTO, foundMessageDTO);
  }

  @Test
  public void testFindAll() {
    when(repository.findAll()).thenReturn(threadMessages);
    when(mapper.mapToDTO(threadMessages)).thenReturn(threadMessageDTOS);

    List<ThreadMessageDTO> allMessages = threadService.findAll();
    assertEquals(threadMessages.size(), allMessages.size());
  }


  @Test
  public void testFindByDate() {

    Instant created = threadMessage.getCreatedOn();
    Instant from = created.minusSeconds(3000L);
    Instant to = created.plusSeconds(3000L);

    when(repository.findAll(
        qThreadMessage.createdOn.after(from).and(qThreadMessage.createdOn.before(to)),
        Sort.unsorted())).thenReturn(threadMessages);
    when(mapper.mapToDTO(threadMessages)).thenReturn(threadMessageDTOS);

    List<ThreadMessageDTO> messages = threadService.findByDate(from, to,
        Sort.unsorted().ascending());

    assertTrue(messages.size() > 0);
    messages.forEach(threadMessageDTO1 -> {
      assertTrue(threadMessageDTO1.getCreatedOn().isAfter(from));
      assertTrue(threadMessageDTO1.getCreatedOn().isBefore(to));
    });
  }


  @Test
  public void testFindAllMessagesByAuthor() {

    when(repository.findAll(qThreadMessage.author.eq(threadMessage.getAuthor()))).thenReturn(
        threadMessages);
    List<ThreadMessageDTO> messages = threadService.findAllMessagesByAuthor(
        threadMessage.getAuthor());
    assertTrue(messages.size() > 0);
    messages.forEach(threadMessageDTO1 -> assertEquals(threadMessageDTO1.getAuthor(), threadMessage.getAuthor()));
  }
  @Test
  public void testFindThreadsByAuthor() {

    when(repository.findAll(qThreadMessage.author.eq(threadMessage.getAuthor())
        .and(qThreadMessage.parentThreadMessage.isNull()))).thenReturn(threadMessages);
    List<ThreadMessageDTO> messages = threadService.findThreadsByAuthor(threadMessage.getAuthor());
    assertTrue(messages.size() > 0);
    messages.forEach(threadMessageDTO1 -> {
      assertEquals(threadMessageDTO1.getAuthor(), threadMessage.getAuthor());
      assertTrue(Objects.isNull(threadMessageDTO1.getParentThread()));
    });
  }

  @Test
  public void testFindMainThreads() {

    when(repository.findAll(qThreadMessage.parentThreadMessage.isNull())).thenReturn(
        threadMessages);
    List<ThreadMessageDTO> messages = threadService.findMainThreads();

    assertTrue(messages.size() > 0);
    messages.forEach(threadMessageDTO1 -> assertTrue(Objects.isNull(threadMessageDTO1.getParentThread())));
  }

  @Test
  public void testFindResource() {
    when(repository.findById(threadMessageDTO.getId())).thenReturn(
        Optional.ofNullable(threadMessage));
    ThreadMessage foundMessage = threadService.findResource(threadMessageDTO.getId());
    assertTrue(Objects.nonNull(foundMessage.getId()));
    assertEquals(foundMessage.getId(), threadMessage.getId());

  }

  @Test
  public void testFindRoot() {

    when(repository.findById(childMessage.getId())).thenReturn(Optional.ofNullable(childMessage));

    ThreadMessage foundChild = threadService.findResource(childMessage.getId());
    ThreadMessage foundRoot = threadService.findRoot(foundChild);
    assertTrue(Objects.nonNull(foundRoot.getId()));
    assertEquals(foundChild.getParentThreadMessage().getId(), threadMessage.getId());
  }


  @Test
  public void testFindThreadsByStatus() {

    when(repository.findAll(qThreadMessage.status.eq(DRAFT))).thenReturn(threadMessages);
    List<ThreadMessageDTO> messages = threadService.findThreadsByStatus(DRAFT);
    messages.forEach(threadMessageDTO1 -> assertEquals(DRAFT, threadMessageDTO1.getStatus()));
  }


  @Test
  public void testFindThreadsWithComments() {

    when(repository.findAll(qThreadMessage.body.isNotNull())).thenReturn(threadMessages);
    List<ThreadMessageDTO> messages = threadService.findThreadsWithComments();
    messages.forEach(threadMessageDTO1 -> assertNotNull(threadMessageDTO1.getBody()));
  }


  @Test
  public void testGetThreadMessages() {

    when(repository.findById(childMessage.getId())).thenReturn(Optional.ofNullable(childMessage));

    when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

    when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
    ThreadMessage foundChild = threadService.findResource(childMessage.getId());

    List<String> messages = threadService.getThreadTextMessages(foundChild.getId());
    assertTrue(messages.size() > 0);
//    messages.forEach(log::info);
    messages.forEach(Assertions::assertNotNull);
  }


  @Test
  public void testOwnershipMask() {
    threadMessage.setOwnershipMask("0000");
    when(repository.findById(threadMessageDTO.getId())).thenReturn(
        Optional.ofNullable(threadMessage));
    when(repository.save(any())).thenReturn(threadMessage);

    ThreadMessageDTO result = threadService.setOwnershipMask(threadMessage.getId(), "1001");
    verify(repository, times(1)).save(threadMessage);
    assertNotNull(result.getId());
    assertEquals(result.getOwnershipMask(), threadMessage.getOwnershipMask());
  }


  @Test
  public void testAttributesMask() {
    threadMessage.setAttributesMask("1010");
    threadMessageDTO.setAttributesMask("1010");
    when(repository.findById(threadMessageDTO.getId())).thenReturn(
        Optional.ofNullable(threadMessage));
    when(mapper.mapToDTO(threadMessage)).thenReturn(threadMessageDTO);
    when(repository.save(any())).thenReturn(threadMessage);

    ThreadMessageDTO result = threadService.setAttributesMask(threadMessage.getId(), "1010");

    verify(repository, times(1)).save(threadMessage);
    assertNotNull(result.getId());
    assertEquals(result.getAttributesMask(), threadMessage.getAttributesMask());
  }


}
