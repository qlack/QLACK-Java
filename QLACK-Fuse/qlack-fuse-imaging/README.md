# QLACK Fuse - Imaging

This module provides methods for applying filters and conversions on images, as well as QR code manipulation.

## Integration

### Add qlack-fuse-imaging dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-imaging</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.eurodyn.qlack.fuse.imaging"})
```
