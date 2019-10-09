package com.eurodyn.qlack.fuse.fileupload.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.fileupload.InitTestValues;
import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.repository.DBFileRepository;
import com.eurodyn.qlack.fuse.fileupload.service.impl.FileUploadImpl;
import com.eurodyn.qlack.util.av.api.dto.VirusScanDTO;
import com.eurodyn.qlack.util.av.api.exception.VirusFoundException;
import com.eurodyn.qlack.util.av.api.exception.VirusScanException;
import com.eurodyn.qlack.util.av.api.service.AvService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadImplClamAvTest {

  @InjectMocks
  private FileUploadImpl fileUpload;

  @Mock
  private DBFileRepository dbFileRepository;

  @Mock
  private AvService avService;

  private InitTestValues initTestValues;

  private DBFileDTO dbFileDTO;

  private VirusScanDTO virusScanDTO;

  @Before
  public void test() {
    fileUpload = new FileUploadImpl(dbFileRepository, Optional.of(avService));
    initTestValues = new InitTestValues();
    dbFileDTO = initTestValues.createDBFileDTO();
    virusScanDTO = initTestValues.createVirusScanVirusFreeDTO();
  }

  @Test
  public void uploadSafeFileTest() {
    ReflectionTestUtils.setField(fileUpload, "isVirusScanEnabled", true);
    when(avService.virusScan(dbFileDTO.getFileData())).thenReturn(virusScanDTO);
    fileUpload.upload(dbFileDTO);
    verify(dbFileRepository, times(1)).getChunk(dbFileDTO.getId(), dbFileDTO.getChunkNumber());
    verify(dbFileRepository, times(1)).save(any(DBFile.class));
  }

  @Test(expected = VirusFoundException.class)
  public void uploadUnsafeFileTest() {
    ReflectionTestUtils.setField(fileUpload, "isVirusScanEnabled", true);
    virusScanDTO.setVirusFree(false);
    when(avService.virusScan(dbFileDTO.getFileData())).thenReturn(virusScanDTO);
    fileUpload.upload(dbFileDTO);
    verify(dbFileRepository, times(1)).getChunk(dbFileDTO.getId(), dbFileDTO.getChunkNumber());
  }

  @Test
  public void uploadVirusExceptionTest() {
    ReflectionTestUtils.setField(fileUpload, "isVirusScanEnabled", true);
    when(avService.virusScan(dbFileDTO.getFileData())).thenThrow(new VirusScanException());
    fileUpload.upload(dbFileDTO);
    verify(dbFileRepository, times(1)).getChunk(dbFileDTO.getId(), dbFileDTO.getChunkNumber());
  }

}
