package com.eurodyn.qlack.fuse.cm.service;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.exception.QIOException;
import com.eurodyn.qlack.fuse.cm.mapper.NodeMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;

import com.eurodyn.qlack.fuse.cm.util.JPAQueryUtil;
import com.eurodyn.qlack.fuse.cm.util.StreamsUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceEMTest {
//
//  @InjectMocks
//  private DocumentService documentService;
//
//  @Mock
//  private ConcurrencyControlService concurrencyControlService;
//
//  @Mock
//  private VersionService versionService;
//
//  @Mock
//  private NodeRepository nodeRepository;
//
//  @Mock
//  private NodeMapper nodeMapper;
//
//  @Mock
//  private EntityManager em;
//
//  @Mock
//  private Query query;
//
//  @Mock
//  private JPAQuery<Node> jpaQuery;
//
//  @Mock
//  private JPAQueryFactory jpaQueryFactory;
//
//  @Mock
//  private ZipOutputStream zipOutputStream;
//
//    private MockedStatic<StreamsUtil> mockedStatic;
//
//    private MockedStatic<JPAQueryUtil> mockedStatic2;
//
//    private MockedStatic<QueryResults> mockedStatic3;
//    private MockedConstruction<JPAQueryFactory> mockedJqf;
//
//  @BeforeEach
//  public void init() throws Exception {
//    documentService = new DocumentService(concurrencyControlService,
//      versionService, nodeRepository,
//      nodeMapper);
//
//
//
//    ReflectionTestUtils.setField(documentService, "em", em);
//
//      mockedStatic = mockStatic(StreamsUtil.class);
//      mockedStatic2 = mockStatic(JPAQueryUtil.class);
//      mockedStatic3 = mockStatic(QueryResults.class);
//
//      mockedJqf = Mockito.mockConstruction(JPAQueryFactory.class,
//              (mock, context) -> {
//                  when(mock).thenReturn(jpaQueryFactory);
//              });
//
//  }
//
//    @AfterEach
//    public void close() {
//
//      mockedStatic.close();
//      mockedStatic2.close();
//      mockedStatic3.close();
//      mockedJqf.close();
//    }
//
//  @Test
//  public void getNodeByAttributesTest() {
//    when(em.createQuery(anyString())).thenReturn(query);
//    Map<String, String> attributes = new HashMap<>();
//    attributes.put("key", "value");
//    documentService.getNodeByAttributes("parentId", attributes);
//    verify(em, times(1)).createQuery(anyString());
//  }
//
//  @Test
//  public void getNodeByEmptyAttributesTest() {
//    when(em.createQuery(anyString())).thenReturn(query);
//    Map<String, String> attributes = new HashMap<>();
//    documentService.getNodeByAttributes("parentId", attributes);
//    verify(em, times(1)).createQuery(anyString());
//  }
//
//  @Test
//  public void getNodeByNullAttributesTest() {
//    when(em.createQuery(anyString())).thenReturn(query);
//    documentService.getNodeByAttributes("parentId", null);
//    verify(em, times(1)).createQuery(anyString());
//  }
//
//  @Test
//  public void isFileNameUniqueTest() {
//    String name = "uniqueName";
//    String parentNodeID = "parentNodeID";
//
//    when(JPAQueryUtil.createJpaQueryForName(em, name, parentNodeID)).thenReturn(jpaQuery);
//    when(jpaQuery.fetchCount()).thenReturn(0L);
//
//    boolean isUnique = documentService.isFileNameUnique(name, parentNodeID);
//
//    assertTrue(isUnique, "Expected true when file name is unique");
//  }
//
//  @Test
//  public void isFileNameUniqueFalseTest() {
//    String name = "notUniqueName";
//    String parentNodeID = "parentNodeID";
//
//    when(JPAQueryUtil.createJpaQueryForName(em, name, parentNodeID)).thenReturn(jpaQuery);
//    when(jpaQuery.fetchCount()).thenReturn(10L);
//
//    boolean isUnique = documentService.isFileNameUnique(name, parentNodeID);
//
//    assertFalse(isUnique, "Expected false when file name is not unique");
//  }
///*
//  @Test
//  public void duplicateFileNamesInDirectoryTest() {
//    QueryResults<Node> queryResults = QueryResults.emptyResults();
//    when(jpaQuery.fetchResults()).thenReturn(queryResults);
//    List<String> namesList = new ArrayList<>();
//    assertEquals(namesList,
//      documentService
//        .duplicateFileNamesInDirectory(Arrays.asList("one", "two"), "parent"));
//
//
//  }
//
//  @Test
//  public void duplicateFileNamesInDirectoryResultsTest() {
//    List<String> namesList = new ArrayList<>();
//
//    InitTestValues initTestValues = new InitTestValues();
//    List<Node> nodes = initTestValues.createListNode();
//
//    NodeDTO nodeDTO = initTestValues.createNodeDTO(null);
//    for (Node node : nodes) {
//      when(nodeMapper.mapToDTO(node, true)).thenReturn(nodeDTO);
//      namesList.add(nodeDTO.getName());
//    }
//
//    QueryResults<Node> queryResults = mock(QueryResults.class);
//    when(jpaQuery.fetchResults()).thenReturn(queryResults);
//    when(queryResults.getResults()).thenReturn(nodes);
//
//    assertEquals(namesList,
//      documentService
//        .duplicateFileNamesInDirectory(Arrays.asList("one", "two"), "parent"));
//  }
//*/
//  @Test
//  public void getFolderAsZipNoChildrenException() throws IOException {
//    InitTestValues initTestValues = new InitTestValues();
//    Node node = initTestValues.createNode(null);
//    NodeDTO nodeDTO = initTestValues.createNodeDTO(null);
//
//    node.setChildren(new ArrayList<>());
//    assertThrows(QIOException.class, () -> {
//        when(nodeRepository.fetchById(nodeDTO.getId())).thenReturn(node);
//
//        when(StreamsUtil.createZipOutputStream(any())).thenReturn(zipOutputStream);
//        doThrow(new IOException()).when(zipOutputStream).close();
//        documentService.getFolderAsZip(nodeDTO.getId(), true, true);
//    });
//  }


}
