package com.eurodyn.qlack.fuse.workflow.configuration;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A configuration class that is used to configure an activity
 *
 * @author European Dynamics SA
 */
@Configuration
@RequiredArgsConstructor
public class ActivitiConfiguration {

  private final DataSource dataSource;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public SpringProcessEngineConfiguration processEngineConfiguration() {
    SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

    configuration.setDataSource(dataSource);
    configuration.setDatabaseSchemaUpdate(
      ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    configuration.setAsyncExecutorActivate(false);
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
    return processEngine().getProcessEngineConfiguration()
      .getRepositoryService();
  }
}
