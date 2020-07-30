# QLACK Fuse - Rules

This module uses the Camunda library and provides rules execution methods.

## Integration

### Add qlack-fuse-rules dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-rules</artifactId>
        <version>${project.parent.version}</version>
    </dependency>
```

### Add qlack-fuse-rules changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-rules/qlack.fuse.rules.changelog.xml"/>
```
