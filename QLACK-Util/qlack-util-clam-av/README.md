# QLACK Util - Clam AV

This module provides file virus scanning functionality using a Clam AntiVirus server instance.

## Integration

### Add qlack-util-clam-av dependency to your pom.xml:

```xml
    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-clam-av</artifactId>
        <version>${qlack.version}</version>
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

### Setup a running Clam AV instance using Docker

There are many Clam AV images available on the docker hub. You can use for example the following:

* https://hub.docker.com/r/mailu/clamav

Open a command line and type:

```docker pull mailu/clamav```

When downloading has finished run the container:

```docker run -p 3310:3310 mailu/clamav```

The antivirus should be up and running in `localhost:3310`
