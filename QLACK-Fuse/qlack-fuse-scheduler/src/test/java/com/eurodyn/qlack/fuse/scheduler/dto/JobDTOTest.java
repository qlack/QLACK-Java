package com.eurodyn.qlack.fuse.scheduler.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.contains;

@RunWith(MockitoJUnitRunner.class)
public class JobDTOTest {

  private JobDTO jobDTO;

  @Before
  public void init() {

    jobDTO = new JobDTO();
  }

  @Test
  public void jobNameTest() {
    String jobName = "jobName";
    jobDTO.setJobName(jobName);
    assertEquals(jobName, jobDTO.getJobName());
  }

  @Test
  public void jobGroupTest() {
    String jobGroup = "jobGroup";
    jobDTO.setJobGroup(jobGroup);
    assertEquals(jobGroup, jobDTO.getJobGroup());
  }

  @Test
  public void nextFireTimeTest() {
    Date nextFireTime = new Date();
    jobDTO.setNextFireTime(nextFireTime);
    assertEquals(nextFireTime, jobDTO.getNextFireTime());
  }

  @Test
  public void testToString() {
    assertNotNull(jobDTO.toString());
  }
}
