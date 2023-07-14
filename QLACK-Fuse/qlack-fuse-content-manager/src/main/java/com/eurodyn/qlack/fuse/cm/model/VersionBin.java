package com.eurodyn.qlack.fuse.cm.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version_bin")
@Getter
@Setter
public class VersionBin extends QlackBaseModel {

  @jakarta.persistence.Version
  private long dbversion;

  @ManyToOne
  @JoinColumn(name = "version_id")
  private Version version;

  @Column(name = "chunk_index")
  private int chunkIndex;

  @Column(name = "bin_content")
  private byte[] binContent;

  @Column(name = "chunk_size")
  private int chunkSize;

  public void setBinContent(byte[] binContent) {
    this.binContent = binContent;
    if (binContent != null) {
      this.chunkSize = binContent.length;
    }
  }
}
