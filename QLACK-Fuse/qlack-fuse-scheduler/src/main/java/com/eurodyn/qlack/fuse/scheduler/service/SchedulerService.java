package com.eurodyn.qlack.fuse.scheduler.service;

import com.eurodyn.qlack.fuse.scheduler.dto.JobDTO;
import com.eurodyn.qlack.fuse.scheduler.exception.QSchedulerException;
import lombok.extern.java.Log;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Provides scheduler administration functionality
 *
 * @author European Dynamics SA.
 */
@Transactional
@Service
@Log
public class SchedulerService {

  /**
   * Reference to Quartz scheduler.
   */
  private Scheduler scheduler;

  @Autowired
  public SchedulerService(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * A helper method to define the name of a job based on the class implementing the job.
   *
   * @param jobClass the class implementing the job
   * @param <J> the Job Object
   * @return the name of the job
   */
  public static <J extends Job> String getJobName(Class<J> jobClass) {
    String jobName = jobClass.getName();
    log.info("Fetching job name for class: " + jobName);
    return jobName;
  }

  /**
   * A helper method to define the name of a trigger based on the class implementing the job.
   *
   * @param jobClass the class implementing the job.
   * @param <J> the Job Object
   * @return returns the name of the trigger
   */
  public static <J extends Job> String getTriggerName(Class<J> jobClass) {
    log.info("Fetching trigger name for class: " + jobClass);
    return getJobName(jobClass) + "_trigger";
  }

  /**
   * Retrieves the name of the Quartz scheduler instance used in the application
   *
   * @return the name of the Quartz scheduler instance
   */
  public String getSchedulerName() {
    try {
      log.info("Fetching the name of the scheduler");
      return scheduler.getSchedulerName();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Retrieves the id of the of the Quartz scheduler instance used in the application
   *
   * @return the id of the Quartz scheduler instance
   */
  public String getSchedulerInstanceID() {
    try {
      log.info("Fetching the id of the scheduler instance");
      return scheduler.getSchedulerInstanceId();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Starts the Quartz scheduler instance
   */
  public void start() {
    try {
      log.info("Starting the scheduler");
      scheduler.start();
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  /**
   * Terminates the Quartz scheduler instance
   */
  public void shutdown() {
    try {
      log.info("Shutting down the scheduler");
      scheduler.shutdown();
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  /**
   * Pauses the Quartz scheduler instance
   */
  public void standby() {
    try {
      log.info("Pausing the scheduler");
      scheduler.standby();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Checks if the Quartz Scheduler instance has started
   *
   * @return true if started, false otherwise
   */
  public boolean isStarted() {
    try {
      log.info("Checking if the scheduler has started");
      return scheduler.isStarted();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Checks if the Quartz Scheduler instance has been terminated
   *
   * @return true if terminated, false otherwise
   */
  public boolean isShutdown() {
    try {
      log.info("Checking if the scheduler has been terminated");
      return scheduler.isShutdown();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Checks if the Quartz Scheduler instance is in standby mode
   *
   * @return true if paused, false otherwise
   */
  public boolean isInStandbyMode() {
    try {
      log.info("Checking if the scheduler is in standby mode");
      return scheduler.isInStandbyMode();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Registers a job to the scheduler's list of jobs. A registered job, should be scheduled manually.
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   */
  public <J extends Job> void registerJob(Class<J> jobClass) {
    registerJob(jobClass, null);
  }

  /**
   * Registers a job to the scheduler's list of jobs. A registered job, should be scheduled manually.
   *
   * @param jobClass a class implementing the Job interface
   * @param jobData a map containing any number of serializable objects, that the job will be able
   * @param <J> the Job Object to access on execution
   */
  public <J extends Job> void registerJob(Class<J> jobClass,
      Map<String, Object> jobData) {
    log.info("Registering job: " + getJobName(jobClass));
    try {
      // Check if the job is already registered and ignore the request.

      if (isJobExisting(jobClass)) {
        log.info(MessageFormat
            .format("Job {0} is already registered. ", getJobName(jobClass)));
      } else {
        scheduler.addJob(buildJob(jobClass, jobData), true);
      }
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  /**
   * Schedules a job for execution
   *
   * @param jobClass a class implementing the job interface
   * @param cronExpression a cron expression defining the execution interval of the job
   * @param <J> the Job Object
   */
  public <J extends Job> void scheduleJob(Class<J> jobClass,
      String cronExpression) {
    scheduleJob(jobClass, cronExpression, null);
  }

  /**
   * Schedules a job for execution
   *
   * @param jobClass a class implementing the job interface
   * @param cronExpression a cron expression defining the execution interval of the job
   * @param jobData a map containing any number of serializable objects, that the job will be able to access on
   * execution
   * @param <J> the Job Object
   */
  public <J extends Job> void scheduleJob(Class<J> jobClass,
      String cronExpression,
      Map<String, Object> jobData) {
    log.info(MessageFormat.format("Scheduling job {0} ", getJobName(jobClass)));
    try {
      // Check if the job is already registered, in that case only reschedule it.
      if (isJobExisting(jobClass)) {
        log.info(MessageFormat
            .format("Job {0} is already registered, rescheduling it.",
                getJobName(jobClass)));
        rescheduleJob(jobClass, cronExpression);
      } else {
        scheduler.scheduleJob(buildJob(jobClass, jobData),
            buildTrigger(jobClass, cronExpression));
      }
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  /**
   * Reschedules a job
   *
   * @param jobClass a class implementing the job interface
   * @param cronExpression a cron expression defining the execution interval of the job
   * @param <J> the Job Object
   */
  public <J extends Job> void rescheduleJob(Class<J> jobClass,
      String cronExpression) {
    log.info(
        MessageFormat.format("Rescheduling job {0} ", getJobName(jobClass)));
    try {
      scheduler.rescheduleJob(TriggerKey.triggerKey(getTriggerName(jobClass)),
          buildTrigger(jobClass, cronExpression));
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  /**
   * Deletes a scheduled job
   *
   * @param jobName The class name of the job to delete
   * @return true if job has been deleted, false otherwise
   */
  public boolean deleteJob(String jobName) {
    log.info(MessageFormat.format("Deleting job {0} ", jobName));
    try {
      return scheduler.deleteJob(JobKey.jobKey(jobName));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Deletes multiple jobs
   *
   * @param jobNames a list containing the job names to delete
   * @return true if all jobs have been deleted, false otherwise
   */
  public boolean deleteJobs(List<String> jobNames) {
    log.info(MessageFormat.format("Deleting jobs {0} ", jobNames));
    boolean deletedAll = true;
    for (String jobName : jobNames) {
      boolean deletedJob = deleteJob(jobName);
      deletedAll = deletedAll && deletedJob;
    }
    return deletedAll;
  }

  /**
   * Triggers the execution of a job
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   */
  public <J extends Job> void triggerJob(Class<J> jobClass) {
    triggerJob(jobClass, null);
  }

  /**
   * Triggers the execution of a job
   *
   * @param jobClass a class implementing the Job interface
   * @param jobData a map containing any number of serializable objects, that the job will be able to access on
   * execution
   * @param <J> the Job Object
   */
  public <J extends Job> void triggerJob(Class<J> jobClass,
      Map<String, Object> jobData) {
    log.info(MessageFormat
        .format("Triggering execution of job {0} ", getJobName(jobClass)));
    try {
      scheduler.triggerJob(JobKey.jobKey(getJobName(jobClass)),
          createJobDataMap(jobData));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Pauses all future executions of a job
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   */
  public <J extends Job> void pauseJob(Class<J> jobClass) {
    log.info(
        MessageFormat.format("Pausing all future executions of job {0}",
            getJobName(jobClass)));
    try {
      scheduler.pauseJob(JobKey.jobKey(getJobName(jobClass)));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Pauses all triggers
   */
  public void pauseAllTriggers() {
    log.info("Pausing all triggers");
    try {
      scheduler.pauseAll();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Resumes all triggers
   */
  public void resumeAllTriggers() {
    log.info("Resuming all triggers");
    try {
      scheduler.resumeAll();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Clears all scheduled jobs
   */
  public void clear() {
    try {
      scheduler.clear();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Finds the next execution of a job
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   * @return the job's next execution date
   */
  public <J extends Job> Date getNextFireForJob(Class<J> jobClass) {
    log.info(MessageFormat
        .format("Finding the next execution of job {0} ", getJobName(jobClass)));
    try {
      return scheduler
          .getTrigger(TriggerKey.triggerKey(getTriggerName(jobClass)))
          .getNextFireTime();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Finds the previous execution time of a job
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   * @return the job's previous execution date
   */
  public <J extends Job> Date getPreviousFireForJob(Class<J> jobClass) {
    log.info(MessageFormat
        .format("Finding the previous execution of job {0} ", getJobName(jobClass)));
    try {
      return scheduler
          .getTrigger(TriggerKey.triggerKey(getTriggerName(jobClass)))
          .getPreviousFireTime();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Checks if a job exists
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   * @return true if exists, false otherwise
   */
  public <J extends Job> boolean isJobExisting(Class<J> jobClass) {
    String jobName = getJobName(jobClass);
    log.info(MessageFormat.format("Checking if job {0} exists", jobName));
    try {
      return scheduler.checkExists(JobKey.jobKey(jobName));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Checks if a trigger exists
   *
   * @param jobClass a class implementing the Job interface
   * @param <J> the Job Object
   * @return true if exists, false otherwise
   */
  public <J extends Job> boolean isTriggerExisting(Class<J> jobClass) {
    String triggerName = getTriggerName(jobClass);
    log.info(MessageFormat.format("Checking if job {0} exists", triggerName));
    try {
      return scheduler.checkExists(TriggerKey.triggerKey(triggerName));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Finds all jobs that are currently executed
   *
   * @return a list with the names of the jobs
   */
  public List<String> getCurrentlyExecutingJobsNames() {
    log.info("Finding jobs that are currently executed");
    try {
      List<JobExecutionContext> runningJobs = scheduler
          .getCurrentlyExecutingJobs();
      List<String> names = new ArrayList<>();

      for (JobExecutionContext runningJob : runningJobs) {
        names.add(runningJob.getJobDetail().getKey().getName());
      }
      return names;
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  /**
   * Collects information about all existing jobs. The information collected is the job's name, group and next execution
   * date
   *
   * @return a list of JobDTO containing the information about each job
   */
  public List<JobDTO> getJobInfo() {
    log.info("Collecting information about all existing jobs");
    List<JobDTO> jobs = new ArrayList<>();

    try {
      for (String groupName : scheduler.getJobGroupNames()) {
        for (JobKey jobKey : scheduler
            .getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
          List<Trigger> triggers = (List<Trigger>) scheduler
              .getTriggersOfJob(jobKey);
          Date nextFireTime = triggers.get(0).getNextFireTime();

          JobDTO jobDTO = new JobDTO(jobKey.getName(), jobKey.getGroup(),
              nextFireTime);
          jobs.add(jobDTO);
        }
      }
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
    return jobs;
  }

  /**
   * Builds a new job
   *
   * @param jobClass a class implementing the Job interface
   * @param jobData a map containing any number of serializable objects, that the job will be able to access on
   * execution
   * @param <J> the Job Object
   * @return a {@link JobDetail} object containing all the info for the new job
   */
  private <J extends Job> JobDetail buildJob(Class<J> jobClass,
      Map<String, Object> jobData) {
    return JobBuilder
        .newJob(jobClass)
        .withIdentity(getJobName(jobClass))
        .storeDurably()
        .setJobData(createJobDataMap(jobData))
        .build();
  }

  /**
   * Builds a new trigger
   *
   * @param jobClass a class implementing the Job interface
   * @param cronExpression a cron expression defining the execution interval of the job
   * @param <J> the Job Object
   * @return a {@link CronTrigger} object containing all the info for the new trigger
   */
  private <J extends Job> CronTrigger buildTrigger(Class<J> jobClass,
      String cronExpression) {
    return TriggerBuilder
        .newTrigger()
        .forJob(getJobName(jobClass))
        .withIdentity(getTriggerName(jobClass))
        .startNow()
        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
        .build();
  }

  /**
   * Creates a new job data map
   *
   * @param jobData a map containing any number of serializable objects, that the job will be able to access on
   * execution
   * @return a {@link JobDataMap} object containing all the info for the job data
   */
  private JobDataMap createJobDataMap(Map<String, Object> jobData) {
    JobDataMap jobDataMap = new JobDataMap();
    if (jobData != null) {
      jobDataMap.putAll(jobData);
    }
    return jobDataMap;
  }
}
