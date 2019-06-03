package com.eurodyn.qlack.fuse.fileupload.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import com.eurodyn.qlack.fuse.fileupload.InitTestValues;
import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileNotCompletedException;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileNotFoundException;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.repository.DBFileRepository;
import com.eurodyn.qlack.fuse.fileupload.service.impl.FileUploadImpl;
import com.eurodyn.qlack.util.av.api.service.AvService;
import com.querydsl.core.types.Predicate;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class FileUploadImplTest {

  @InjectMocks
  private FileUploadImpl fileUploadImpl;

  private DBFileRepository dbFileRepository = mock(DBFileRepository.class);
  private AvService clamAvService = mock(AvService.class);
  private InitTestValues initTestValues;
  private DBFile chunk;
  private DBFileDTO dbFileDTO;
  private List<DBFile> dbFiles;
  private List<String> chunksIds;
  private final String dbFileId = "ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c";
  private final String chunkId = "499dcb76-c8b9-42a8-babe-bd5b917fbc4e";
  private final Long chunkNumber = 1L;
  private final Long chunkIndex = 1L;

  @Before
  public void init() {
    fileUploadImpl = new FileUploadImpl(dbFileRepository, clamAvService);
    initTestValues = new InitTestValues();
    dbFileDTO = initTestValues.createDBFileDTO();
    dbFiles = initTestValues.createDBFiles();
    chunk = initTestValues.createChunkNo1();
    chunksIds = initTestValues.createChunkIds();
  }

  @Test
  public void testCheckChunkExists() {
    when(dbFileRepository.getChunk(chunkId, chunkNumber)).thenReturn(chunk);
    boolean checkChunk = fileUploadImpl.checkChunk(chunkId, chunkNumber);
    assertTrue(checkChunk);
  }

  @Test
  public void testCheckChunkNotExists() {
    when(dbFileRepository.getChunk(chunkId, chunkNumber)).thenReturn(null);
    boolean checkChunk = fileUploadImpl.checkChunk(chunkId, chunkNumber);
    assertFalse(checkChunk);
  }


  @Test
  public void deleteByID() {
    fileUploadImpl.deleteByID(dbFileId);
    verify(dbFileRepository, times(1)).deleteById(dbFileId);
  }

  @Test
  public void testGetByIDAndChunk() {
    when(dbFileRepository.findAll(any(Predicate.class), any(Sort.class))).thenReturn(dbFiles);
    DBFileDTO dbfDTO = fileUploadImpl.getByIDAndChunk(dbFileId, chunkIndex);
    assertEquals(dbFileDTO.getId(), dbfDTO.getId());
    assertEquals(dbFileDTO.getChunkNumber(), dbfDTO.getChunkNumber());
  }

  @Test(expected = QFileNotFoundException.class)
  public void testGetByIDAndChunkFileNotFoundException() {
    when(dbFileRepository.findAll(any(Predicate.class), any(Sort.class))).thenReturn(new ArrayList<>());
    DBFileDTO dbfDTO = fileUploadImpl.getByIDAndChunk(dbFileId, chunkIndex);
    assertEquals(dbFileDTO.getId(), dbfDTO.getId());
    assertEquals(dbFileDTO.getChunkNumber(), dbfDTO.getChunkNumber());
  }

  @Test
  public void testListFilesIncludeBinaryContentTrue() {
    when(dbFileRepository.findAll(Sort.by("UploadedAt"))).thenReturn(dbFiles);
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    List<DBFileDTO> dbFileDTOList = fileUploadImpl.listFiles(true);
    assertEquals(chunksIds.size(), dbFileDTOList.size());
  }

  @Test
  public void testListFilesIncludeBinaryContentFalse() {
    when(dbFileRepository.findAll(Sort.by("UploadedAt"))).thenReturn(dbFiles);
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    List<DBFileDTO> dbFileDTOList = fileUploadImpl.listFiles(false);
    assertEquals(chunksIds.size(), dbFileDTOList.size());
  }

  @Test
  public void testGetByID() {
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    fileUploadImpl.getByID("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
    assertEquals(dbFileDTO.getId(), dbFiles.get(0).getDbFilePK().getId());
  }

  @Test(expected = QFileNotFoundException.class)
  public void testGetByIDEmptyDBFilesList() {
    when(dbFileRepository.findAll()).thenReturn(new ArrayList<>());
    fileUploadImpl.getByID("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");

  }

  @Test
  public void testGetByIDIncludeBinary() {
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    DBFileDTO dbFileDTO = fileUploadImpl.getByID("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
    assertEquals(dbFileDTO.getId(), dbFiles.get(0).getDbFilePK().getId());
  }

  @Test(expected = QFileNotCompletedException.class)
  public void testGetByIDIncludeBinaryWithFileNotCompletedException() {
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    dbFiles.forEach(dbFile1 -> dbFile1.setExpectedChunks(2));
    fileUploadImpl.getByID("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
  }

  @Test
  public void cleanupExpiredFilesEmpty() {
    ReflectionTestUtils.setField(fileUploadImpl, "cleanupEnabled", true);
    when(dbFileRepository.findAll()).thenReturn(new ArrayList<>());
    fileUploadImpl.cleanupExpired();
  }

  @Test
  public void cleanupExpiredDeleteBeforeFilesEmpty() {
    ReflectionTestUtils.setField(fileUploadImpl, "cleanupEnabled", true);
    when(dbFileRepository.findAll()).thenReturn(new ArrayList<>());
    fileUploadImpl.cleanupExpired(System.currentTimeMillis());
  }

  @Test
  public void deleteByIDForConsole() {
    fileUploadImpl.deleteByIDForConsole(dbFileId);
    verify(dbFileRepository, times(1)).deleteById(dbFileId);
  }

  @Test
  public void testGetByIDForConsole() {
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    DBFileDTO dbFileDTO = fileUploadImpl.getByIDForConsole(dbFileId);
    assertEquals(dbFileDTO.getId(), dbFileId);
  }

  @Test
  public void testListFilesForConsoleIncludeBinaryContentTrue() {
    when(dbFileRepository.findAll(Sort.by("uploadedAt"))).thenReturn(dbFiles);
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    List<DBFileDTO> dbFileDTOList = fileUploadImpl.listFilesForConsole(true);
    assertEquals(chunksIds.size(), dbFileDTOList.size());
  }

  @Test
  public void testListFilesForConsoleIncludeBinaryContentFalse() {
    when(dbFileRepository.findAll(Sort.by("uploadedAt"))).thenReturn(dbFiles);
    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    List<DBFileDTO> dbFileDTOList = fileUploadImpl.listFilesForConsole(false);
    assertEquals(chunksIds.size(), dbFileDTOList.size());
  }
}
