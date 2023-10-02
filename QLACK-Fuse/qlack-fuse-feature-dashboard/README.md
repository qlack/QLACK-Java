# QLACK Fuse - Feature Dashboard

This module provides language translations for the system.

## Integration

### Add qlack-fuse-feature-dashboard dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-feature-dashboard</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add qlack-fuse-lexicon changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-feature-dashboard/qlack.fuse.fd.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.fd.repository")
@EntityScan("com.eurodyn.qlack.fuse.fd.model")
@ComponentScan(basePackages = {"com.eurodyn.qlack.fuse.fd"})
```

### Example 1
```java
// ..
TBD

```




