package com.eurodyn.qlack.fuse.cm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinChunkDTO {

  private String id;
  private String versionID;
  private byte[] binContent;
  private boolean hasMoreChunks = false;
  private int chunkIndex = 0;
}

