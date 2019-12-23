package com.eurodyn.qlack.fuse.scheduler;

import com.eurodyn.qlack.fuse.scheduler.dto.JobDTO;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * Provides object initialization methods for Test classes
 *
 * @author European Dynamics SA.
 */
public class InitTestValues {

  public <J extends Job> Trigger createTrigger(Class<J> clazz,
    String cronExpression) {
    return TriggerBuilder
      .newTrigger()
      .forJob(clazz.getName())
      .withIdentity(clazz.getName() + "_trigger")
      .startNow()
      .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
      .build();
  }

  public JobDetail createJobDetail(boolean addData) {
    JobDataMap jobDataMap = new JobDataMap();

    if (addData) {
      jobDataMap.putAll(createMap());
    }

    return JobBuilder
      .newJob(TestJob.class)
      .withIdentity(TestJob.class.getName())
      .storeDurably()
      .setJobData(jobDataMap)
      .build();
  }

  public Map<String, Object> createMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("test", 1);

    return map;
  }

  public JobDTO createJobDTO(String jobName, String groupName,
    Date nextFireTime) {
    JobDTO jobDTO = new JobDTO();
    jobDTO.setJobName(jobName);
    jobDTO.setJobGroup(groupName);
    jobDTO.setNextFireTime(nextFireTime);

    return jobDTO;
  }
}
