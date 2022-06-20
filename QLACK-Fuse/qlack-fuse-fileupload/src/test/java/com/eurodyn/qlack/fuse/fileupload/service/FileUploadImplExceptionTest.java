package com.eurodyn.qlack.fuse.fileupload.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.fileupload.InitTestValues;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileUploadException;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.repository.DBFileRepository;
import com.eurodyn.qlack.fuse.fileupload.service.impl.FileUploadImpl;
import com.eurodyn.qlack.fuse.fileupload.util.ByteArrayOutputStreamUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author European Dynamics
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(ByteArrayOutputStreamUtil.class)
public class FileUploadImplExceptionTest {
/*
  @InjectMocks
  private FileUploadImpl fileUpload;

  @Mock
  private DBFileRepository dbFileRepository;

  private InitTestValues initTestValues;

  private List<DBFile> dbFiles;

  @Before
  public void init() {
    fileUpload = new FileUploadImpl(dbFileRepository, Optional.empty());
    initTestValues = new InitTestValues();
    dbFiles = initTestValues.createDBFiles();
  }

  @Test(expected = QFileUploadException.class)
  public void getByIDIncludeBinaryExceptionTest() throws IOException {
    PowerMockito.mockStatic(ByteArrayOutputStreamUtil.class);
    ByteArrayOutputStream byteArrayOutputStream = PowerMockito
      .mock(ByteArrayOutputStream.class);
    when(ByteArrayOutputStreamUtil.createByteArrayOutputStream(anyLong()))
      .thenReturn(byteArrayOutputStream);
    doThrow(new IOException()).when(byteArrayOutputStream).write(any());

    when(dbFileRepository.findAll()).thenReturn(dbFiles);
    fileUpload.getByID("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
  }
  
 */
}
