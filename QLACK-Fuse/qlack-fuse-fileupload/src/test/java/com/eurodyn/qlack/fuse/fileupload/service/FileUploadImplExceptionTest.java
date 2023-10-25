package com.eurodyn.qlack.fuse.fileupload.service;

import com.eurodyn.qlack.fuse.fileupload.InitTestValues;
import com.eurodyn.qlack.fuse.fileupload.exception.QFileUploadException;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.repository.DBFileRepository;
import com.eurodyn.qlack.fuse.fileupload.service.impl.FileUploadImpl;
import com.eurodyn.qlack.fuse.fileupload.util.ByteArrayOutputStreamUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class FileUploadImplExceptionTest {

  @InjectMocks
  private FileUploadImpl fileUpload;

  @Mock
  private DBFileRepository dbFileRepository;

  private InitTestValues initTestValues;

  private List<DBFile> dbFiles;

    private MockedStatic<ByteArrayOutputStreamUtil> mockedStatic;

  @BeforeEach
  public void init() {
    fileUpload = new FileUploadImpl(dbFileRepository, Optional.empty());
    initTestValues = new InitTestValues();
    dbFiles = initTestValues.createDBFiles();

      mockedStatic = mockStatic(ByteArrayOutputStreamUtil.class);
  }

    @AfterEach
    public void close() {
        mockedStatic.close();
    }

  @Test
  public void getByIDIncludeBinaryExceptionTest(){
      assertThrows(QFileUploadException.class, () -> {
          ByteArrayOutputStream byteArrayOutputStream = Mockito
                  .mock(ByteArrayOutputStream.class);
          when(ByteArrayOutputStreamUtil.createByteArrayOutputStream(anyLong()))
                  .thenReturn(byteArrayOutputStream);
          doThrow(new IOException()).when(byteArrayOutputStream).write(any());

          when(dbFileRepository.findAll()).thenReturn(dbFiles);
          fileUpload.getByID("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
      });
  }


}
