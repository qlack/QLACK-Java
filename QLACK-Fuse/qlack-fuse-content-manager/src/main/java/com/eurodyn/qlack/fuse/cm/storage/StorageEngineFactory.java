package com.eurodyn.qlack.fuse.cm.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StorageEngineFactory {

  @Autowired
  private DBStorage dbStorage;

  @Autowired
  private FSStorage fsStorage;

  @Value("${qlack.fuse.cm.storageStrategy:DB_STORAGE}")
  private String defaultStorageStrategy;

  /**
   * @param dbStorage the dbStorage to set
   */
  public void setDbStorage(DBStorage dbStorage) {
    this.dbStorage = dbStorage;
  }

  /**
   * @param fsStorage the fsStorage to set
   */
  public void setFsStorage(FSStorage fsStorage) {
    this.fsStorage = fsStorage;
  }

  public StorageEngine getEngine(StorageEngineType type) {
    switch (type) {
      case FS_STORAGE:
        return fsStorage;
      case DB_STORAGE:
      default:
        return dbStorage;
    }
  }

  public StorageEngine getEngine() {
    return getEngine(StorageEngineType.valueOf(defaultStorageStrategy));
  }
}
