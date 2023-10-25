package com.eurodyn.qlack.fuse.cm.storage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class StorageEngineFactoryTest {

  @InjectMocks
  private StorageEngineFactory storageEngineFactory;

  @Mock
  private DBStorage dbStorage;

  @Mock
  private FSStorage fsStorage;

  @BeforeEach
  public void init() {
    storageEngineFactory = new StorageEngineFactory();
  }

  @Test
  public void getFsEngineTest() {
    storageEngineFactory.setFsStorage(fsStorage);
    ReflectionTestUtils
      .setField(storageEngineFactory, "defaultStorageStrategy", "FS_STORAGE");
    assertEquals(fsStorage, storageEngineFactory.getEngine());
  }

  @Test
  public void getDBEngineTest() {
    storageEngineFactory.setDbStorage(dbStorage);
    ReflectionTestUtils
      .setField(storageEngineFactory, "defaultStorageStrategy", "DB_STORAGE");
    assertEquals(dbStorage, storageEngineFactory.getEngine());
  }

}
