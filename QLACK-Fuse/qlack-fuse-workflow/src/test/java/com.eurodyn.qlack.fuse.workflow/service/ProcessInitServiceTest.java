package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.eurodyn.qlack.fuse.workflow.InitTestValues;
import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import com.eurodyn.qlack.fuse.workflow.util.Md5ChecksumUtil;
import java.io.IOException;
import java.io.InputStream;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest(Md5ChecksumUtil.class)
public class ProcessInitServiceTest {

  @InjectMocks
  private ProcessInitService processInitService;

  @Mock
  private ProcessFileRepository processFileRepository;

  @Mock
  private RepositoryService repositoryService;

  @Autowired
  private ApplicationContext applicationContext;

  @Mock
  private DeploymentBuilder deploymentBuilder;

  private Resource[] resources;

  private InitTestValues initTestValues;

  private ProcessFile processFile;

  @Before
  public void init() throws IOException {
    processInitService = new ProcessInitService(processFileRepository,
        repositoryService);
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
      when(processFileRepository.findOneByFilename(r.getFilename())).thenReturn(null);
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1)).findOneByFilename(r.getFilename());
      verify(repositoryService, times(1)).createDeployment();
    }
  }

  @Test
  public void initExistingProcessNullChecksumTest() {
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename())).thenReturn(processFile);
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1)).findOneByFilename(r.getFilename());
      verify(processFileRepository, times(1)).save(processFile);
    }
  }

  @Test
  public void initExistingProcessSameChecksumTest() {
    processFile.setChecksum("d83b229cdb7f85dbd0db0196a501c2cc");
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename())).thenReturn(processFile);
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1)).findOneByFilename(r.getFilename());
    }
  }

  @Test
  public void initIoExceptionTest() throws IOException {
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename())).thenReturn(processFile);
    }
    mockStatic(Md5ChecksumUtil.class);
    when(Md5ChecksumUtil.getMd5Hex(any(InputStream.class))).thenThrow(new IOException());
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1)).findOneByFilename(r.getFilename());
    }
  }

}
