package com.eurodyn.qlack.fuse.cm.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version_bin")
@Getter
@Setter
public class VersionBin extends QlackBaseModel {

  @javax.persistence.Version
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
    this.chunkSize = binContent.length;
  }
}
