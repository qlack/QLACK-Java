# QLACK Fuse - Content Manager

This module provides an easy to understand API around commonly used cryptoraphy-related functionality. Crypto module does not implement itself any cryptography algorithms as it reuses industry standard libraries for such functions. In Crypto module you can find helper method to create, maintain and use Symmetric Cryptography, Asymmetric Cryptography, Signatures generation and validation, Certificate Authority management, and Keystore management.

## Integration

### Add qlack-fuse-content-manager dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-crypto</artifactId>
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
