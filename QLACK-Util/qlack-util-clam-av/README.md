# QLACK Clam AntiVirus module

This module provides file virus scanning functionality using a Clam AntiVirus server instance.

## Integration

### Add qlack-util-clam-av dependency to your pom.xml:

```xml
    <properties>
        <!-- ... -->
        <version.qlack>3.0.1-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-clam-av</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add the qlack-util-clam-av configuration properties in the Spring boot application.properties file:
```properties
# Hostname of the Clam AV server instance
qlack.util.clamav.host=localhost
# Clam AV server port
qlack.util.clamav.port=3310
# Clam AV server socket timeout
qlack.util.clamav.socket.timeout=100000
```

### Add the packages in the Spring boot application main class declaration:

```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.util.clamav"
})
```

### Example
```java
    // Scan the file data byte array
    VirusScanDTO result = clamAvService.virusScan(data);

    if (!result.isVirusFree()) {
      // If virus is found throw a Runtime exception
      throw new VirusFoundException(SECURITY_RISK_MESSAGE);
    } else {
      // Save the file
      fileRepository.save(file);
    }
```
