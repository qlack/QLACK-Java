package com.eurodyn.qlack.fuse.lexicon.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * A Util class for the LanguageService class operations.
 *
 * @author European Dynamics SA
 */
public class WorkbookUtil {

  /**
   * Creates a new instance of the HSSFWorkbook class.
   *
   * @return the HSSFWorkbook instance
   */
  public static HSSFWorkbook createHssfWorkbook() {
    return new HSSFWorkbook();
  }

}
