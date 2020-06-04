package com.eurodyn.qlack.fuse.workflow;

import com.eurodyn.qlack.fuse.workflow.model.ProcessFile;

public class InitTestValues {

  public ProcessFile generateProcessFile() {
    ProcessFile processFile = new ProcessFile();
    processFile.setChecksum("null");

    return processFile;
  }

}
