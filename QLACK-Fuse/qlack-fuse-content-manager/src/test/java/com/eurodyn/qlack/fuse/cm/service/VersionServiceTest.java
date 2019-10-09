package com.eurodyn.qlack.fuse.cm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.cm.InitTestValues;
import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.exception.QAncestorFolderLockException;
import com.eurodyn.qlack.fuse.cm.exception.QSelectedNodeLockException;
import com.eurodyn.qlack.fuse.cm.mapper.VersionMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.QVersion;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionBinRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionDeletedRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionRepository;
import com.eurodyn.qlack.fuse.cm.storage.DBStorage;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngine;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngineFactory;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import com.querydsl.core.types.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.tika.exception.TikaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

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
  private VersionBinRepository versionBinRepository;

  @Mock
  private VersionDeletedRepository versionDeletedRepository;

  @Mock
  private VersionMapper versionMapper;

  private QVersion qVersion;

  private InitTestValues initTestValues;

  private Node file;
  private Node parent;
  private FileDTO fileDTO;
  private FolderDTO parentDTO;
  private Version version;
  private VersionDTO versionDTO;
  private List<Version> versions;
  private List<VersionDTO> versionsDTO;
  private String FILENAME;
  private String USER_ID;
  private String LOCK_TOKEN;
  private byte[] content;

  @Before
  public void init() {
    versionService = new VersionService(concurrencyControlService, storageEngineFactory,
        nodeRepository, versionRepository, versionDeletedRepository, versionMapper);

    try {
      when(storageEngineFactory.getEngine())
          .thenReturn(new DBStorage(versionRepository, versionBinRepository));
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
    version = initTestValues.createVersion();
    version.setNode(file);

    Arrays.fill(content, (byte) 1);

    qVersion = new QVersion("version");

    versions = initTestValues.createVersions();
    versionsDTO = initTestValues.createVersionsDTO();
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testCreateVersionOfConflictingFile() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, content, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(0)).save(any());
  }

  @Test(expected = QAncestorFolderLockException.class)
  public void testCreateVersionWithConflictingParent() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(parentDTO);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, content, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(0)).save(any());
  }

  @Test()
  public void testCreateVersionWithoutContent() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, null, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(any());
  }

  @Test()
  public void testCreateVersionWithoutContentAndCustom() {
    VersionAttributeDTO versionAttributeDTO = new VersionAttributeDTO();
    versionAttributeDTO.setName("Test Attribute");
    versionAttributeDTO.setName("Test value");
    versionAttributeDTO.setVersionId(versionDTO.getId());

    versionDTO.getAttributes().add(versionAttributeDTO);

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.createVersion(file.getId(), versionDTO, FILENAME, null, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(any());
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testUpdateVersionOfConflictedFile() {
    when(nodeRepository.fetchById(any())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);
    versionService.updateVersion(file.getId(), versionDTO, null, USER_ID, true, LOCK_TOKEN);
  }

  @Test
  public void testUpdateVersion() {
    long size = 123456789L;
    Predicate predicate = qVersion.name.eq(version.getName())
        .and(qVersion.node.id.eq(file.getId()));

    when(nodeRepository.fetchById(any())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(version));

    versionDTO.setName("New Name");
    versionDTO.setSize(size);

    versionService.updateVersion(file.getId(), versionDTO, null, USER_ID, true, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(version);
    assertEquals("New Name", version.getName());
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testDeleteVersionOfConflictedNode() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);
    versionService.deleteVersion(version.getId(), LOCK_TOKEN);
  }

  @Test
  public void testDeleteVersion() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.deleteVersion(version.getId(), LOCK_TOKEN);

    verify(versionRepository, times(1)).delete(version);
  }

  @Test
  public void testGetVersionById() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    when(versionMapper.mapToDTO(version)).thenReturn(versionDTO);

    VersionDTO versionById = versionService.getVersionById(version.getId());

    assertEquals(versionDTO, versionById);
  }

  @Test
  public void testGetFileVersions() {
    commonMocks();
    List<VersionDTO> fileVersionsDTO = versionService.getFileVersions(file.getId());

    assertEquals(versionsDTO, fileVersionsDTO);
  }

  @Test
  public void testGetFileLatestVersion() {
    commonMocks();
    VersionDTO fileLatestVersion = versionService.getFileLatestVersion(file.getId());

    assertEquals(versionsDTO.get(1), fileLatestVersion);
  }

  @Test
  public void testGetFileLatestVersionNull() {
    Predicate predicate = qVersion.node.eq(file);
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(predicate)).thenReturn(new ArrayList<>());

    VersionDTO fileLatestVersion = versionService.getFileLatestVersion(file.getId());

    assertNull(fileLatestVersion);
  }

  private void commonMocks() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
  }

  @Test
  public void getBinContentWithoutVersionName() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    when(versionMapper.mapToEntity(any(VersionDTO.class))).thenReturn(version);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);

    versionService.getBinContent(file.getId());

    verify(versionBinRepository, times(1)).findByVersionOrderByChunkIndex(version);
  }

  @Test
  public void testGetBinContent() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(version));

    versionService.getBinContent(file.getId(), version.getName());

    verify(versionBinRepository, times(1)).findByVersionOrderByChunkIndex(version);
  }

  @Test
  public void testGetFileAsZipNoVersionName() {
    Version latestVersion = versions.get(1);
    commonMocks();
    when(versionMapper.mapToEntity(versionsDTO.get(1))).thenReturn(latestVersion);
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(latestVersion));

    byte[] fileAsZip = versionService.getFileAsZip(file.getId(), true);

    assertNotNull(fileAsZip);
    verify(versionRepository, times(1)).findOne(any(Predicate.class));

  }

  @Test
  public void testGetFileAsZip() {
    version.setAttributes(initTestValues.createVersionAttributes(version));

    commonMocks();
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(version));

    byte[] fileAsZip = versionService.getFileAsZip(file.getId(), version.getName(), true);

    assertNotNull(fileAsZip);
    verify(versionRepository, times(2)).findOne(any(Predicate.class));
  }

  @Test
  public void testSetBinChunk() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    versionService.setBinChunk(version.getId(), content, 1);

    verify(versionBinRepository, times(1)).save(any(VersionBin.class));
    verify(versionRepository, times(1)).save(version);
  }

  @Test
  public void testSetBinChunkIndexDifferentThanOne() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    versionService.setBinChunk(version.getId(), content, not(eq(1)));

    verify(versionBinRepository, times(1)).save(any(VersionBin.class));
    verify(versionRepository, times(0)).save(version);
  }

  @Test
  public void testGetBinChunk() {
    when(versionRepository.fetchById(version.getId())).thenReturn(version);
    BinChunkDTO binChunk = versionService.getBinChunk(version.getId(), 1);

    verify(versionBinRepository, times(1)).findAll(any(Predicate.class), any(Sort.class));
  }

  @Test
  public void testUpdateAttributeWithoutVersionName() {
    version.setAttributes(new ArrayList<>());

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToEntity(any(VersionDTO.class))).thenReturn(version);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService
        .updateAttribute(file.getId(), CMConstants.LOCKABLE, "true", "NEW_USER", LOCK_TOKEN);

    verify(versionRepository, times(1)).save(version);
    assertNotNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }

  @Test
  public void testUpdateAttributeWithoutVersionNameWithoutUser() {
    version.setAttributes(new ArrayList<>());

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToEntity(any(VersionDTO.class))).thenReturn(version);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.updateAttribute(file.getId(), CMConstants.LOCKABLE, "true", null, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(version);
    assertEquals(1, version.getAttributes().size());
    assertNotNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testUpdateAttributeWithoutVersionNameAndConflicted() {
    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);

    versionService
        .updateAttribute(file.getId(), CMConstants.LOCKABLE, "true", "NEW_USER", LOCK_TOKEN);

    verify(versionRepository, times(0)).save(version);
  }

  @Test
  public void testUpdateAttributeWithVersionName() {
    version.setAttributes(new ArrayList<>());

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(version));
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService
        .updateAttribute(file.getId(), CMConstants.LOCKABLE, "true", "NEW_USER", LOCK_TOKEN,
            version.getName());

    verify(versionRepository, times(1)).save(version);
    assertNotNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }


  @Test
  public void testUpdateAttributesWithoutVersionName() {
    Map<String, String> newAttributes = new HashMap<>();
    newAttributes.put(CMConstants.LOCKABLE, "true");
    newAttributes.put(CMConstants.ATTR_CREATED_BY, "Some User");

    version.setAttributes(new ArrayList<>());

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToEntity(any(VersionDTO.class))).thenReturn(version);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.updateAttributes(file.getId(), newAttributes, "NEW_USER", LOCK_TOKEN);

    verify(versionRepository, times(1)).save(version);
    assertNotNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNotNull(version.getAttribute(CMConstants.ATTR_CREATED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }


  @Test
  public void testUpdateAttributesWithVersionName() {
    Map<String, String> newAttributes = new HashMap<>();
    newAttributes.put(CMConstants.LOCKABLE, "true");
    newAttributes.put(CMConstants.ATTR_CREATED_BY, "Some User");

    version.setAttributes(new ArrayList<>());

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(version));
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService
        .updateAttributes(file.getId(), newAttributes, "NEW_USER", LOCK_TOKEN, version.getName());

    verify(versionRepository, times(1)).save(version);
    assertNotNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNotNull(version.getAttribute(CMConstants.ATTR_CREATED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testUpdateAttributesWithVersionNameAndConflicted() {
    Map<String, String> newAttributes = new HashMap<>();
    newAttributes.put(CMConstants.LOCKABLE, "true");
    newAttributes.put(CMConstants.ATTR_CREATED_BY, "Some User");

    version.setAttributes(new ArrayList<>());

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);

    versionService
        .updateAttributes(file.getId(), newAttributes, "NEW_USER", LOCK_TOKEN, version.getName());

    verify(versionRepository, times(0)).save(version);
    assertEquals(0, version.getAttributes().size());
  }

  @Test
  public void testDeleteAttributeWithoutVersionName() {
    version.setAttribute(CMConstants.LOCKABLE, "true");

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToEntity(any(VersionDTO.class))).thenReturn(version);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.deleteAttribute(file.getId(), CMConstants.LOCKABLE, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(version);
    assertNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNotNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }

  @Test
  public void testDeleteAttributeWithoutVersionNameAndWithoutUser() {
    version.setAttribute(CMConstants.LOCKABLE, "true");

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findAll(any(Predicate.class))).thenReturn(versions);
    when(versionMapper.mapToEntity(any(VersionDTO.class))).thenReturn(version);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.deleteAttribute(file.getId(), CMConstants.LOCKABLE, null, LOCK_TOKEN);

    verify(versionRepository, times(1)).save(version);
    assertNull(version.getAttribute(CMConstants.LOCKABLE));
    assertNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_BY));
    assertNull(version.getAttribute(CMConstants.ATTR_LAST_MODIFIED_ON));
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testDeleteAttributeWithoutVersionNameAndConflicted() {
    version.setAttribute(CMConstants.LOCKABLE, "true");

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);

    versionService.deleteAttribute(file.getId(), CMConstants.LOCKABLE, USER_ID, LOCK_TOKEN);

    verify(versionRepository, times(0)).save(version);
  }

  @Test
  public void testDeleteAttributeWithVersionName() {
    version.setAttribute(CMConstants.LOCKABLE, "true");

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(versionRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(version));
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(null);

    versionService.deleteAttribute(file.getId(), CMConstants.LOCKABLE, "NEW_USER", LOCK_TOKEN,
        version.getName());

    verify(versionRepository, times(1)).save(version);
    assertNull(version.getAttribute(CMConstants.LOCKABLE));
  }

  @Test(expected = QSelectedNodeLockException.class)
  public void testDeleteAttributeWithVersionNameConflicted() {
    version.setAttribute(CMConstants.LOCKABLE, "true");

    when(nodeRepository.fetchById(file.getId())).thenReturn(file);
    when(concurrencyControlService.getSelectedNodeWithLockConflict(file.getId(), LOCK_TOKEN))
        .thenReturn(fileDTO);

    versionService.deleteAttribute(file.getId(), CMConstants.LOCKABLE, "NEW_USER", LOCK_TOKEN,
        version.getName());

    verify(versionRepository, times(0)).save(version);
    assertNotNull(version.getAttribute(CMConstants.LOCKABLE));
  }

  @Test
  public void getVersionsByFilenameForFile() {

    List<String> fileNames = new ArrayList<>();
    fileNames.add(version.getFilename());
    fileNames.add("ANOTHER FILE NAME");

    Predicate predicate = qVersion.filename.in(fileNames).and(qVersion.node.id.eq(file.getId()));
    when(versionRepository.findAll(predicate)).thenReturn(versions);
    when(versionMapper.mapToDTO(versions)).thenReturn(versionsDTO);

    List<VersionDTO> versionsByFilenameForFile = versionService
        .getVersionsByFilenameForFile(file.getId(), fileNames);

    assertEquals(versionsDTO, versionsByFilenameForFile);
    verify(versionRepository, times(1)).findAll(predicate);
  }

}
