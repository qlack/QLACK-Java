package com.eurodyn.qlack.fuse.fileupload.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBFileDTO {

  /**
   * Uploaded file id
   */
  private String id;

  /**
   * Current chunk size
   */
  long chunkSize;

  /**
   * Current chunk order number
   */
  long chunkNumber;

  /**
   * Current file/chunk data
   */
  private byte[] fileData;

  /**
   * Filename
   */
  private String filename;

  /**
   * File upload time
   */
  private long uploadedAt;

  /**
   * User that uploaded the file
   */
  private String uploadedBy;

  /**
   * The number of chunks the file was divided to
   */
  private long totalChunks;

  /**
   * The number of chunks received
   */
  private long receivedChunks;

  /**
   * The time needed to reassemble the file
   */
  private long reassemblyTime;

  /**
   * Total file size
   */
  private long totalSize;

  /**
   * The file was divided to more than one chunks
   */
  private boolean hasMoreChunks;

}
