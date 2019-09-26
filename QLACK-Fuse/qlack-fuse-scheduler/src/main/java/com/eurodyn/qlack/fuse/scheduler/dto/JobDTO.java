package com.eurodyn.qlack.fuse.scheduler.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for Job.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobDTO implements Serializable {

  /**
   * Job name
   */
  private String jobName;

  /**
   * Job group
   */
  private String jobGroup;

  /**
   * The time of the next scheduler fire up
   */
  private Date nextFireTime;
}
