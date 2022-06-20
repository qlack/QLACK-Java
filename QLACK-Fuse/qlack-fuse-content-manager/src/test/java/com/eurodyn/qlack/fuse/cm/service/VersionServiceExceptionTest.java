package com.eurodyn.qlack.fuse.cm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.exception.QIOException;
import com.eurodyn.qlack.fuse.cm.mapper.VersionMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionBinRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionDeletedRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionRepository;
import com.eurodyn.qlack.fuse.cm.storage.DBStorage;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngine;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngineFactory;
import com.eurodyn.qlack.fuse.cm.util.StreamsUtil;
import com.querydsl.core.types.Predicate;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipOutputStream;
import org.apache.tika.exception.TikaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(StreamsUtil.class)
//@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.dom.*"})
public class VersionServiceExceptionTest {
/*
  @InjectMocks
  private VersionService versionService;

  @Mock
  private ConcurrencyControlService concurrencyControlService;

  @Mock
  private StorageEngineFactory storageEngineFactory;

  @Mock
  private NodeRepository nodeRepository;

  @Mock
  private VersionRepository versionRepository;

  @Mock
  private VersionDeletedRepository versionDeletedRepository;

  @Mock
  private VersionMapper versionMapper;

  @Mock
  private VersionBinRepository versionBinRepository;

  @Mock
  private StorageEngine storageEngine;

  @Mock
  private ZipOutputStream zipOutputStream;

  @Mock
  private InputStream inputStream;

  private InitTestValues initTestValues;

  private Node file;
  private Node parent;
  private FileDTO fileDTO;
  private FolderDTO parentDTO;
  private Version version;
  private VersionDTO versionDTO;
  private List<Version> versions;
  private List<VersionDTO> versionsDTO;

  private byte[] content = new byte[5];

  @Before
  public void init() throws TikaException, IOException {
    versionService = new VersionService(concurrencyControlService,
      storageEngineFactory,
      nodeRepository, versionRepository, versionDeletedRepository,
      versionMapper);
    when(storageEngineFactory.getEngine())
      .thenReturn(new DBStorage(versionRepository, versionBinRepository));
    versionService.init();

    initTestValues = new InitTestValues();

    parent = initTestValues.createNode(null);
    parentDTO = initTestValues.createFolderDTO(parent.getId());

    file = initTestValues.createNode("1620e90c-d023-4dfe-b1b6-f56eea05afb5");
    file.setType(NodeType.FILE);
    file.setParent(parent);

    fileDTO = initTestValues.createFileDTO();
    fileDTO.setId("1620e90c-d023-4dfe-b1b6-f56eea05afb5");
    fileDTO.setParentId(parentDTO.getId());

    parent.getChildren().add(file);
    parentDTO.getChildren().add(fileDTO);

    fileDTO = initTestValues.createFileDTO();
    versionDTO = initTestValues.createVersionDTO();
    version = initTestValues.createVersion();
    version.setNode(file);

    versions = initTestValues.createVersions();
    versionsDTO = initTestValues.createVersionsDTO();

    PowerMockito.mockStatic(StreamsUtil.class);
  }

  @Test(expected = QIOException.class)
  public void testGetFileAsZip() throws IOException {
    version.setAttributes(initTestValues.createVersionAttributes(version));

    when(StreamsUtil.createZipOutputStream(any())).thenReturn(zipOutputStream);
    doThrow(new IOException()).when(zipOutputStream).close();

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
    when(versionRepository.findOne(any(Predicate.class)))
      .thenReturn(Optional.of(version));

    versionService.getFileAsZip(file.getId(), version.getName(), true);
  }


  @Test
  public void setBinChunkNoMimetypeIoExceptionTest() throws IOException {
    when(StreamsUtil.createInputStream(content)).thenReturn(inputStream);
    doThrow(new IOException()).when(inputStream).close();

    versionService.getMimeType(content);
    verify(inputStream, times(1)).close();
  }
*/
}
