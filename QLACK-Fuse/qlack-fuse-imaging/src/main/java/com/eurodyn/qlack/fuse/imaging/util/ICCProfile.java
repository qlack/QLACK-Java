package com.eurodyn.qlack.fuse.imaging.util;

/**
 * A list of ICC color profiles to be used in colorspace conversions.
 */
@SuppressWarnings("squid:S00115")
public enum ICCProfile {
  // Official profiles from http://www.color.org
  CGATS21_CRPC1,
  CGATS21_CRPC2,
  CGATS21_CRPC3,
  CGATS21_CRPC4,
  CGATS21_CRPC5,
  CGATS21_CRPC6,
  CGATS21_CRPC7,
  Coated_Fogra39L_VIGC_260,
  Coated_Fogra39L_VIGC_300,
  GRACoL2006_Coated1v2,
  GRACoL2013_CRPC6,
  GRACoL2013UNC_CRPC3,
  SC_paper_eci,
  SNAP2007,
  SWOP2006_Coated3v2,
  SWOP2006_Coated5v2,
  SWOP2013C3_CRPC5,
  Uncoated_Fogra47L_VIGC_260,
  Uncoated_Fogra47L_VIGC_300,

  // Custom profiles
  CoatedFOGRA27
}
