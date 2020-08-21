# mavenizr

`mavenizr` is a tool that helps to minimize the time needed to install local jar dependencies on your local maven repository and use them directly in your maven project `pom` file. It allows for quicker transition of legacy projects with local jar files dependencies to a cleaner maven solution. 

 ## Quick start 

Clone or download the QLACK-JAVA project source code from [github](https://github.com/qlack/QLACK-Java). 

Go to QLACK-Tools/qlack-tools-mavenizr folder and run:

```cmd
mvn clean package
```

After a successful build you can run the following, adjusting the options values to your project:

```cmd
java -jar target/qlack-tools-mavenizr-1.0.jar --deps --exec --d=C:/my_maven_project/lib --c=lib --g=com.eurodyn.my_maven_project 

```

or if you prefer maven you can also run:

```reStructuredText
mvn spring-boot:run -Dspring-boot.run.arguments="--deps,--exec,--d=C:/my_maven_project/lib,--c=lib,--g=com.eurodyn.my_maven_project"
```

Next, <u>copy the dependencies and executions output to your projects' pom.xml file</u>. For details see "Copying output to your projects' pom.xml" at the end of this file.

Your project should be ready to build, using the installed jar dependencies.

## How does it work?

For each jar file in a directory defined by the `--d` parameter it generates a dependency and/or an execution node. The jar file name is used as `<artifactId>`, whereas the `<version>`  is a hash of the actual file. Using the `--g` parameter the user defines the `<groupId>` that the output artifacts will be installed under. The `<execution>` configuration node constructs the path for the `<file>` property using the parameter `--c` and the jar file name.

In this way `mavenizr` creates a pair of `<dependency>` and `<execution>` definition nodes. The `maven-install-plugin` uses the execution definition to install the jar file to the local .m2 repository under the defined groupId, artifactId and version. The jar file should also be declared as a dependency in the `dependencies` section of the pom.xml file, so maven simply resolves the dependency.

## Usage Information

```reStructuredText
___________________________________________________________
Usage
___________________________________________________________
Parameters:
 --d        the jar libraries directory (required)
 --c        the maven execution block configuration file directory (required for --exec option)
 --g        the default groupId for the created dependency/execution nodes (required)
___________________________________________________________
Execution options:
 --deps     generates dependency nodes
 --exec     generates execution nodes
 --u        usage information
___________________________________________________________
```

## Output sample

For dependencies executions option `--deps`  the output will be of the following form: 

```xml
<dependencies>
    <dependency>
        <artifactId>my_custom_jar_filename</artifactId>
        <groupId>com.eurodyn.my_maven_project</groupId>
        <version>2881c79c9d6ef01c58e62beea13e9d1ac8b8baa16f2fc198ad6e6776defdcdd3</version>
    </dependency>
<!-- Other dependencies... -->
</dependencies>
    
```

For executions option `--exec` the output will be of the following form:

```xml
<executions>
    <execution>
            <configuration>
                <artifactId>my_custom_jar_filename</artifactId>
                <file>lib/my_custom_jar_file.jar</file>
                <groupId>com.eurodyn.my_maven_project</groupId>
                <packaging>jar</packaging>
                <version>2881c79c9d6ef01c58e62beea13e9d1ac8b8baa16f2fc198ad6e6776defdcdd3</version>
            </configuration>
            <goals>
                <goal>install-file</goal>
            </goals>
            <id>my_custom_jar_filename</id>
            <phase>install</phase>
    </execution>
<!-- Other executions... -->
</executions>
```



## Copying output to your projects' pom.xml

Assuming you have a module `mymodule-server` pom.xml where you need to include local jar files as dependencies, **you need to create in the same level (same parent) another module i.e. `mymodule-libs`.** This module will have the sole responsibility of installing the local jar files on your local m2 repository. Naturally `mymodule-libs` will include a `lib` folder containing all the jar files needed to be installed. 

Your project should have the following tree structure:

```txt
- my_maven_project
	|_ mymodule-libs
		|_ lib
			|_ my_custom_jar_file.jar
		|_ pom.xml (where the output of --exec option is included)
	|_ mymodule-server
		|_ pom.xml (where the output of --deps option is included)
```



The pom.xml file of `mymodule-libs` module should include the maven-install-plugin and the defined `executions` which you can copy from the output of the `--exec` option and should look like the following example: 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.eurodyn.qlack</groupId>
        <artifactId>qlack-be-forms</artifactId>
        <version>3.3.1-SNAPSHOT</version>
    </parent>

    <artifactId>qlack-be-forms-libs</artifactId>

    <packaging>pom</packaging>

    <build>
        <plugins>           
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-install-plugin</artifactId>
                <version>${maven-install-plugin.version}</version>
                <!-- Here you should copy the output of the --executions option of the mavenizr tool -->
                <!-- i.e. 
			   <executions>
                    <execution>
                        <configuration>
                            <artifactId>my_custom_jar_filename</artifactId>
                            <file>lib/my_custom_jar_file.jar</file>
                            <groupId>com.eurodyn.my_maven_project</groupId>
                            <packaging>jar</packaging>
                    <version>2881c79c9d6ef01c58e62beea13e9d1ac8b8baa16f2fc198ad6e6776defdcdd3</version>
                        </configuration>
                        <goals>
                            <goal>install-file</goal>
                        </goals>
                        <id>my_custom_jar_filename</id>
                        <phase>install</phase>
                    </execution>
                </executions> -->
            </plugin>
        </plugins>
    </build>
</project>
```

Then in the`mymodule-server` module pom.xml copy the output of the `--deps` (dependencies) option and paste it in the pom.xml `<dependencies></dependencies>` section like the example below: 

```xml
<dependencies>
    <dependency>
        <artifactId>my_custom_jar_filename</artifactId>
        <groupId>com.eurodyn.my_maven_project</groupId>
        <version>2881c79c9d6ef01c58e62beea13e9d1ac8b8baa16f2fc198ad6e6776defdcdd3</version>
    </dependency>
<!-- Other dependencies... -->
</dependencies>
    
```
