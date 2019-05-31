package com.eurodyn.qlack.fuse.scheduler.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author European Dynamics
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobDTO implements Serializable {

  private String jobName;
  private String jobGroup;
  private Date nextFireTime;
}
