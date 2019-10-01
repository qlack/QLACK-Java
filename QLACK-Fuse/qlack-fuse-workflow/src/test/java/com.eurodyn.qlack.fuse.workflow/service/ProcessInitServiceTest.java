package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import java.io.IOException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessInitServiceTest {

  @InjectMocks
  private ProcessInitService processInitService;

  @Mock
  private ProcessFileRepository processFileRepository;

  @Mock
  private RepositoryService repositoryService;

  @Autowired
  private ApplicationContext applicationContext;

  private Resource[] resources;

  @Before
  public void init() throws IOException {
    processInitService = new ProcessInitService(processFileRepository,
        repositoryService);
    resources = applicationContext.getResources("/processes/*.xml");
    ReflectionTestUtils.setField(processInitService, "resources", resources);
  }

  @Test
  public void initNewProcessesTest() {
    for (Resource r : resources) {
      when(processFileRepository.findOneByFilename(r.getFilename())).thenReturn(null);

      DeploymentBuilder deploymentBuilder = mock(DeploymentBuilder.class);
      when(repositoryService.createDeployment()).thenReturn(deploymentBuilder);
      when(deploymentBuilder.addClasspathResource("processes/" + r.getFilename()))
          .thenReturn(deploymentBuilder);
      //doNothing().when(deploymentBuilder).deploy();
    }
    processInitService.init();
    for (Resource r : resources) {
      verify(processFileRepository, times(1)).findOneByFilename(r.getFilename());
    }
  }

}
