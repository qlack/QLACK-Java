package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import java.io.IOException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.activiti.engine.RepositoryService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

  private final ApplicationContext applicationContext;

  private final ProcessFileRepository processFileRepository;

  private final RepositoryService repositoryService;

  @Autowired
  public ProcessInitService(ApplicationContext applicationContext, ProcessFileRepository processFileRepository,
    RepositoryService repositoryService) {
    this.applicationContext = applicationContext;
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
   * This method reads the .xml files located under the resources/processes folder and reads their content. If their content has already
   * been persisted in the Activiti tables and no changes are found, nothing happens. In any other case, a new version of the process is
   * created.
   */
  public void updateProcessesFromResources() {
    try {
      Resource[] resources = applicationContext.getResources("classpath:processes/*.xml");

      Arrays.stream(resources).forEach(r -> {
        ProcessFile existingProcessFile = processFileRepository.findOneByFilename(r.getFilename());

        try {
          String md5 = DigestUtils.md5Hex(r.getInputStream());

          if (existingProcessFile == null) {
            ProcessFile newProcessFile = new ProcessFile(r.getFilename(), md5);
            processFileRepository.save(newProcessFile);
            repositoryService.createDeployment().addClasspathResource("processes/" + r.getFilename()).deploy();
          } else if (!existingProcessFile.getChecksum().equals(md5)) {
            existingProcessFile.setChecksum(md5);
            processFileRepository.save(existingProcessFile);
            repositoryService.createDeployment().addClasspathResource("processes/" + r.getFilename()).deploy();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
