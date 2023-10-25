package com.eurodyn.qlack.fuse.acv.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.javers.core.Javers;
import org.javers.core.commit.Commit;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.repository.jql.JqlQuery;
import org.javers.shadow.Shadow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VersioningServiceTest {

  @InjectMocks
  private VersioningService versioningService;

  @Mock
  private Javers javers;

  @Mock
  private Stream<Shadow<Object>> shadowStream;

  @Mock
  private Stream<Object> stream;

  @Mock
  private List<Object> objectList;

  @Mock
  private Shadow<Object> objectShadow;

  @Mock
  private Shadow shadow;

  @Mock
  private CommitMetadata commitMetadata;

  @Mock
  private Object object1;

  @BeforeEach
  public void init() {
    versioningService = new VersioningService(javers);

  }

  @Test
  public void createVersionTest() {
    Commit commit = mock(Commit.class);
    CommitId commitId = mock(CommitId.class);

    when(javers.commit(anyString(), any(), any())).thenReturn(commit);
    when(commit.getId()).thenReturn(commitId);
    assertEquals(0,
      versioningService.createVersion("qlack", object1, "change"));
  }

  @Test
  public void createVersionNullAuthorTest() {
    assertThrows(NullPointerException.class, () -> {
      versioningService.createVersion(null, object1, "change");
    });
  }

  @Test
  public void createVersionNullObjectTest() {
    assertThrows(NullPointerException.class, () -> {
      versioningService.createVersion("qlack", null, "change");
    });
  }

  @Test
  public void createVersionWithPropertiesNullAuthorTest() {
    assertThrows(NullPointerException.class, () -> {
      versioningService.createVersion(null, object1, "change", null);
    });
  }

  @Test
  public void createVersionWithPropertiesNullObjectTest() {
    assertThrows(NullPointerException.class, () -> {
      versioningService.createVersion("qlack", null, "change", null);
    });
  }

  @Test
  public void createVersionNullPropertiesTest() {
    assertThrows(NullPointerException.class, () -> {
      versioningService.createVersion("qlack", object1, "change", null);
    });
  }

  @Test
  public void findVersionsTest() {
    assertEquals(Collections.emptyList(),
      versioningService.findVersions(object1));
  }

  @Test
  public void findVersionsNullObjectTest() {
    assertThrows(NullPointerException.class, () -> {
      versioningService.findVersions(null);
    });
  }

  @Test
  public void retrieveVersionTest() {
    when(javers.findShadowsAndStream(any(JqlQuery.class)))
      .thenReturn(shadowStream);
    when(shadowStream.filter(any(Predicate.class))).thenReturn(shadowStream);
    when(shadowStream.map(any(Function.class))).thenReturn(stream);
    when(stream.collect(any())).thenReturn(objectList);
    assertEquals(objectList.get(0),
      versioningService.retrieveVersion(object1, 1L));
  }

  @Test
  public void retrieveVersionExceptionTest() {
    assertThrows(QDoesNotExistException.class, () -> {
      versioningService.retrieveVersion(object1, 1L);
    });
  }

  @Test
  public void retrieveLatestVersionTest() {
    when(javers.findShadowsAndStream(any(JqlQuery.class)))
      .thenReturn(shadowStream);
    when(shadowStream.findFirst()).thenReturn(Optional.of(objectShadow));
    assertEquals(Optional.of(objectShadow).get().get(),
      versioningService.retrieveLatestVersion(object1));
  }

  @Test
  public void retrieveLatestVersionExceptionTest() {
    assertThrows(QDoesNotExistException.class, () -> {
      versioningService.retrieveLatestVersion(object1);
    });
  }

  @Test
  public void convertToVersionDTOTest() {
    CommitId commitId = mock(CommitId.class);
    when(shadow.getCommitMetadata()).thenReturn(commitMetadata);
    when(shadow.getCommitId()).thenReturn(commitId);
    assertNotNull(versioningService.convertToVersionDTO(shadow));
  }

}
