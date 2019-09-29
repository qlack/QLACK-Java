package com.eurodyn.qlack.fuse.lexicon.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilConstructorsTest {

  @Test
  public void constantsTest(){
    assertEquals(Constants.class, new Constants().getClass());
  }

  @Test
  public void workbookUtilTest(){
    assertEquals(WorkbookUtil.class, new WorkbookUtil().getClass());
  }
}
