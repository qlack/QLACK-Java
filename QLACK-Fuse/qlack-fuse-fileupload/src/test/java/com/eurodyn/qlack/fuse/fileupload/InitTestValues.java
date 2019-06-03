package com.eurodyn.qlack.fuse.fileupload;


import java.util.ArrayList;
import java.util.List;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.model.DBFilePK;
import com.eurodyn.qlack.util.av.api.dto.VirusScanDTO;

/**
 * @author European Dynamics
 */
public class InitTestValues {

  byte[] data = {80, 65, 78, 75, 65, 74};

  public DBFilePK createDBFilePK() {
    DBFilePK dBFilePK = new DBFilePK();
    dBFilePK.setId("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
    dBFilePK.setChunkOrder(1L);
    return dBFilePK;
  }

  public DBFilePK createDBFilePK2() {
    DBFilePK dBFilePK = new DBFilePK();
    dBFilePK.setId("ad1f5bb0-e1a9-4960-b0ca-1998fa5a1d6c");
    dBFilePK.setChunkOrder(2L);
    return dBFilePK;
  }

  public DBFile createDBFile() {
    DBFile dbFile = new DBFile();
    dbFile.setDbFilePK(createDBFilePK());
    dbFile.setDbversion(1L);
    dbFile.setFileName("document.pdf");
    dbFile.setFileSize(1024156);
    dbFile.setUploadedAt(System.currentTimeMillis());
    dbFile.setUploadedBy("admin");
    //    dbFile.setChunkSize(512078);
    dbFile.setExpectedChunks(1);
    dbFile.setChunkData(data);
    return dbFile;
  }

  public DBFile createDBFile2() {
    DBFile dbFile = new DBFile();
    dbFile.setDbFilePK(createDBFilePK2());
    dbFile.setDbversion(1L);
    dbFile.setFileName("document.pdf");
    dbFile.setFileSize(1024156);
    dbFile.setUploadedAt(System.currentTimeMillis());
    dbFile.setUploadedBy("admin");
    //    dbFile.setChunkSize(512078);
    dbFile.setExpectedChunks(2);
    dbFile.setChunkData(data);
    return dbFile;
  }

  public DBFile createChunkNo1() {
    DBFile dbFile = new DBFile();
    dbFile.setDbFilePK(createDBFilePK());
    dbFile.setDbversion(1L);
    dbFile.setFileName("document.pdf");
    dbFile.setFileSize(1024156);
    dbFile.setUploadedAt(System.currentTimeMillis());
    dbFile.setUploadedBy("admin");
    //    dbFile.setChunkSize(512078);
    dbFile.setExpectedChunks(2);
    dbFile.setChunkData(data);
    return dbFile;
  }

  public DBFile createChunkNo2() {
    DBFile dbFile = new DBFile();
    dbFile.setDbFilePK(new DBFilePK("",2));
    dbFile.setDbversion(1L);
    dbFile.setFileName("document.pdf");
    dbFile.setFileSize(1024156);
    dbFile.setUploadedAt(System.currentTimeMillis());
    dbFile.setUploadedBy("admin");
    //    dbFile.setChunkSize(512078);
    dbFile.setExpectedChunks(2);
    dbFile.setChunkData(data);
    return dbFile;
  }

  public List<DBFile> createChunks() {
    List<DBFile> chunks = new ArrayList<>();
    chunks.add(createChunkNo1());
    chunks.add(createChunkNo2());
    return chunks;
  }

  public List<String> createChunkIds() {
    List<String> chunks = new ArrayList<>();
    chunks.add(createChunkNo1().getDbFilePK().getId());
    return chunks;
  }

  public List<DBFile> createDBFiles() {
    List<DBFile> dbFiles = new ArrayList<>();
    dbFiles.add(createDBFile());
    //    dbFiles.add(createDBFile2());
    return dbFiles;
  }

  public List<DBFile> createDBFiles2() {
    List<DBFile> dbFiles2 = new ArrayList<>();
    dbFiles2.add(createDBFile());
    dbFiles2.add(createDBFile2());
    return dbFiles2;
  }

  public DBFileDTO createDBFileDTO() {
    DBFileDTO dbFileDTO = new DBFileDTO();
    dbFileDTO.setId(createDBFilePK().getId());
    dbFileDTO.setReceivedChunks(0L);
    dbFileDTO.setFilename("document.pdf");
    dbFileDTO.setTotalSize(1024156);
    dbFileDTO.setUploadedAt(System.currentTimeMillis());
    dbFileDTO.setUploadedBy("admin");
    //    dbFileDTO.setChunkSize(512078);
    dbFileDTO.setTotalChunks(2L);
    dbFileDTO.setFileData(data);
    dbFileDTO.setChunkNumber(1L);
    dbFileDTO.setHasMoreChunks(false);
    return dbFileDTO;
  }

  public List<DBFileDTO> createDBFileDTOs() {
    List<DBFileDTO> dbFileDTOs = new ArrayList<>();
    dbFileDTOs.add(createDBFileDTO());
    return dbFileDTOs;
  }

  public VirusScanDTO createVirusScanVirusFreeDTO() {
    VirusScanDTO virusScanDTO = new VirusScanDTO();
    virusScanDTO.setVirusFree(true);
    virusScanDTO.setVirusScanDescription("OK");
    return virusScanDTO;
  }
}
