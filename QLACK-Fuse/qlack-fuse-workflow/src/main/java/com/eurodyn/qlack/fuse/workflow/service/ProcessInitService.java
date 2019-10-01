package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import java.io.IOException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the initialization of the processes.
 *
 * @author European Dynamics
 */
@Service
@Transactional
public class ProcessInitService {

  private final ProcessFileRepository processFileRepository;

  private final RepositoryService repositoryService;

  @Value("classpath:processes/*.xml")
  private Resource[] resources;

  @Autowired
  public ProcessInitService(ProcessFileRepository processFileRepository,
      RepositoryService repositoryService) {
    this.processFileRepository = processFileRepository;
    this.repositoryService = repositoryService;
  }

  /**
   * This method is executed during the deployment of the application.
   */
  @PostConstruct
  public void init() {
    updateProcessesFromResources();
  }

  /**
   * This method reads the .xml files located under the resources/processes folder and reads their
   * content. If their content has already been persisted in the Activiti tables and no changes are
   * found, nothing happens. In any other case, a new version of the process is created.
   */
  public void updateProcessesFromResources() {
    Arrays.stream(resources).forEach(r -> {
      ProcessFile existingProcessFile = processFileRepository.findOneByFilename(r.getFilename());

      try {
        String md5 = DigestUtils.md5Hex(r.getInputStream());

        if (existingProcessFile == null) {
          ProcessFile newProcessFile = new ProcessFile(r.getFilename(), md5);
          processFileRepository.save(newProcessFile);
          DeploymentBuilder d = repositoryService.createDeployment();
          DeploymentBuilder d1 = d.addClasspathResource("processes/" + r.getFilename());
          d1.deploy();
        } else if (!existingProcessFile.getChecksum().equals(md5)) {
          existingProcessFile.setChecksum(md5);
          processFileRepository.save(existingProcessFile);
          repositoryService.createDeployment()
              .addClasspathResource("processes/" + r.getFilename()).deploy();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}
