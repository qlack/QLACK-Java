package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.crypto.service.CryptoDigestService;
import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

/**
 * {@inheritDoc}
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivitiProcessInitService implements ProcessInitService {

  private final ProcessFileRepository processFileRepository;
  private final RepositoryService repositoryService;
  private final CryptoDigestService cryptoDigestService;

  @Value("classpath:processes/*.xml")
  private Resource[] resources;

  /**
   * This method is executed during the deployment of the application.
   */
  @PostConstruct
  public void init() {
    updateProcessesFromResources();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
        log.error(e.getMessage());
      }
    });
  }

}
