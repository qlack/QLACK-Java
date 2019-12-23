package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.crypto.service.CryptoDigestService;
import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the initialization of the
 * processes.
 *
 * @author European Dynamics
 */
@Service
@Transactional
public class ProcessInitService {

  private static final Logger LOGGER = Logger
    .getLogger(ProcessInitService.class.getName());

  private final ProcessFileRepository processFileRepository;

  private final RepositoryService repositoryService;

  private final CryptoDigestService cryptoDigestService;

  @Value("classpath:processes/*.xml")
  private Resource[] resources;

  @Autowired
  public ProcessInitService(ProcessFileRepository processFileRepository,
    RepositoryService repositoryService,
    CryptoDigestService cryptoDigestService) {
    this.processFileRepository = processFileRepository;
    this.repositoryService = repositoryService;
    this.cryptoDigestService = cryptoDigestService;
  }

  /**
   * This method is executed during the deployment of the application.
   */
  @PostConstruct
  public void init() {
    updateProcessesFromResources();
  }

  /**
   * This method reads the .xml files located under the resources/processes
   * folder and reads their content. If their content has already been
   * persisted in the Activiti tables and no changes are found, nothing
   * happens. In any other case, a new version of the process is created.
   */
  public void updateProcessesFromResources() {
    Arrays.stream(resources).forEach(r -> {
      ProcessFile existingProcessFile = processFileRepository
        .findOneByFilename(r.getFilename());

      try {
        String sha256 = cryptoDigestService.sha256(r.getInputStream());

        if (existingProcessFile == null) {
          ProcessFile newProcessFile = new ProcessFile(r.getFilename(), sha256);
          processFileRepository.save(newProcessFile);
          repositoryService.createDeployment()
            .addClasspathResource("processes/" + r.getFilename())
            .deploy();
        } else if (!existingProcessFile.getChecksum().equals(sha256)) {
          existingProcessFile.setChecksum(sha256);
          processFileRepository.save(existingProcessFile);
          repositoryService.createDeployment()
            .addClasspathResource("processes/" + r.getFilename()).deploy();
        }
      } catch (IOException e) {
        LOGGER.severe(e.getMessage());
      }
    });
  }

}
