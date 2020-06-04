package com.eurodyn.qlack.fuse.workflow.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.crypto.service.CryptoDigestService;
import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
public class CamundaProcessInitServiceTest {

  @InjectMocks
  private CamundaProcessInitService processInitService;
  @Mock
  private ProcessFileRepository processFileRepository;
  @Mock
  private RepositoryService repositoryService;
  @Mock
  private CryptoDigestService cryptoDigestService;
  @Mock
  private DeploymentBuilder deploymentBuilder;

  private Resource[] resources;

  @Before
  public void init() throws IOException {
    resources = new PathMatchingResourcePatternResolver()
        .getResources("/processes/*.bpmn");

    ReflectionTestUtils.setField(processInitService, "resources", resources);
    when(cryptoDigestService.sha256(any(InputStream.class))).thenReturn("someChecksum");
    when(repositoryService.createDeployment()).thenReturn(deploymentBuilder);
    when(deploymentBuilder.addClasspathResource(anyString())).thenReturn(deploymentBuilder);
  }

  @Test
  public void newProcessIsSuccessful() throws IOException {
    when(processFileRepository.findOneByFilename(anyString())).thenReturn(null);

    processInitService.updateProcessesFromResources();

    verify(processFileRepository, times(resources.length)).findOneByFilename(anyString());
    verify(cryptoDigestService, times(resources.length)).sha256(any(InputStream.class));
    verify(processFileRepository, times(resources.length)).save(any(ProcessFile.class));
    verify(repositoryService, times(resources.length)).createDeployment();
  }

  @Test
  public void existingProcessDifferentChecksumIsSuccessful() throws IOException {
    ProcessFile processFile = new ProcessFile("someFilename", "someChecksum");
    when(processFileRepository.findOneByFilename(anyString())).thenReturn(processFile);
    when(cryptoDigestService.sha256(any(InputStream.class))).thenReturn("differentChecksum");

    processInitService.updateProcessesFromResources();

    assertEquals("differentChecksum", processFile.getChecksum());
    verify(processFileRepository, times(resources.length)).findOneByFilename(anyString());
    verify(cryptoDigestService, times(resources.length)).sha256(any(InputStream.class));
    verify(processFileRepository, times(resources.length)).save(any(ProcessFile.class));
    verify(repositoryService, times(resources.length)).createDeployment();
  }

  @Test
  public void existingProcessSameChecksumIsSuccessful() throws IOException {
    ProcessFile processFile = new ProcessFile("someFilename", "someChecksum");
    when(processFileRepository.findOneByFilename(anyString())).thenReturn(processFile);

    processInitService.updateProcessesFromResources();

    verify(processFileRepository, times(resources.length)).findOneByFilename(anyString());
    verify(cryptoDigestService, times(resources.length)).sha256(any(InputStream.class));
    verify(processFileRepository, never()).save(any(ProcessFile.class));
    verify(repositoryService, never()).createDeployment();
  }

  @Test
  public void shaGenerationIOException() throws IOException {
    ProcessFile processFile = new ProcessFile("someFilename", "someChecksum");
    when(processFileRepository.findOneByFilename(anyString())).thenReturn(processFile);
    when(cryptoDigestService.sha256(any(InputStream.class))).thenThrow(new IOException());

    processInitService.updateProcessesFromResources();

    verify(processFileRepository, times(resources.length)).findOneByFilename(anyString());
    verify(cryptoDigestService, times(resources.length)).sha256(any(InputStream.class));
    verify(processFileRepository, never()).save(any(ProcessFile.class));
    verify(repositoryService, never()).createDeployment();
  }

}
