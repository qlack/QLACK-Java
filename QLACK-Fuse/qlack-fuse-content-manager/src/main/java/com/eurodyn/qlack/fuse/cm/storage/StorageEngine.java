package com.eurodyn.qlack.fuse.cm.storage;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;

import java.io.IOException;

public interface StorageEngine {

  void setVersionContent(String versionID, byte[] content);

  byte[] getVersionContent(String versionID) throws IOException;

  String setBinChunk(String versionID, byte[] content, int chunkIndex);

  BinChunkDTO getBinChunk(String versionID, int chunkIndex);

  void deleteVersionBinaries(String versionID);

}
