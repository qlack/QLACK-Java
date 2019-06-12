# QLACK fileupload module

This module provides file upload functionality. It is design to use flow.js as the chunks 
generation and file upload mechanism but could be integrated with other libraries.

## Integration

### Add qlack-fuse-fileupload dependency to your pom.xml:

```xml
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-fileupload</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add qlack-fuse-fileupload changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack.fuse.fileupload.changelog.xml"/>
```

### Add the fileupload configuration properties in the Spring boot application.properties file:
```properties

# The maximum upload permitted file size
spring.servlet.multipart.max-file-size = 5MB
# The maximum upload permitted file size per request 
spring.servlet.multipart.max-request-size = 5MB
# Enable uploaded files cleanup
qlack.fuse.fileupload.cleanupEnabled=false
# Files which are older than this value (msec) will be cleaned up. Default (5 minutes)
qlack.fuse.fileupload.cleanupThreshold=300000
# Interval between the re-execution of the cleanup. Default (5 minutes)
qlack.fuse.fileupload.cleanupInterval=300000
# Enable file virus scanning. Default (false). Requires a running AV server instance
qlack.fuse.fileupload.virusScanEnabled=false

```

### Add the packages in the Spring boot application main class declaration:

```java
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.fileupload.repository")
@EntityScan("com.eurodyn.qlack.fuse.fileupload.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.fileupload",
    "com.eurodyn.qlack.fuse.fileupload.service",
    "com.eurodyn.qlack.fuse.fileupload.service.impl",
    ...
})
```

`@EnableScheduling` is required for the proper execution of the scheduled files cleanup 
functionality 

### Extend FileUploadRestTemplate class and define your custom endpoints and implementation

### Example
```java
package com.eurodyn.qlack.test.web.controller;

import com.eurodyn.qlack.fuse.fileupload.rest.FileUploadRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author European Dynamics
 */
@RestController
@RequestMapping("/file-upload")
public class FileController extends FileUploadRestTemplate {

    /**
     * Checks if a chunk has already been uploaded
     *
     * @param flowIdentifier The file id
     * @param chunkNumber The chunk order number
     * @return ResponseEntity with http status 200 (in case of success), 204 (if chunk does not 
     * exist)
     */
    @RequestMapping(path = "/upload",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity checkChunk(
        @RequestParam("flowChunkNumber") Long chunkNumber,
        @RequestParam("flowIdentifier") String flowIdentifier) {
        return super.checkChunk(flowIdentifier, chunkNumber);
    }

    /**
     * Uploads a chunk of the to be uploaded file.
     * @param body The {@link MultipartHttpServletRequest} body
     * @return ResponseEntity with http status 200 (in case of success), 500 (in case of error)
     */
    @RequestMapping(path = "/upload",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_HTML_VALUE,
        consumes = {"multipart/form-data"})
    public ResponseEntity upload(MultipartHttpServletRequest body) {
        return super.upload(body);
    }

}
```

### (AngularJS) ngFlow example

Include `ng-flow-standalone.js` in your index.html file. 

```html
        <!-- concatenated flow.js + ng-flow libraries -->
       <script src="../js/ng-flow-standalone.js"></script>
```
Add `flow`  to your module and configure file upload using `flowFactoryProvider`
```javascript
var app = angular.module('myApp',['flow']);

app.config(["flowFactoryProvider",
    function(flowFactoryProvider) {
    
    
          // # #####################################################################
          // # Configure file upload
          // # #####################################################################
    
          flowFactoryProvider.defaults = {
            target: "/file-upload/upload",
            permanentErrors: [404, 500, 501],
            maxChunkRetries: 1,
            chunkRetryInterval: 5000,
            simultaneousUploads: 4,
            generateUniqueIdentifier: function() {
              return "xxxxxxxx-xxxx-xxxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
                var r, v;
                r = Math.random() * 16 | 0;
                v = (c === "x" ? r : r & 0x3 | 0x8);
                return v.toString(16);
              });
            }
          };
    
```
### Antivirus support

The `qlack-fuse-fileupload` module provides antivirus file scanning for the uploaded files. To enable it you need to 
provide in your application an implementation of the `AvService` of the `qlack-util-av-api` module. Such an 
implementation is already included in the `qlack-util-clam-av` module. 

To enable antivirus support follow these steps: 

1) Add the dependency for a module containing an implementation of the AvService in the Spring boot 
application

```xml
<dependency>
      <groupId>com.eurodyn.qlack.util</groupId>
      <artifactId>qlack-util-clam-av</artifactId>
      <version>${project.version}</version>
    </dependency>
```

2) Add the package of an implementation class for AvService in the Spring boot application main class declaration:

```java
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.fileupload.repository")
@EntityScan("com.eurodyn.qlack.fuse.fileupload.model")
@ComponentScan(basePackages = {
   ...
    ,"com.eurodyn.qlack.util.clamav" // package of the qlack-util-clam-av module 
                                    // containing an implementation of AvService class
})
```
