package com.eurodyn.qlack.fuse.workflow.service;

/**
 * This service provides API methods related to the initialization of the processes.
 *
 * @author European Dynamics
 */
public interface ProcessInitService {

  /**
   * This method reads the .xml files located under the resources/processes
   * folder and reads their content. If their content has already been
   * persisted in the Activiti tables and no changes are found, nothing
   * happens. In any other case, a new version of the process is created.
   */
  void updateProcessesFromResources();

}
