package com.eurodyn.qlack.fuse.rules.util;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import java.nio.charset.Charset;
import java.util.List;
import lombok.extern.java.Log;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.io.ResourceFactory;

/**
 * This util class is responsible for compiling the provided rules.
 *
 * @author European Dynamics S.A.
 */
@Log
public class CompileRulesUtil {

  /**
   * Compiles the given rules and adds them in the KnowledgeBuilder
   *
   * @param rules the rules to compiled and added
   * @param knowledgeBuilder the KnowledgeBuilder that will include the compiled rules
   */
  public void compileRules(List<String> rules, KnowledgeBuilder knowledgeBuilder) {
    for (String rulesText : rules) {
      byte[] rulesBytes = rulesText.getBytes(Charset.forName("UTF-8"));
      Resource rulesResource = ResourceFactory.newByteArrayResource(rulesBytes);
      knowledgeBuilder.add(rulesResource, ResourceType.DRL);

      if (knowledgeBuilder.hasErrors()) {
        KnowledgeBuilderErrors kerrors = knowledgeBuilder.getErrors();
        for (KnowledgeBuilderError kerror : kerrors) {
          log.severe(kerror.toString());
        }
        throw new QRulesException(kerrors.toString());
      }
    }
  }


}
