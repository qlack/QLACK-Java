# QLACK Util - FileUtils

This module provides basic file utility methods.

## Integration

### Add qlack-util-fileutils dependency to your pom.xml:

```xml
    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-fileutils</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

## Available methods

* *zip* : Zips file(s) and all of its subfolders recursively to a zip file
* *unzip* : Unzips file and all of its subfolders recursively to a destination folder. This method also provides 
protection from zip slip vulnerability ([more on Zip Slip vulnerability](https://snyk.io/research/zip-slip-vulnerability)).
* *removeFileExtension* : Removes file extension from a filename or path

## Examples

Zip a file or folder

```java
try (
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("path/to/zip/file"))
    ) {
    
    // If the second argument is null or empty, then the folder or file is zipped as is (at the root level) to the zip 
    file.
      fileUtils.zip(new File("path/to/file/or/folder"), null , zipOut);
    } catch (IOException e) {
      log.log(Level.WARNING, e.toString());
    }
```

Zip with a parent folder

```java
try (
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("path/to/zip/file"))
    ) {
    // If the second argument is not null, then the folder or file is moved to a parent folder with the name 
    // provided and then zipped to the zip file
      fileUtils.zip(new File("path/to/file/or/folder"), "name_of_root_parent_directory" , zipOut);
    } catch (IOException e) {
      log.log(Level.WARNING, e.toString());
    }
```

Unzip file

```java
try {
    fileUtils.unzip("path_to_zip_file", "path_to_destination_directory");
    } catch (IOException e) {
      log.log(Level.WARNING, e.toString());
    }
```



