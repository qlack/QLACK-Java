package com.eurodyn.qlack.fuse.scheduler.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.scheduler.InitTestValues;
import com.eurodyn.qlack.fuse.scheduler.TestJob;
import com.eurodyn.qlack.fuse.scheduler.dto.JobDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * JUnit Test for SchedulerService class
 *
 * @author European Dynamics SA.
 */

@RunWith(MockitoJUnitRunner.class)
public class SchedulerServiceTest {

  @InjectMocks
  private SchedulerService schedulerService;

  @Mock
  private Scheduler scheduler;

  @Mock
  private JobExecutionContext jobExecutionContext;

  @Captor
  private ArgumentCaptor<JobDetail> jobDetailArgumentCaptor;

  @Captor
  private ArgumentCaptor<JobDataMap> jobDataMapArgumentCaptor;

  @Captor
  private ArgumentCaptor<Trigger> triggerArgumentCaptor;

  @Captor
  private ArgumentCaptor<TriggerKey> triggerKeyArgumentCaptor;

  private String className;
  private Class<TestJob> testJobClass;
  private InitTestValues initTestValues;
  private String cronExpDaily;
  private String cronExp5Min;
  private JobDetail jobDetail;
  private JobKey jobKey;
  private Trigger trigger;
  private TriggerKey triggerKey;

  @Before
  public void init() {
    schedulerService = new SchedulerService(scheduler);
    testJobClass = TestJob.class;
    className = testJobClass.getName();
    initTestValues = new InitTestValues();
    cronExpDaily = "0 0 12 1/1 * ? *";
    cronExp5Min = "0 0/5 * 1/1 * ? *";
    jobKey = JobKey.jobKey(testJobClass.getName());
  }

  @Test
  public void testGetJobName() {
    String jobName = SchedulerService.getJobName(testJobClass);
    assertEquals(className, jobName);
  }

  @Test
  public void testGetTriggerName() {
    String expectedTriggerName = className + "_trigger";
    String actualTriggerName = SchedulerService.getTriggerName(testJobClass);
    assertEquals(expectedTriggerName, actualTriggerName);
  }

  @Test
  public void testGetSchedulerName() {
    String schedulerName = schedulerService.getSchedulerName();
    try {
      assertEquals(scheduler.getSchedulerName(), schedulerName);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetSchedulerInstanceId() {
    String schedulerInstanceID = schedulerService.getSchedulerInstanceID();
    try {
      assertEquals(scheduler.getSchedulerInstanceId(), schedulerInstanceID);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testStart() {
    schedulerService.start();
    try {
      verify(scheduler, times(1)).start();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testShutdown() {
    schedulerService.shutdown();
    try {
      verify(scheduler, times(1)).shutdown();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testStandBy() {
    schedulerService.standby();
    try {
      verify(scheduler, times(1)).standby();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsStarted() {
    schedulerService.isStarted();
    try {
      verify(scheduler, times(1)).isStarted();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsShutdown() {
    schedulerService.isShutdown();
    try {
      verify(scheduler, times(1)).isShutdown();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsInStandByMode() {
    schedulerService.isInStandbyMode();
    try {
      verify(scheduler, times(1)).isInStandbyMode();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRegisterJob() {
    jobDetail = initTestValues.createJobDetail(false);
    schedulerService.registerJob(testJobClass);

    try {
      verify(scheduler, times(1)).addJob(jobDetailArgumentCaptor.capture(), eq(true));
      assertEquals(jobDetailArgumentCaptor.getValue().getJobDataMap().size(), 0);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRegisterJobWithMap() {
    jobDetail = initTestValues.createJobDetail(true);
    JobDataMap jobDataMap = jobDetail.getJobDataMap();
    schedulerService.registerJob(testJobClass, initTestValues.createMap());

    try {
      verify(scheduler, times(1)).addJob(jobDetailArgumentCaptor.capture(), eq(true));
      assertTrue(jobDetailArgumentCaptor.getValue().getJobDataMap().equals(jobDataMap));
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRegisterExistingJob() {
    when(schedulerService.isJobExisting(testJobClass)).thenReturn(true);
    schedulerService.registerJob(testJobClass);

    try {
      verify(scheduler, times(0)).addJob(any(), anyBoolean());
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testScheduleJob() {
    jobDetail = initTestValues.createJobDetail(false);
    trigger = initTestValues.createTrigger(TestJob.class, cronExpDaily);
    schedulerService.scheduleJob(testJobClass, cronExpDaily);

    try {
      verify(scheduler, times(1)).scheduleJob(jobDetailArgumentCaptor.capture(), triggerArgumentCaptor.capture());
      assertTrue(jobDetailArgumentCaptor.getValue().getJobDataMap().equals(jobDetail.getJobDataMap()));
      assertEquals(triggerArgumentCaptor.getValue(), trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testScheduleJobWithMap() {
    jobDetail = initTestValues.createJobDetail(true);
    trigger = initTestValues.createTrigger(TestJob.class, cronExpDaily);
    schedulerService.scheduleJob(testJobClass, cronExpDaily, initTestValues.createMap());

    try {
      verify(scheduler, times(1)).scheduleJob(jobDetailArgumentCaptor.capture(), triggerArgumentCaptor.capture());
      assertTrue(jobDetailArgumentCaptor.getValue().getJobDataMap().equals(jobDetail.getJobDataMap()));
      assertEquals(triggerArgumentCaptor.getValue(), trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testScheduleExistingJob() {
    when(schedulerService.isJobExisting(testJobClass)).thenReturn(true);
    schedulerService.scheduleJob(testJobClass, cronExp5Min);
    verifyReschedule();
  }

  @Test
  public void testRescheduleJob() {
    schedulerService.rescheduleJob(testJobClass, cronExp5Min);
    verifyReschedule();
  }

  public void verifyReschedule() {
    trigger = initTestValues.createTrigger(testJobClass, cronExpDaily);
    try {
      verify(scheduler, times(1)).rescheduleJob(triggerKeyArgumentCaptor.capture(), triggerArgumentCaptor.capture());
      CronTrigger actualTrigger = (CronTrigger) triggerArgumentCaptor.getValue();
      String newCronExp = actualTrigger.getCronExpression();
      assertEquals(triggerKeyArgumentCaptor.getValue(), trigger.getKey());
      assertEquals(newCronExp, cronExp5Min);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDeleteJob() {
    String jobName = testJobClass.getName();
    schedulerService.deleteJob(jobName);
    try {
      verify(scheduler, times(1)).deleteJob(any());
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDeleteJobs() {
    List<String> jobNames = new ArrayList<>();
    jobNames.add(testJobClass.getName());
    jobNames.add(testJobClass.getName() + "_new");

    schedulerService.deleteJobs(jobNames);

    try {
      verify(scheduler, times(2)).deleteJob(any());
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTriggerJob() {
    schedulerService.triggerJob(testJobClass);
    try {
      verify(scheduler, times(1)).triggerJob(any(), jobDataMapArgumentCaptor.capture());
      assertEquals(jobDataMapArgumentCaptor.getValue().size(), 0);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTriggerJobWithMap() {
    jobDetail = initTestValues.createJobDetail(true);
    JobDataMap jobDataMap = jobDetail.getJobDataMap();

    schedulerService.triggerJob(testJobClass, initTestValues.createMap());
    try {
      verify(scheduler, times(1)).triggerJob(any(), jobDataMapArgumentCaptor.capture());
      assertTrue(jobDataMapArgumentCaptor.getValue().equals(jobDataMap));
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPauseJob() {
    schedulerService.pauseJob(testJobClass);
    try {
      verify(scheduler, times(1)).pauseJob(jobKey);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPauseAllTriggers() {
    schedulerService.pauseAllTriggers();
    try {
      verify(scheduler, times(1)).pauseAll();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void resumeAllTriggers() {
    schedulerService.resumeAllTriggers();
    try {
      verify(scheduler, times(1)).resumeAll();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void clear() {
    schedulerService.clear();
    try {
      verify(scheduler, times(1)).clear();
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetNextFireForJob() {
    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    triggerKey = trigger.getKey();
    try {
      when(scheduler.getTrigger(triggerKey)).thenReturn(trigger);
      schedulerService.getNextFireForJob(testJobClass);
      verify(scheduler, times(1)).getTrigger(triggerKey);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsJobExisting() {
    schedulerService.isJobExisting(testJobClass);
    try {
      verify(scheduler, times(1)).checkExists(jobKey);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsTriggerExisting() {
    schedulerService.isTriggerExisting(testJobClass);
    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    triggerKey = trigger.getKey();
    try {
      verify(scheduler, times(1)).checkExists(triggerKey);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetCurrentlyRunningJobs() {
    jobDetail = initTestValues.createJobDetail(false);

    List<JobExecutionContext> jobs = new ArrayList<>();
    jobs.add(jobExecutionContext);

    List<String> expectedNames = new ArrayList<>();
    expectedNames.add(jobDetail.getKey().getName());

    try {
      when(scheduler.getCurrentlyExecutingJobs()).thenReturn(jobs);
      when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
      List<String> currentlyExecutingJobsNames = schedulerService.getCurrentlyExecutingJobsNames();
      verify(scheduler, times(1)).getCurrentlyExecutingJobs();
      assertEquals(expectedNames, currentlyExecutingJobsNames);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetJobInfo() {
    String groupName = "DEFAULT";
    List<String> groupNames = new ArrayList<>();
    groupNames.add(groupName);

    Set<JobKey> jobKeys = new HashSet<>();
    jobKeys.add(jobKey);

    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    List<Trigger> triggers = new ArrayList<>();
    triggers.add(trigger);

    JobDTO jobDTO = initTestValues.createJobDTO(testJobClass.getName(), groupName, trigger.getNextFireTime());
    List<JobDTO> expectedInfo = new ArrayList<>();
    expectedInfo.add(jobDTO);

    try {
      when(scheduler.getJobGroupNames()).thenReturn(groupNames);
      when(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))).thenReturn(jobKeys);
      doReturn(triggers).when(scheduler).getTriggersOfJob(jobKey);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

    List<JobDTO> actualJobInfo = schedulerService.getJobInfo();

    assertEquals(expectedInfo.size(), actualJobInfo.size());
  }
}
