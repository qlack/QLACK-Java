package com.eurodyn.qlack.fuse.fileupload.rest;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.service.FileUpload;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;


/**
 * Provides basic fileupload functionality
 */
@Log
@Component
@NoArgsConstructor
public abstract class FileUploadRestTemplate {

  @Autowired
  private FileUpload fileUpload;

  /**
   * Checks if a chunk has already been uploaded
   *
   * @param id The file id
   * @param chunkNumber The chunk order number
   * @return ResponseEntity.ok() if chunk exists, ResponseEntity.status(HttpStatus.NO_CONTENT) if
   * not
   */
  public ResponseEntity checkChunk(String id, long chunkNumber) {
    return fileUpload.checkChunk(id, chunkNumber) ? ResponseEntity.ok().build()
      : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * Uploads a chunk/file.
   *
   * @param body a {@link MultipartHttpServletRequest} body
   * @return ResponseEntity.ok() if upload succeeds, ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
   * if not
   */
  public ResponseEntity upload(MultipartHttpServletRequest body) {

    try {

      fileUpload.upload(mapMultiPartToDBFileDTO(body));

    } catch (Exception e) {
      log.log(Level.SEVERE, "Could not process file upload.", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok().build();
  }

  /**
   * Maps the Multipart request body content to a DBFileDTO
   *
   * @param body a {@link MultipartHttpServletRequest} body
   * @return a {@link DBFileDTO}
   * @throws IOException if error occurs during multipart data inputstream reading
   */
  private DBFileDTO mapMultiPartToDBFileDTO(MultipartHttpServletRequest body) throws IOException {
    DBFileDTO dbFileDTO = new DBFileDTO();

    dbFileDTO.setId(getString("flowIdentifier", body));
    dbFileDTO.setFilename(getString("flowFilename", body));
    dbFileDTO.setFileData(getBin("file", body));

    if (body.getParameter("flowChunkNumber") != null) {
      dbFileDTO.setChunkNumber(getLong("flowChunkNumber", body).longValue());
    } else {
      dbFileDTO.setChunkNumber(1); // Support for older browsers, where there is always one chunk.
    }

    if (body.getParameter("flowChunkSize") != null) {
      dbFileDTO.setChunkSize(getLong("flowChunkSize", body).longValue());
    }

    if (body.getParameter("flowTotalChunks") != null) {
      dbFileDTO.setTotalChunks(getLong("flowTotalChunks", body).longValue());
    } else {
      dbFileDTO.setTotalChunks(1); // Support for older browsers, where there is always one chunk.
    }

    if (body.getParameter("flowTotalSize") != null) {
      dbFileDTO.setTotalSize(getLong("flowTotalSize", body).longValue());
    }

    return dbFileDTO;
  }

  /**
   * Extracts data array from a {@link MultipartHttpServletRequest} body
   *
   * @param fieldName the name of the data Multipart section
   * @param body a {@link MultipartHttpServletRequest}
   * @return data byte array
   * @throws IOException if error occurs during multipart data inputstream reading
   */
  private byte[] getBin(String fieldName, MultipartHttpServletRequest body)
    throws IOException {
    return body.getFile(fieldName) != null ? IOUtils
      .toByteArray(Objects.requireNonNull(body.getFile(fieldName)).getInputStream()) : null;
  }

  /**
   * Extracts a Multipart parameter to a {@link String} variable
   *
   * @param fieldName the name of the data Multipart parameter
   * @param body a {@link MultipartHttpServletRequest} body
   * @return a {@link String} variable
   */
  private String getString(String fieldName, MultipartHttpServletRequest body) {
    return body.getParameter(fieldName);
  }

  /**
   * Extracts a Multipart parameter to a {@link Long} variable
   *
   * @param fieldName the name of the data Multipart parameter
   * @param body a {@link MultipartHttpServletRequest} body
   * @return a {@link Long} variable
   */
  private Long getLong(String fieldName, MultipartHttpServletRequest body) {
    return body.getParameter(fieldName) != null ? Long.valueOf(body.getParameter(fieldName)) : null;
  }


}
