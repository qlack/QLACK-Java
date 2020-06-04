package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.crypto.service.CryptoDigestService;
import com.eurodyn.qlack.fuse.workflow.InitTestValues;
import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
public class ActivitiProcessInitServiceTest {

  @InjectMocks
  private ActivitiProcessInitService processInitService;

  @Mock
  private ProcessFileRepository processFileRepository;

  @Mock
  private RepositoryService repositoryService;

  @Mock
  private CryptoDigestService cryptoDigestService;

  @Autowired
  private ApplicationContext applicationContext;

  @Mock
  private DeploymentBuilder deploymentBuilder;

  private Resource[] resources;

  private InitTestValues initTestValues;

  private ProcessFile processFile;

  @Before
  public void init() throws IOException {
    processInitService = new ActivitiProcessInitService(processFileRepository,
      repositoryService, cryptoDigestService);
    resources = applicationContext.getResources("/processes/*.xml");
    initTestValues = new InitTestValues();
    processFile = initTestValues.generateProcessFile();

    ReflectionTestUtils.setField(processInitService, "resources", resources);
    when(repositoryService.createDeployment()).thenReturn(deploymentBuilder);
    when(deploymentBuilder.addClasspathResource(anyString()))
      .thenReturn(deploymentBuilder);
  }

  @Test
  public void initNewProcessesTest() {
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename()))
        .thenReturn(null);
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1))
        .findOneByFilename(r.getFilename());
      verify(repositoryService, times(1)).createDeployment();
    }
  }

  @Test
  public void initExistingProcessNullChecksumTest() {
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename()))
        .thenReturn(processFile);
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1))
        .findOneByFilename(r.getFilename());
      verify(processFileRepository, times(1)).save(processFile);
    }
  }

  @Test
  public void initExistingProcessSameChecksumTest() throws IOException {
    String checksum = "same_checksum";
    processFile.setChecksum(checksum);
    when(cryptoDigestService.sha256(any(InputStream.class)))
      .thenReturn(checksum);
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename()))
        .thenReturn(processFile);
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1))
        .findOneByFilename(r.getFilename());
    }
  }

  @Test
  public void initIoExceptionTest() throws IOException {
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename()))
        .thenReturn(processFile);
    }
    when(cryptoDigestService.sha256(any(InputStream.class)))
      .thenThrow(new IOException());
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1))
        .findOneByFilename(r.getFilename());
    }
  }

}
