package com.eurodyn.qlack.fuse.acv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({Commit.class, CommitId.class})
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

  @Before
  public void init() {
    versioningService = new VersioningService(javers);
    //PowerMockito.mockStatic(Commit.class);
    //PowerMockito.mockStatic(CommitId.class);
  }
/*
  @Test
  public void createVersionTest() {
    Commit commit = mock(Commit.class);
    CommitId commitId = mock(CommitId.class);

    when(javers.commit(anyString(), any(), any())).thenReturn(commit);
    when(commit.getId()).thenReturn(commitId);
    assertEquals(0,
      versioningService.createVersion("qlack", object1, "change"));
  }
*/
  @Test(expected = NullPointerException.class)
  public void createVersionNullAuthorTest() {
    versioningService.createVersion(null, object1, "change");
  }

  @Test(expected = NullPointerException.class)
  public void createVersionNullObjectTest() {
    versioningService.createVersion("qlack", null, "change");
  }

  @Test(expected = NullPointerException.class)
  public void createVersionWithPropertiesNullAuthorTest() {
    versioningService.createVersion(null, object1, "change", null);
  }

  @Test(expected = NullPointerException.class)
  public void createVersionWithPropertiesNullObjectTest() {
    versioningService.createVersion("qlack", null, "change", null);
  }

  @Test(expected = NullPointerException.class)
  public void createVersionNullPropertiesTest() {
    versioningService.createVersion("qlack", object1, "change", null);
  }

  @Test
  public void findVersionsTest() {
    assertEquals(Collections.emptyList(),
      versioningService.findVersions(object1));
  }

  @Test(expected = NullPointerException.class)
  public void findVersionsNullObjectTest() {
    versioningService.findVersions(null);
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

  @Test(expected = QDoesNotExistException.class)
  public void retrieveVersionExceptionTest() {
    versioningService.retrieveVersion(object1, 1L);
  }

  @Test
  public void retrieveLatestVersionTest() {
    when(javers.findShadowsAndStream(any(JqlQuery.class)))
      .thenReturn(shadowStream);
    when(shadowStream.findFirst()).thenReturn(Optional.of(objectShadow));
    assertEquals(Optional.of(objectShadow).get().get(),
      versioningService.retrieveLatestVersion(object1));
  }

  @Test(expected = QDoesNotExistException.class)
  public void retrieveLatestVersionExceptionTest() {
    versioningService.retrieveLatestVersion(object1);
  }
/*
  @Test
  public void convertToVersionDTOTest() {
    CommitId commitId = mock(CommitId.class);
    when(shadow.getCommitMetadata()).thenReturn(commitMetadata);
    when(shadow.getCommitId()).thenReturn(commitId);
    assertNotNull(versioningService.convertToVersionDTO(shadow));
  }
*/
}
