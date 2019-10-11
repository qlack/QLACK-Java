package com.eurodyn.qlack.fuse.cm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.exception.QIOException;
import com.eurodyn.qlack.fuse.cm.mapper.NodeMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.util.JPAQueryUtil;
import com.eurodyn.qlack.fuse.cm.util.StreamsUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JPAQueryUtil.class, QueryResults.class, StreamsUtil.class})
public class DocumentServiceEMTest {

  @InjectMocks
  private DocumentService documentService;

  @Mock
  private ConcurrencyControlService concurrencyControlService;

  @Mock
  private VersionService versionService;

  @Mock
  private NodeRepository nodeRepository;

  @Mock
  private NodeMapper nodeMapper;

  @Mock
  private EntityManager em;

  @Mock
  private Query query;

  @Mock
  private JPAQuery<Node> jpaQuery;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  @Mock
  private ZipOutputStream zipOutputStream;

  @Before
  public void init() throws Exception {
    documentService = new DocumentService(concurrencyControlService, versionService, nodeRepository,
        nodeMapper);
    ReflectionTestUtils.setField(documentService, "em", em);

    whenNew(JPAQueryFactory.class).withArguments(em).thenReturn(jpaQueryFactory);
    when(jpaQueryFactory.selectFrom(any(EntityPath.class))).thenReturn(jpaQuery);
    when(jpaQuery.innerJoin(any(CollectionExpression.class), any(Path.class))).thenReturn(jpaQuery);
    when(jpaQuery.where((Predicate) any())).thenReturn(jpaQuery);
  }

  @Test
  public void getNodeByAttributesTest() {
    when(em.createQuery(anyString())).thenReturn(query);
    Map<String, String> attributes = new HashMap<>();
    attributes.put("key", "value");
    documentService.getNodeByAttributes("parentId", attributes);
    verify(em, times(1)).createQuery(anyString());
  }

  @Test
  public void getNodeByEmptyAttributesTest() {
    when(em.createQuery(anyString())).thenReturn(query);
    Map<String, String> attributes = new HashMap<>();
    documentService.getNodeByAttributes("parentId", attributes);
    verify(em, times(1)).createQuery(anyString());
  }

  @Test
  public void getNodeByNullAttributesTest() {
    when(em.createQuery(anyString())).thenReturn(query);
    documentService.getNodeByAttributes("parentId", null);
    verify(em, times(1)).createQuery(anyString());
  }

  @Test
  public void isFileNameUniqueTest() {
    assertTrue(documentService.isFileNameUnique("name", "parentNodeId"));
  }

  @Test
  public void isFileNameUniqueFalseTest() {
    when(jpaQuery.fetchCount()).thenReturn(10L);
    assertFalse(documentService.isFileNameUnique("name", "parentNodeId"));
  }

  @Test
  public void duplicateFileNamesInDirectoryTest() {
    when(jpaQuery.fetchResults()).thenReturn(QueryResults.emptyResults());
    List<String> namesList = new ArrayList<>();
    assertEquals(namesList,
        documentService.duplicateFileNamesInDirectory(Arrays.asList("one", "two"), "parent"));
  }

  @Test
  public void duplicateFileNamesInDirectoryResultsTest() {
    List<String> namesList = new ArrayList<>();

    InitTestValues initTestValues = new InitTestValues();
    List<Node> nodes = initTestValues.createListNode();

    NodeDTO nodeDTO = initTestValues.createNodeDTO(null);
    for (Node node : nodes) {
      when(nodeMapper.mapToDTO(node, true)).thenReturn(nodeDTO);
      namesList.add(nodeDTO.getName());
    }

    QueryResults<Node> queryResults = mock(QueryResults.class);
    when(jpaQuery.fetchResults()).thenReturn(queryResults);
    when(queryResults.getResults()).thenReturn(nodes);

    assertEquals(namesList,
        documentService.duplicateFileNamesInDirectory(Arrays.asList("one", "two"), "parent"));
  }

  @Test(expected = QIOException.class)
  public void getFolderAsZipNoChildrenException() throws IOException {
    PowerMockito.mockStatic(StreamsUtil.class);
    InitTestValues initTestValues = new InitTestValues();
    Node node = initTestValues.createNode(null);
    NodeDTO nodeDTO = initTestValues.createNodeDTO(null);

    node.setChildren(new ArrayList<>());
    when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);

    when(StreamsUtil.createZipOutputStream(any())).thenReturn(zipOutputStream);
    doThrow(new IOException()).when(zipOutputStream).close();
    documentService.getFolderAsZip(nodeDTO.getId(), true, true);
  }


}
