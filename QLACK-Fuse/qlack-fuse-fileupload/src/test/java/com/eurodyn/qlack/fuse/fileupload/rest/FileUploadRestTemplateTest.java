package com.eurodyn.qlack.fuse.fileupload.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.service.FileUpload;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadRestTemplateTest {

  private FileUploadRestTemplate fileUploadRestTemplate;

  @Mock
  private FileUpload fileUpload;

  @Mock
  private MultipartHttpServletRequest multipartHttpServletRequest;

  @Mock
  private MultipartFile multipartFile;

  @Before
  public void init() {
    fileUploadRestTemplate = spy(FileUploadRestTemplate.class);
    ReflectionTestUtils.setField(fileUploadRestTemplate, "fileUpload", fileUpload);
  }

  @Test
  public void checkChunkTest() {
    when(fileUpload.checkChunk("id", 1L)).thenReturn(false);
    fileUploadRestTemplate.checkChunk("id", 1L);
    verify(fileUpload, times(1)).checkChunk("id", 1L);
  }

  @Test
  public void checkChunkExistsTest() {
    when(fileUpload.checkChunk("id", 1L)).thenReturn(true);
    fileUploadRestTemplate.checkChunk("id", 1L);
    verify(fileUpload, times(1)).checkChunk("id", 1L);
  }

  @Test
  public void uploadTest() {
    fileUploadRestTemplate.upload(multipartHttpServletRequest);
    verify(fileUpload, times(1)).upload(any(DBFileDTO.class));
  }

  @Test
  public void uploadExceptionTest() {
    when(multipartHttpServletRequest.getParameter("flowChunkNumber")).thenReturn("null");
    fileUploadRestTemplate.upload(multipartHttpServletRequest);
    verify(fileUpload, times(0)).upload(any(DBFileDTO.class));
  }

  @Test
  public void uploadWithValuesTest() throws IOException {
    when(multipartHttpServletRequest.getParameter("flowChunkNumber")).thenReturn("2");
    when(multipartHttpServletRequest.getParameter("flowChunkSize")).thenReturn("100");
    when(multipartHttpServletRequest.getParameter("flowTotalChunks")).thenReturn("5");
    when(multipartHttpServletRequest.getParameter("flowTotalSize")).thenReturn("430");
    when(multipartHttpServletRequest.getFile("file")).thenReturn(multipartFile);
    when(multipartFile.getInputStream())
        .thenReturn(IOUtils.toInputStream("file", StandardCharsets.UTF_8));
    fileUploadRestTemplate.upload(multipartHttpServletRequest);
    verify(fileUpload, times(1)).upload(any(DBFileDTO.class));
  }

}
