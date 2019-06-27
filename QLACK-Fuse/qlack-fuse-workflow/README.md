# QLACK Workflow module

This module uses the Activiti BPM tool and provides functionalities for creating and managing workflow instances.

## Integration

### Add qlack-fuse-workflow dependency to your pom.xml:
```
    <properties>
        <!-- ... -->
        <version.qlack>3.1.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-workflow</artifactId>
        <version>${version.qlack}</version>
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
        "com.eurodyn.qlack.fuse.workflow"
})
```
### During the deployment of the application, workflow will locate all the .xml files inside the /resources/processes folder in order to persist them as processes.

### Changes in those files, will create new versions of the same process.
