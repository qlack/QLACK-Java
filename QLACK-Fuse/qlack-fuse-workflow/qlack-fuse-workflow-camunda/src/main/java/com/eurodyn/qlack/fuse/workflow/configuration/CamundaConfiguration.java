package com.eurodyn.qlack.fuse.workflow.configuration;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A configuration class that is used to configure Camunda engine.
 *
 * @author European Dynamics SA
 */
@Configuration
@RequiredArgsConstructor
public class CamundaConfiguration {

  private final DataSource dataSource;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public SpringProcessEngineConfiguration processEngineConfiguration() {
    SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

    configuration.setDataSource(dataSource);
    configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    configuration.setTransactionManager(transactionManager);

    return configuration;
  }

  @Bean
  public ProcessEngineFactoryBean processEngine() {
    ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
    factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
    return factoryBean;
  }

  @Bean
  public RepositoryService repositoryService() {
    return processEngine().getProcessEngineConfiguration().getRepositoryService();
  }
}
