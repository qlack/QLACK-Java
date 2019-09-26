package com.eurodyn.qlack.fuse.scheduler;

import lombok.extern.java.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job class for testing purposes
 *
 * @author European Dynamics SA.
 */
@Log
public class TestJob implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    log.info("Executing for testing purposes");
  }
}
