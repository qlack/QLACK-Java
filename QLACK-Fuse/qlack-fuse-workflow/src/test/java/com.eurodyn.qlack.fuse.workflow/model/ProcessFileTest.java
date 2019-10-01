package com.eurodyn.qlack.fuse.workflow.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProcessFileTest {

  private ProcessFile processFile;

  @Before
  public void init() {
    processFile = new ProcessFile();
  }

  @Test
  public void filenameTest() {
    String filename = "filename";
    processFile.setFilename(filename);
    assertEquals(filename, processFile.getFilename());
  }

  @Test
  public void checksumTest() {
    String checksum = "checksum";
    processFile.setChecksum(checksum);
    assertEquals(checksum, processFile.getChecksum());
  }

}
