package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.crypto.service.CryptoDigestService;
import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;
import com.eurodyn.qlack.fuse.workflow.repository.ProcessFileRepository;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
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
public class CamundaProcessInitService implements ProcessInitService {

  private final ProcessFileRepository processFileRepository;
  private final RepositoryService repositoryService;
  private final CryptoDigestService cryptoDigestService;

  public static final String PROCESSES = "processes/";

  @Value("classpath:processes/*.bpmn")
  private Resource[] resources;

  /**
   * {@inheritDoc}
   */
  @PostConstruct
  @Override
  public void updateProcessesFromResources() {
    Arrays.stream(resources).forEach(this::createOrUpdateProcess);
  }

  private void createOrUpdateProcess(Resource res) {
    ProcessFile existingProcessFile = processFileRepository.findOneByFilename(res.getFilename());

    try {
      String sha256 = cryptoDigestService.sha256(res.getInputStream());

      if (existingProcessFile == null) {
        ProcessFile newProcessFile = new ProcessFile(res.getFilename(), sha256);
        createProcessDeployment(res, newProcessFile);
      } else if (!existingProcessFile.getChecksum().equals(sha256)) {
        existingProcessFile.setChecksum(sha256);
        createProcessDeployment(res, existingProcessFile);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void createProcessDeployment(Resource r, ProcessFile newProcessFile) {
    processFileRepository.save(newProcessFile);
    repositoryService.createDeployment().addClasspathResource(PROCESSES + r.getFilename()).deploy();
  }

}
