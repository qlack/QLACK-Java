# QLACK ACV module

This module provides Object Auditing, Comparison and Versioning operations.

## Integration
Add following property in your projects application.properties file, in order to persist the 
object versions in the database used by your application.
`javers.sqlSchema.sqlSchemaManagementEnabled=true`

### Add qlack-fuse-acv dependency to your pom.xml:
```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-acv</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.fuse.acv"
})
```

See the tests for how to use the available services.