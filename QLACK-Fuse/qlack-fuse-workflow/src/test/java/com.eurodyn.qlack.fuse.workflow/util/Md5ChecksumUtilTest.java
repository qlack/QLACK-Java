package com.eurodyn.qlack.fuse.workflow.util;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.eurodyn.qlack.common.util.Md5ChecksumUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Md5ChecksumUtilTest {

  @Test
  public void getMd5HexTest() throws IOException {
    assertNotNull(Md5ChecksumUtil.getMd5Hex(new ByteArrayInputStream("input".getBytes())));
  }

}
