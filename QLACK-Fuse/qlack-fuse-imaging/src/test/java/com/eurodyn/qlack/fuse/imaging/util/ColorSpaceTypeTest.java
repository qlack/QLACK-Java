package com.eurodyn.qlack.fuse.imaging.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ColorSpaceTypeTest {

  @Test(expected = IllegalArgumentException.class)
  public void reverseValUnknownTest() {
    ColorSpaceType.getReverseVal(-9999);
  }

}
