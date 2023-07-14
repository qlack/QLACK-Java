package com.eurodyn.qlack.fuse.fileupload.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "flu_file")
public class DBFile {

  /**
   * Composite key id
   */
  @EmbeddedId
  private DBFilePK dbFilePK;

  /**
   * User that uploaded the file
   */
  @Column(name = "uploaded_by")
  private String uploadedBy;

  /**
   * Filename
   */
  @Column(name = "file_name")
  private String fileName;

  /**
   * File upload time
   */
  @Column(name = "uploaded_at")
  private long uploadedAt;

  /**
   * Total file size
   */
  @Column(name = "file_size")
  private long fileSize;

  /**
   * The number of chunks the file was divided to
   */
  @Column(name = "expected_chunks")
  private long expectedChunks;

  /**
   * Current file/chunk data
   */
  @Column(name = "chunk_data")
  @Basic(fetch = FetchType.LAZY)
  private byte[] chunkData;

  /**
   * Current chunk size
   */
  @Column(name = "chunk_size")
  private long chunkSize;

  @Version
  private long dbversion;

  public DBFile(DBFilePK dbFilePK) {
    super();
    this.dbFilePK = dbFilePK;
  }
}
