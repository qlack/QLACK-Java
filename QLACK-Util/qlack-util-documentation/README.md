# QLACK Util - Documentation

This module generate application documentation based on the Javadocs using [Enunciate](http://enunciate.webcohesion.com/) and [Swagger-UI](https://swagger.io/tools/swagger-ui/) libraries.

## Integration

### Add qlack-util-documentation dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-documentation</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add the enunciate.xml file under the src/main/resources/enunciate folder

The file enunciate.xml is required during the build, because holds two important pieces of information:
1) The packages to include or exclude from the documentation
2) The application path in order to test the resources

Only `api-classes` and `swagger.basePath` should be edited.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<enunciate xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="http://enunciate.webcohesion.com/schemas/enunciate-2.11.1.xsd">

  <api-classes>
    <include pattern="com.eurodyn.qlack.test.web.controller.packageA.**"/>
    <exclude pattern="com.eurodyn.qlack.test.web.controller.packageB.**"/>
    <include pattern="com.eurodyn.qlack.fuse.aaa.dto.**"/>
    <exclude pattern="com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO"/>
  </api-classes>

  <modules>
    <jackson1 disabled="true"/>
    <jaxb disabled="true"/>
    <jaxws disabled="true"/>
    <idl disabled="true"/>
    <c-xml-client disabled="true"/>
    <csharp-xml-client disabled="true"/>
    <java-xml-client disabled="true"/>
    <java-json-client disabled="true"/>
    <gwt-json-overlay disabled="true"/>
    <obj-c-xml-client disabled="true"/>
    <php-xml-client disabled="true"/>
    <php-json-client disabled="true"/>
    <ruby-json-client disabled="true"/>
    <jaxrs disabled="true"/>
    <javascript-client disabled="true"/>

    <spring-web disabled="false"/>
    <swagger disabled="false" basePath="/my_application_path"/>
  </modules>

</enunciate>
```

### Build your application using the `documentation` profile
`mvn clean install -Pdocumentation`

The documentation can be found on the path **/documentation/index.html** and Swagger-UI is accessible at **/documentation/ui/index.html** 

If your application has secured endpoints using JWT, they can be accessed by including the JWT value in the api_token filed of the navbar.

#### If your project pom.xml does not have QLACK as its parent, then the 'documentation' profile will not be found.
#### You will have to copy it from the [QLACK pom.xml](https://github.com/qlack/QLACK-Java/blob/master/pom.xml) and include it your pom.xml.
#### NOTE: Make sure to change the version of the qlack-util-documentation artifact of the 'unpack' goal to match the QLACK version you are using on your project.
