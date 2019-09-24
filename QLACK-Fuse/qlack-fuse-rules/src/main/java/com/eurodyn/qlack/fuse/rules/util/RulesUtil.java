package com.eurodyn.qlack.fuse.rules.util;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.java.Log;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.util.DroolsStreamUtils;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

/**
 * This util method contains useful method that are used by the rules Qlack component.
 *
 * @author European Dynamics SA
 */
@Log
public class RulesUtil {

    /**
     * Creates a KieBase using a persisted Knowledge Base.
     *
     * @param base the persisted Knowledge Base
     * @return the created KieBase
     */
    public ClassLoaderKnowledgeBase createKieBaseFromBaseState(KnowledgeBase base) {

        List<byte[]> libraries = new ArrayList<>();
        List<KnowledgeBaseLibrary> runtimeLibraries = base.getLibraries();
        for (KnowledgeBaseLibrary runtimeLibrary : runtimeLibraries) {
            byte[] library = runtimeLibrary.getLibrary();
            libraries.add(library);
        }

        // re-create classloader
        JarClassLoaderBuilder classLoaderBuilder = new JarClassLoaderBuilder();

        for (byte[] libraryBytes : libraries) {
            classLoaderBuilder.add(libraryBytes);
        }

        MapBackedClassLoader classLoader = classLoaderBuilder.buildClassLoader(base.getId());

        // restore compiled knowledge base
        byte[] state = base.getState();

        KieBase kieBase = deserializeKnowledgeBase(state, classLoader);

        return new ClassLoaderKnowledgeBase(classLoader, kieBase);
    }

    /**
     * Creates a new KieBase using the given libraries and rules.
     *
     * @param libraries the libraries of the base
     * @param rules the rules of the base
     * @return the newly created kieBase
     */
    public KieBase createKieBase(List<byte[]> libraries, List<String> rules) {

        // add libraries to classloader
        JarClassLoaderBuilder classLoaderBuilder = new JarClassLoaderBuilder();
        if (libraries != null) {
            libraries.stream().forEach(l -> classLoaderBuilder.add(l));
        }
        ClassLoader classLoader = classLoaderBuilder.buildClassLoader(null);

        KnowledgeBuilderConfiguration kBuilderConfiguration = KnowledgeBuilderFactory
            .newKnowledgeBuilderConfiguration(null, classLoader);
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kBuilderConfiguration);
        compileRules(rules, kbuilder);

        KieBaseConfiguration kBaseConfiguration = KnowledgeBaseFactory
            .newKnowledgeBaseConfiguration(null, classLoader);
        KieBase kbase = KnowledgeBaseFactory.newKnowledgeBase("test", kBaseConfiguration);

        // add packages to knowledge base
        ((KnowledgeBaseImpl) kbase).addPackages(kbuilder.getKnowledgePackages());
        return kbase;
    }

    /**
     * Serializes a KieBase.
     *
     * @param kieBase the KieBase to be serialized
     * @return the serialized KieBase
     */
    public byte[] serializeKnowledgeBase(KieBase kieBase) {
        try {
            return DroolsStreamUtils.streamOut(kieBase);
        } catch (IOException e) {
            throw new QRulesException(e);
        }
    }

    /**
     * Deserializes a KieBase.
     *
     * @param state the serialized KieBase
     * @param classLoader the ClassLoader of the KieBase
     * @return the KieBase
     */
    public KieBase deserializeKnowledgeBase(byte[] state, ClassLoader classLoader) {
        try {
            return (KieBase) DroolsStreamUtils.streamIn(state, classLoader);
        } catch (IOException | ClassNotFoundException e) {
            throw new QRulesException(e);
        }
    }

  /**
   * Compiles the given rules and adds them in the KnowledgeBuilder
   *
   * @param rules the rules to compiled and added
   * @param knowledgeBuilder the KnowledgeBuilder that will include the compiled rules
   */
  private void compileRules(List<String> rules, KnowledgeBuilder knowledgeBuilder) {
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
