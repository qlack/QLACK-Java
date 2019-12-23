# QLACK Util - Logger

This module provides method execution logging functionality based on the `@Logged` annotation and AspectJ.

## Integration

### Add qlack-util-logger dependency to your pom.xml:

```xml
    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-logger</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add the qlack-util-logger configuration properties in the Spring boot application.properties file:
```properties
# Set the spring logger log level for the logger module package
logging.level.com.eurodyn.qlack.util.logger=DEBUG
# Global value that enables method execution performance logging (in msec).
qlack.util.logger.logPerformance=true
# Global value that enables logging of method parameter values during method execution. 
qlack.util.logger.logParamValues=false

```

Important: If `qlack.util.logger.logPerformance` and `qlack.util.logger.logParamValues` are not set in the 
application.properties file the default value is `null`. In this case the annotation declared flag is used.

### Add the packages in the Spring boot application main class declaration:

```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.util.logger"
})
```

### The `@Logged` annotation
To enable logging for a certain method or for the methods of a class, the `@Logged` annotation is used. This 
annotation has two flags to enable performance and parameter values logging for the method or methods it 
annotates or refers to (in case of an annotated class). 
These are the `performance` (default: true) and `paramValues` (default: false) flags. Between global flags declared 
in `application.properties` file and annotation declared flags the latter take precedence. So if the global value for
 the logging of performance is different from the one declared through the annotation of a certain method, the flag 
 declared in the annotation is used.

The `@Logged` annotation can be used to annotate either a method or a class. When a class is annotated, it is implied
 that all the methods are annotated too. If both the class and a method of this class are annotated, method 
 annotation takes precedence.
 
 The log output is of the following format:
`mypackage.MyClass##method([params]): in XX[msec]`

i.e.
 ```
c.e.qlack.util.logger.MethodLogger       : com.eurodyn.qlack.fuse.fileupload.service.impl.FileUploadImpl##cleanupExpired([]): in 1[msec]
```

### Example

Annotated Method
```java
  @Logged(performance = false, paramValues = true)
  public void myMethod(String myParam) {
  }
```

or

```java
  @Logged
  public void myMethod(String myParam) {
  }
```

Annotated class

```java
  @Logged
  public class MyClass {
  }
```
