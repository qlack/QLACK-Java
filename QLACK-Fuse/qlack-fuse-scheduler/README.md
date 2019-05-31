# QLACK scheduler module

This module is responsible for creating and scheduling custom application jobs by using Spring Quartz.

## Integration

### Add qlack-fuse-scheduler dependency to your pom.xml:

```xml
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-scheduler</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add qlack-fuse-scheduler changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack.fuse.scheduler.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:

```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.scheduler"
})
```

### Define jobs to be scheduled

Each Job should be defined in a class that implements the org.quartz.Job interface.  The method "execute" should contain the business logic that will be executed.

### Add Cron Expressions 

When scheduling a Job, it is required to provide a Cron expression, which will define the frequency that the Job will be executed (e.g. every 5 minutes). 
For example, a Job that is executed every 5 minutes would use the cron expression "0 0/5 * 1/1 * ? *".

### Schedule Job
Each Job that has been defined, must be scheduled and will be automatically executed.  
The execution frequency is described using the aforementioned Cron Expressions.  
In addition to the Cron Expression described above, you can provide some data to the Job instance when it's executed, by using the JobDataMap.  
Any number of (serializable) objects can be provided to the Job, in a key-value format. Inside the Job#execute() method you can access the JobDataMap and use the provided data.  

### Example 

#### Defining Jobs

```java

package com.eurodyn.qlack.test.cmd.services.scheduler.job;

import com.eurodyn.qlack.test.cmd.model.Employee;
import com.eurodyn.qlack.test.cmd.repository.EmployeeRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author European Dynamics
 */

@Transactional
public class EmployeeDeletionJob implements Job {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int employeeThreshold = jobDataMap.getInt("employeeThreshold");

        List<Employee> employees = employeeRepository.findByFirstNameAndLastName("John", "Doe");

        if(employees.size() >= employeeThreshold) {
            employeeRepository.deleteByFirstNameAndLastName("John", "Doe");
            System.out.println("Deleted all generated employees");
        }
    }
}

// ..
```

```java
package com.eurodyn.qlack.test.cmd.services.scheduler.job;

import lombok.extern.java.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author European Dynamics
 */

@Log
public class LoggerJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(jobExecutionContext.getJobInstance() + " : executed at " + jobExecutionContext.getFireTime());
    }
}
```

#### Adding a Cron Expression in application.properties

```properties
qlack.fuse.scheduler.cron.employeeDeletionJob =  0 0/5 * 1/1 * ? *
```


#### Schedule jobs on Application start

```java
package com.eurodyn.qlack.test.cmd;

import com.eurodyn.qlack.fuse.scheduler.service.SchedulerService;
import com.eurodyn.qlack.test.cmd.services.scheduler.job.LoggerJob;
//..

public class QlackSpringBootConsoleApplication implements CommandLineRunner {


    @Autowired
    private SchedulerService schedulerService;
    
    //The CronExp added in application.properties in the example above
    @Value("${qlack.fuse.scheduler.cron.employeeDeletionJob}")
    private String deletionCronExp;

    //Defines the max allowed generated employees and is used in EmployeeDeletionJob.
    @Value("${qlack.fuse.scheduler.employeeThreshold}")
    private int employeeThreshold;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QlackSpringBootConsoleApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    public void run(String... args) {
        //A cron expression to run a Job daily
        String cronExpDaily = "0 0 12 1/1 * ? *";
        schedulerService.scheduleJob(LoggerJob.class, cronExpDaily);
        
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("employeeThreshold", employeeThreshold) ;
        schedulerService.scheduleJob(EmployeeDeletionJob.class, deletionCronExp, jobDataMap);

    }
}
```

#### Basic Job operations 

In addition to scheduling a job you can also:  
    1. Get information about all scheduled jobs (job name, job group and next execution time).  
    2. Execute a Job, regardless of it's forthcoming execution time.  
    3. Delete a scheduled Job.  

```java
//..
    public void listAllJobs() {
       List<JobDTO> jobNames = schedulerService.getJobInfo();
       log.info("Scheduled jobs are: ");
       jobNames.forEach(s -> log.info( s.toString()));
    }
    
    public void triggerLoggerJob() {
        schedulerService.triggerJob(LoggerJob.class);
    }
    
    public void deleteJob() {
        schedulerService.deleteJob(schedulerService.getJobName(EmployeeDeletionJob.class));
    }
   //..
}
```