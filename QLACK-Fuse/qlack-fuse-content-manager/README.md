# QLACK Fuse - Content Manager

The Content manager module allows you to organise and maintain a content repository. Content entries can be in any form, binary or text, and a rich API is provided to manage versions of your content, check in/check out, upload/download, etc. The repository can be configured to store your content either in database tables or in the filesystem.

## Integration

### Add qlack-fuse-content-manager dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-content-manager</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add qlack-fuse-content-manager changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-content-manager/qlack.fuse.cm.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.cm.repository")
@EntityScan("com.eurodyn.qlack.fuse.cm.model")
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.fuse.cm"
})
```
