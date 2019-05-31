package com.eurodyn.qlack.fuse.cm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.exception.QAncestorFolderLockException;
import com.eurodyn.qlack.fuse.cm.exception.QFileNotFoundException;
import com.eurodyn.qlack.fuse.cm.exception.QSelectedNodeLockException;
import com.eurodyn.qlack.fuse.cm.mappers.VersionMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.QVersion;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionRepository;
import com.eurodyn.qlack.fuse.cm.storage.DBStorage;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngine;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngineFactory;
import java.io.IOException;
import java.util.Arrays;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class VersionServiceTest {

  @InjectMocks
  private VersionService versionService;

  @Mock
  private ConcurrencyControlService concurrencyControlService;

  @Mock
  private StorageEngineFactory storageEngineFactory;

  @Mock
  private StorageEngine storageEngine;

  @Mock
  private NodeRepository nodeRepository;

  @Mock
  private VersionRepository versionRepository;

  @Mock
  private VersionMapper versionMapper;

  @Mock
  private TikaConfig tika;

  private QVersion qVersion;

  private InitTestValues initTestValues;

  private Node file;
  private Node parent;
  private FileDTO fileDTO;
  private FolderDTO parentDTO;
  private VersionDTO versionDTO;
  private String FILENAME;
  private String USER_ID;
  private String LOCK_TOKEN;
  private byte[] content;

  @Before
  public void init() {
    versionService = new VersionService(concurrencyControlService, storageEngineFactory, nodeRepository, versionRepository, versionMapper);

    try {
      when(storageEngineFactory.getEngine()).thenReturn(new DBStorage());
      versionService.init();
    } catch (TikaException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

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

    FILENAME = "Test File";
    USER_ID = "user1";
    LOCK_TOKEN = "12345";
    content = new byte[5];

    fileDTO = initTestValues.createFileDTO();
    versionDTO = initTestValues.createVersionDTO();

    Arrays.fill(content, (byte) 1);
  }

  @Test(expected = QFileNotFoundException.class)
  public void testCreateVersionOfNonExistingFile() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(null);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, content, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(0)).save(any());
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testCreateVersionOfConflictingFile() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN)).thenReturn(fileDTO);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, content, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(0)).save(any());
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testCreateVersionWithConflictingParent() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN)).thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN)).thenReturn(parentDTO);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, content, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(0)).save(any());
  }

  @Test()
  public void testCreateVersionWithoutContent() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN)).thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN)).thenReturn(null);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, null, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(any());
  }

}
