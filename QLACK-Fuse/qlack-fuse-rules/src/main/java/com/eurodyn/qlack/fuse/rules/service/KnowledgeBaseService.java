package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseDTO;
import com.eurodyn.qlack.fuse.rules.mapper.KnowledgeBaseMapper;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import com.eurodyn.qlack.fuse.rules.repository.KnowledgeBaseRepository;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.kie.api.KieBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the KnowledgeBase, KnowledgeBaseLibrary and
 * KnowledgeBaseRule objects.
 *
 * @author European Dynamics SA
 */
@Service
@Transactional
@Log
public class KnowledgeBaseService {

  private final RulesUtil rulesUtil = new RulesUtil();

  private final KnowledgeBaseMapper knowledgeBaseMapper;

  private final KnowledgeBaseRepository knowledgeBaseRepository;

  public KnowledgeBaseService(KnowledgeBaseMapper knowledgeBaseMapper,
      KnowledgeBaseRepository knowledgeBaseRepository) {
    this.knowledgeBaseMapper = knowledgeBaseMapper;
    this.knowledgeBaseRepository = knowledgeBaseRepository;
  }

  /**
   * Finds the persisted Knowledge Base State based on its id.
   *
   * @param knowledgeBaseId the id of the persisted Knowledge Base
   * @return the persisted Knowledge Base
   */
  public KnowledgeBase findKnowledgeBaseStateById(String knowledgeBaseId) {
    log.info("Retrieving Knowledge Base with id " + knowledgeBaseId);

    KnowledgeBase base = knowledgeBaseRepository.fetchById(knowledgeBaseId);

    if (base == null) {
      String errorMessage = "Cannot find Knowledge Base with id " + knowledgeBaseId;

      log.severe(errorMessage);
      throw new QDoesNotExistException(errorMessage);
    }

    return base;
  }

  /**
   * Creates a new Knowledge Base and its linked Knowledge Base Libraries with given libraries and
   * rules.
   *
   * @param libraries the libraries of the base
   * @param rules the rules of the base
   * @return the id of the newly created Knowledge Base
   */
  public String createKnowledgeBase(List<byte[]> libraries, List<String> rules) {
    log.info(
        "Creating new Knowledge Base with " + libraries.size() + " libraries and " + rules.size()
            + " rules.");

    KieBase kieBase = rulesUtil.createKieBase(libraries, rules);

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.setState(rulesUtil.serializeKnowledgeBase(kieBase));

    List<KnowledgeBaseLibrary> knowledgeBaseLibraries = new ArrayList<>();
    for (byte[] library : libraries) {
      KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
      knowledgeBaseLibrary.setLibrary(library);

      knowledgeBaseLibrary.setBase(knowledgeBase);
      knowledgeBaseLibraries.add(knowledgeBaseLibrary);
    }
    knowledgeBase.setLibraries(knowledgeBaseLibraries);

    List<KnowledgeBaseRule> knowledgeBaseRules = new ArrayList<>();
    for (String rule : rules) {
      KnowledgeBaseRule knowledgeBaseRule = new KnowledgeBaseRule();
      knowledgeBaseRule.setRule(rule);

      knowledgeBaseRule.setBase(knowledgeBase);
      knowledgeBaseRules.add(knowledgeBaseRule);
    }
    knowledgeBase.setRules(knowledgeBaseRules);

    knowledgeBaseRepository.save(knowledgeBase);

    return knowledgeBase.getId();
  }

  /**
   * Retrieves all the Knowledge Base entries.
   *
   * @return a list containing all the found Knowledge Base
   */
  public List<KnowledgeBaseDTO> getAllKnowledgeBases() {
    log.log(Level.INFO, "Retrieving all Knowledge Base entries.");

    List<KnowledgeBase> knowledgeBases = knowledgeBaseRepository.findAll();

    return knowledgeBaseMapper.mapToDTO(knowledgeBases);
  }

}
