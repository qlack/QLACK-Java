package com.eurodyn.qlack.fuse.scheduler.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.scheduler.InitTestValues;
import com.eurodyn.qlack.fuse.scheduler.TestJob;
import com.eurodyn.qlack.fuse.scheduler.dto.JobDTO;
import com.eurodyn.qlack.fuse.scheduler.exception.QSchedulerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JUnit Test for SchedulerService class
 *
 * @author European Dynamics SA.
 */

@ExtendWith(MockitoExtension.class)
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
  
  @BeforeEach
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
  public void getSchedulerNameTest() throws SchedulerException {
    String schedulerName = schedulerService.getSchedulerName();
    assertEquals(scheduler.getSchedulerName(), schedulerName);
  }

  @Test
  public void getSchedulerNameSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.getSchedulerName()).thenThrow(new SchedulerException());
      schedulerService.getSchedulerName();
    });
  }

  @Test
  public void getSchedulerInstanceIdTest() throws SchedulerException {
    String schedulerInstanceID = schedulerService.getSchedulerInstanceID();
    assertEquals(scheduler.getSchedulerInstanceId(), schedulerInstanceID);
  }

  @Test
  public void getSchedulerInstanceIdSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.getSchedulerInstanceId())
              .thenThrow(new SchedulerException());
      schedulerService.getSchedulerInstanceID();
    });
  }

  @Test
  public void startTest() throws SchedulerException {
    schedulerService.start();
    verify(scheduler, times(1)).start();
  }

  @Test
  public void startSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler).start();
      schedulerService.start();
    });
  }

  @Test
  public void shutdownTest() throws SchedulerException {
    schedulerService.shutdown();
    verify(scheduler, times(1)).shutdown();
  }

  @Test
  public void shutdownSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler).shutdown();
      schedulerService.shutdown();
    });
  }

  @Test
  public void standByTest() throws SchedulerException {
    schedulerService.standby();
    verify(scheduler, times(1)).standby();
  }

  @Test
  public void standBySchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler).standby();
      schedulerService.standby();
    });
  }

  @Test
  public void isStartedTest() throws SchedulerException {
    schedulerService.isStarted();
    verify(scheduler, times(1)).isStarted();
  }

  @Test
  public void isStartedSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.isStarted()).thenThrow(new SchedulerException());
      schedulerService.isStarted();
    });
  }

  @Test
  public void isShutdownTest() throws SchedulerException {
    schedulerService.isShutdown();
    verify(scheduler, times(1)).isShutdown();
  }

  @Test
  public void isShutdownSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.isShutdown()).thenThrow(new SchedulerException());
      schedulerService.isShutdown();
    });
  }

  @Test
  public void isInStandByModeTest() throws SchedulerException {
    schedulerService.isInStandbyMode();
    verify(scheduler, times(1)).isInStandbyMode();
  }

  @Test
  public void isInStandByModeSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.isInStandbyMode()).thenThrow(new SchedulerException());
      schedulerService.isInStandbyMode();
    });
  }

  @Test
  public void registerJobTest() throws SchedulerException {
    jobDetail = initTestValues.createJobDetail(false);
    schedulerService.registerJob(testJobClass);

    verify(scheduler, times(1))
        .addJob(jobDetailArgumentCaptor.capture(), eq(true));
    assertEquals(0, jobDetailArgumentCaptor.getValue().getJobDataMap().size());
  }

  @Test
  public void registerJobSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler)
              .addJob(any(), anyBoolean());
      schedulerService.registerJob(testJobClass);
    });
  }

  @Test
  public void registerJobWithMapTest() throws SchedulerException {
    jobDetail = initTestValues.createJobDetail(true);
    JobDataMap jobDataMap = jobDetail.getJobDataMap();
    schedulerService.registerJob(testJobClass, initTestValues.createMap());

    verify(scheduler, times(1))
        .addJob(jobDetailArgumentCaptor.capture(), eq(true));
    assertEquals(jobDataMap, jobDetailArgumentCaptor.getValue().getJobDataMap());
  }

  @Test
  public void registerExistingJobTest() throws SchedulerException {
    when(schedulerService.isJobExisting(testJobClass)).thenReturn(true);
    schedulerService.registerJob(testJobClass);
    verify(scheduler, times(0)).addJob(any(), anyBoolean());
  }

  @Test
  public void scheduleJobTest() throws SchedulerException {
    jobDetail = initTestValues.createJobDetail(false);
    trigger = initTestValues.createTrigger(TestJob.class, cronExpDaily);
    schedulerService.scheduleJob(testJobClass, cronExpDaily);

    verify(scheduler, times(1))
        .scheduleJob(jobDetailArgumentCaptor.capture(),
            triggerArgumentCaptor.capture());
    assertEquals(jobDetail.getJobDataMap(), jobDetailArgumentCaptor.getValue().getJobDataMap());
    assertEquals(triggerArgumentCaptor.getValue(), trigger);
  }

  @Test
  public void scheduleJobSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.scheduleJob(any(JobDetail.class), any(Trigger.class)))
              .thenThrow(new SchedulerException());
      schedulerService.scheduleJob(testJobClass, cronExpDaily);
    });
  }

  @Test
  public void scheduleJobWithMapTest() throws SchedulerException {
    jobDetail = initTestValues.createJobDetail(true);
    trigger = initTestValues.createTrigger(TestJob.class, cronExpDaily);
    schedulerService
        .scheduleJob(testJobClass, cronExpDaily, initTestValues.createMap());

    verify(scheduler, times(1))
        .scheduleJob(jobDetailArgumentCaptor.capture(),
            triggerArgumentCaptor.capture());
    assertEquals(jobDetail.getJobDataMap(), jobDetailArgumentCaptor.getValue().getJobDataMap());
    assertEquals(triggerArgumentCaptor.getValue(), trigger);
  }

  @Test
  public void scheduleExistingJobTest() throws SchedulerException {
    when(schedulerService.isJobExisting(testJobClass)).thenReturn(true);
    schedulerService.scheduleJob(testJobClass, cronExp5Min);
    verifyReschedule();
  }

  @Test
  public void rescheduleJobTest() throws SchedulerException {
    schedulerService.rescheduleJob(testJobClass, cronExp5Min);
    verifyReschedule();
  }

  @Test
  public void rescheduleJobSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.rescheduleJob(any(TriggerKey.class), any(Trigger.class)))
              .thenThrow(new SchedulerException());
      schedulerService.rescheduleJob(testJobClass, cronExp5Min);
    });
  }

  public void verifyReschedule() throws SchedulerException {
    trigger = initTestValues.createTrigger(testJobClass, cronExpDaily);
    verify(scheduler, times(1))
        .rescheduleJob(triggerKeyArgumentCaptor.capture(),
            triggerArgumentCaptor.capture());
    CronTrigger actualTrigger = (CronTrigger) triggerArgumentCaptor.getValue();
    String newCronExp = actualTrigger.getCronExpression();
    assertEquals(triggerKeyArgumentCaptor.getValue(), trigger.getKey());
    assertEquals(newCronExp, cronExp5Min);
  }

  @Test
  public void deleteJobTest() throws SchedulerException {
    String jobName = testJobClass.getName();
    schedulerService.deleteJob(jobName);
    verify(scheduler, times(1)).deleteJob(any());
  }

  @Test
  public void deleteJobSchedulerExceptionTest(){
    assertThrows(QSchedulerException.class, () -> {
      String jobName = testJobClass.getName();
      when(scheduler.deleteJob(any(JobKey.class)))
              .thenThrow(new SchedulerException());
      schedulerService.deleteJob(jobName);
    });
  }

  @Test
  public void deleteJobsTest() throws SchedulerException {
    List<String> jobNames = new ArrayList<>();
    jobNames.add(testJobClass.getName());
    jobNames.add(testJobClass.getName() + "_new");

    schedulerService.deleteJobs(jobNames);
    verify(scheduler, times(2)).deleteJob(any());
  }

  @Test
  public void deleteJobsFalseTest() throws SchedulerException {
    List<String> jobNames = new ArrayList<>();
    jobNames.add(testJobClass.getName());

    SchedulerService schedulerServiceMock = spy(schedulerService);
    when(schedulerServiceMock.deleteJob(testJobClass.getName()))
        .thenReturn(true);
    schedulerServiceMock.deleteJobs(jobNames);
    verify(scheduler, times(1)).deleteJob(any());
  }

  @Test
  public void triggerJobTest() throws SchedulerException {
    schedulerService.triggerJob(testJobClass);
    verify(scheduler, times(1))
        .triggerJob(any(), jobDataMapArgumentCaptor.capture());
    assertEquals(0, jobDataMapArgumentCaptor.getValue().size());
  }

  @Test
  public void triggerJobSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler)
              .triggerJob(any(JobKey.class), any(JobDataMap.class));
      schedulerService.triggerJob(testJobClass);
    });
  }

  @Test
  public void triggerJobWithMapTest() throws SchedulerException {
    jobDetail = initTestValues.createJobDetail(true);
    JobDataMap jobDataMap = jobDetail.getJobDataMap();

    schedulerService.triggerJob(testJobClass, initTestValues.createMap());
    verify(scheduler, times(1))
        .triggerJob(any(), jobDataMapArgumentCaptor.capture());
    assertEquals(jobDataMap, jobDataMapArgumentCaptor.getValue());
  }

  @Test
  public void pauseJobTest() throws SchedulerException {
    schedulerService.pauseJob(testJobClass);
    verify(scheduler, times(1)).pauseJob(jobKey);
  }

  @Test
  public void pauseJobSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler)
              .pauseJob(any(JobKey.class));
      schedulerService.pauseJob(testJobClass);
    });
  }

  @Test
  public void pauseAllTriggersTest() throws SchedulerException {
    schedulerService.pauseAllTriggers();
    verify(scheduler, times(1)).pauseAll();
  }

  @Test
  public void pauseAllTriggersSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler)
              .pauseAll();
      schedulerService.pauseAllTriggers();
    });
  }

  @Test
  public void resumeAllTriggersTest() throws SchedulerException {
    schedulerService.resumeAllTriggers();
    verify(scheduler, times(1)).resumeAll();
  }

  @Test
  public void resumeAllTriggersSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler)
              .resumeAll();
      schedulerService.resumeAllTriggers();
    });
  }

  @Test
  public void clearTest() throws SchedulerException {
    schedulerService.clear();
    verify(scheduler, times(1)).clear();
  }

  @Test
  public void clearSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      doThrow(new SchedulerException()).when(scheduler)
              .clear();
      schedulerService.clear();
    });
  }

  @Test
  public void getNextFireForJobTest() throws SchedulerException {
    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    triggerKey = trigger.getKey();
    when(scheduler.getTrigger(triggerKey)).thenReturn(trigger);
    schedulerService.getNextFireForJob(testJobClass);
    verify(scheduler, times(1)).getTrigger(triggerKey);
  }

  @Test
  public void getNextFireForJobSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.getTrigger(any(TriggerKey.class)))
              .thenThrow(new SchedulerException());
      schedulerService.getNextFireForJob(testJobClass);
    });
  }

  @Test
  public void getPreviousFireForJobTest() throws SchedulerException {
    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    triggerKey = trigger.getKey();
    when(scheduler.getTrigger(triggerKey)).thenReturn(trigger);
    schedulerService.getPreviousFireForJob(testJobClass);
    verify(scheduler, times(1)).getTrigger(triggerKey);
  }

  @Test
  public void getPreviousFireForJobSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.getTrigger(any(TriggerKey.class)))
              .thenThrow(new SchedulerException());
      schedulerService.getPreviousFireForJob(testJobClass);
    });
  }

  @Test
  public void isJobExistingTest() throws SchedulerException {
    schedulerService.isJobExisting(testJobClass);
    verify(scheduler, times(1)).checkExists(jobKey);
  }

  @Test
  public void isJobExistingSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.checkExists(any(JobKey.class)))
              .thenThrow(new SchedulerException());
      schedulerService.isJobExisting(testJobClass);
    });
  }

  @Test
  public void isTriggerExistingTest() throws SchedulerException {
    schedulerService.isTriggerExisting(testJobClass);
    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    triggerKey = trigger.getKey();
    verify(scheduler, times(1)).checkExists(triggerKey);
  }

  @Test
  public void isTriggerExistingSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.checkExists(any(TriggerKey.class)))
              .thenThrow(new SchedulerException());
      schedulerService.isTriggerExisting(testJobClass);
    });
  }

  @Test
  public void getCurrentlyRunningJobsTest() throws SchedulerException {
    jobDetail = initTestValues.createJobDetail(false);

    List<JobExecutionContext> jobs = new ArrayList<>();
    jobs.add(jobExecutionContext);

    List<String> expectedNames = new ArrayList<>();
    expectedNames.add(jobDetail.getKey().getName());

    when(scheduler.getCurrentlyExecutingJobs()).thenReturn(jobs);
    when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
    List<String> currentlyExecutingJobsNames = schedulerService
        .getCurrentlyExecutingJobsNames();
    verify(scheduler, times(1)).getCurrentlyExecutingJobs();
    assertEquals(expectedNames, currentlyExecutingJobsNames);
  }

  @Test
  public void getCurrentlyRunningJobsSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.getCurrentlyExecutingJobs())
              .thenThrow(new SchedulerException());
      schedulerService.getCurrentlyExecutingJobsNames();
    });
  }

  @Test
  public void getJobInfoTest() throws SchedulerException {
    String groupName = "DEFAULT";
    List<String> groupNames = new ArrayList<>();
    groupNames.add(groupName);

    Set<JobKey> jobKeys = new HashSet<>();
    jobKeys.add(jobKey);

    trigger = initTestValues.createTrigger(testJobClass, cronExp5Min);
    List<Trigger> triggers = new ArrayList<>();
    triggers.add(trigger);

    JobDTO jobDTO = initTestValues
        .createJobDTO(testJobClass.getName(), groupName,
            trigger.getNextFireTime());
    List<JobDTO> expectedInfo = new ArrayList<>();
    expectedInfo.add(jobDTO);

    when(scheduler.getJobGroupNames()).thenReturn(groupNames);
    when(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)))
        .thenReturn(jobKeys);
    doReturn(triggers).when(scheduler).getTriggersOfJob(jobKey);

    List<JobDTO> actualJobInfo = schedulerService.getJobInfo();

    assertEquals(expectedInfo.size(), actualJobInfo.size());
  }

  @Test
  public void getJobInfoSchedulerExceptionTest() {
    assertThrows(QSchedulerException.class, () -> {
      when(scheduler.getJobGroupNames()).thenThrow(new SchedulerException());
      schedulerService.getJobInfo();
    });
  }

}
