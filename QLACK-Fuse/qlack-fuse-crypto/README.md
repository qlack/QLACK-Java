# QLACK Fuse - Crypto

This module provides an easy to understand API around commonly used cryptoraphy-related functionality. Crypto module does not implement itself any cryptography algorithms as it reuses industry standard libraries for such functions. In Crypto module you can find helper method to create, maintain and use Symmetric Cryptography, Asymmetric Cryptography, Signatures generation and validation, Certificate Authority management, and Keystore management.

## Integration

### Add qlack-fuse-crypto dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-crypto</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.eurodyn.qlack.fuse.crypto"})
```
