package com.eurodyn.qlack.fuse.rules.configuration;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The spring configuration of the Drools library that is need for rules execution.
 *
 * @author European Dynamics SA
 */
@Configuration
public class DroolsConfiguration {

  @Bean
  @ConditionalOnMissingBean(KieContainer.class)
  public KieContainer kieContainer() {
    return KieServices.Factory.get().getKieClasspathContainer();
  }

  @Bean
  @ConditionalOnMissingBean(KieBase.class)
  public KieBase kieBase() {
    return kieContainer().getKieBase();
  }

  @Bean
  @ConditionalOnMissingBean(KieSession.class)
  public KieSession kieSession() {
    return kieContainer().newKieSession();
  }

  @Bean
  @ConditionalOnMissingBean(StatelessKieSession.class)
  public StatelessKieSession statelessKieSession() {
    return kieContainer().newStatelessKieSession();
  }

  @Bean
  @ConditionalOnMissingBean(KModuleBeanFactoryPostProcessor.class)
  public KModuleBeanFactoryPostProcessor kiePostProcessor() {
    return new KModuleBeanFactoryPostProcessor();
  }
}
