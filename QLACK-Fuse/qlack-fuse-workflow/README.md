# QLACK Fuse - Workflow

This module provides a unified and simple api for creating and managing workflow instances. It
 comes in two flavors, implementating two well-established BPM tools (Camunda/Activiti).

## Integration

### Add either of the below dependencies to your pom.xml:

```
    <dependency>
      <groupId>com.eurodyn.qlack.fuse.workflow</groupId>
      <artifactId>qlack-fuse-workflow-camunda</artifactId>
      <version>${project.parent.version}</version>
    </dependency>
```
or 
```
    <dependency>
      <groupId>com.eurodyn.qlack.fuse.workflow</groupId>
      <artifactId>qlack-fuse-workflow-activiti</artifactId>
      <version>${project.parent.version}</version>
    </dependency>
``` 

### Add qlack-fuse-workflow changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-workflow/qlack.fuse.workflow.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.workflow.repository")
@EntityScan("com.eurodyn.qlack.fuse.workflow.model")
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.fuse.workflow",
        "com.eurodyn.qlack.fuse.crypto.service"
})
```
### During the deployment of the application, workflow will locate all the (.xml or .bpmn) files inside the /resources/processes folder in order to persist them as processes.

### Changes in those files, will create new versions of the same process.
