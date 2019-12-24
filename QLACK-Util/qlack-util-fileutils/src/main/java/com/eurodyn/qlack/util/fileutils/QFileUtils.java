package com.eurodyn.qlack.util.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

/**
 * File utilities class
 *
 * @author European Dynamics
 */
@Log
@Component
public class QFileUtils {

  /**
   * Zips file(s) and all of its subfolders recursively to a zip file
   *
   * @param fileToZip The file to be zipped
   * @param parentDirName The name of the parent dir to be zipped
   * @param zipOut a {@link java.util.zip.ZipOutputStream} object
   * @throws IOException if any of the I/O related methods fails to be
   * executed as expected
   */

  public void zip(File fileToZip, String parentDirName, ZipOutputStream zipOut)
    throws IOException {

    if (fileToZip == null || !fileToZip.exists() || fileToZip.isHidden()) {
      throw new FileNotFoundException(
        "The file or folder you are trying to zip does not exist or is hidden.");
    }

    String zipEntryName = fileToZip.getName();
    if (parentDirName != null && !parentDirName.isEmpty()) {
      zipEntryName = parentDirName.concat(File.separator)
        .concat(fileToZip.getName());
    }

    if (fileToZip.isDirectory()) {
      for (File nestedFile : Objects.requireNonNull(fileToZip.listFiles())) {
        zip(nestedFile, zipEntryName, zipOut);
      }
    } else {
      try (FileInputStream fis = new FileInputStream(fileToZip)) {
        ZipEntry zipEntry = new ZipEntry(zipEntryName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
          zipOut.write(bytes, 0, length);
        }
        zipOut.closeEntry();
      }
    }
  }

  /**
   * Unzips file and all of its subfolders to the destination folder provided
   *
   * @param zipFile The zip file to be unzipped
   * @param destDir The destination folder in which the file(s) will be
   * unzipped
   * @throws java.io.IOException if any of the I/O related methods fails to be
   * executed as expected
   */
  @SuppressWarnings("squid:S5042")
  public void unzip(String zipFile, String destDir) throws IOException {
    byte[] buffer = new byte[1024];

    try (ZipInputStream zis = new ZipInputStream(
      new FileInputStream(zipFile))) {
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {

        // Validate file creation path to prevent Zip Slip
        File createdFile = createAndValidateZipEntryFile(new File(destDir),
          zipEntry.getName());

        if (zipEntry.isDirectory()) {
          createRequiredFolders(createdFile);
        } else {
          createRequiredFolders(createdFile);

          try (FileOutputStream fos = new FileOutputStream(createdFile)) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
              fos.write(buffer, 0, len);
            }
          }
        }
        zipEntry = zis.getNextEntry();
      }
      zis.closeEntry();
    }
  }

  /**
   * Creates the required folders
   *
   * @param createdFile The file that the created folders will contain
   */
  private static void createRequiredFolders(File createdFile) {
    File parentFolder = createdFile.getParentFile();

    if (!parentFolder.mkdirs()) {
      if (parentFolder.canWrite()) {
        log.log(Level.WARNING,
          String.format("The folder %s already exists in %s",
            createdFile.getName(), parentFolder.getPath()
          ));
      } else {
        log.log(Level.WARNING,
          String.format("The folder %s could not be created in %s",
            createdFile.getName(), parentFolder.getPath()
          ));
      }
    }
  }

  /**
   * Validates file creation path to prevent Zip Slip extraction vulnerability
   * exploit
   *
   * @param destinationDir The target directory
   * @param fileName The name of the file to be created
   * @return the {@link java.io.File}
   * @throws IOException if an error occurs when trying to retrieve the path
   * or if the destination path of the file is outside the target directory
   */
  private static File createAndValidateZipEntryFile(File destinationDir,
    String fileName) throws IOException {
    File destFile = new File(destinationDir, fileName);

    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
      throw new IOException(
        "Created file is outside of the target dir: " + fileName);
    }
    return destFile;
  }

  /**
   * Removes file extension from a filename or path
   *
   * @param filename the filename or path with the extension
   * @return the filename or path with the extension removed
   */
  @SuppressWarnings("squid:S4784")
  public static String removeFileExtension(String filename) {
    return filename != null
      ? filename.replaceAll("\\.[^.\\\\/:*?\"<>|\\r\\n]+$", "")
      : "";
  }
}
