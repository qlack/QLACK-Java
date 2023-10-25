package com.eurodyn.qlack.fuse.imaging.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ColorSpaceTypeTest {

  @Test
  public void reverseValUnknownTest() {
    assertThrows(IllegalArgumentException.class, () -> ColorSpaceType.getReverseVal(-9999));

  }


}
