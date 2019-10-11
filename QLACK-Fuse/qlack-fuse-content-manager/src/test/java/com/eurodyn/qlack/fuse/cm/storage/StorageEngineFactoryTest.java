package com.eurodyn.qlack.fuse.cm.storage;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class StorageEngineFactoryTest {

  @InjectMocks
  private StorageEngineFactory storageEngineFactory;

  @Mock
  private DBStorage dbStorage;

  @Mock
  private FSStorage fsStorage;

  @Before
  public void init() {
    storageEngineFactory = new StorageEngineFactory();
  }

  @Test
  public void getFsEngineTest() {
    storageEngineFactory.setFsStorage(fsStorage);
    ReflectionTestUtils.setField(storageEngineFactory, "defaultStorageStrategy", "FS_STORAGE");
    assertEquals(fsStorage, storageEngineFactory.getEngine());
  }

  @Test
  public void getDBEngineTest() {
    storageEngineFactory.setDbStorage(dbStorage);
    ReflectionTestUtils.setField(storageEngineFactory, "defaultStorageStrategy", "DB_STORAGE");
    assertEquals(dbStorage, storageEngineFactory.getEngine());
  }

}
