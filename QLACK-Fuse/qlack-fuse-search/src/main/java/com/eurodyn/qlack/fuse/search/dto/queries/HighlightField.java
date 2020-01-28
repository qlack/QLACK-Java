package com.eurodyn.qlack.fuse.search.dto.queries;

public class HighlightField {

  private String field;
  private String type;
  private boolean forceSource;
  private int fragmentSize = 100;
  private int numberOfFragments = 5;
  private int noMatchSize = 0;

  public String getField() {
    return field;
  }

  public HighlightField setField(String field) {
    this.field = field;
    return this;
  }

  public String getType() {
    return type;
  }

  public HighlightField setType(String type) {
    this.type = type;
    return this;
  }

  public boolean isForceSource() {
    return forceSource;
  }

  public HighlightField setForceSource(boolean forceSource) {
    this.forceSource = forceSource;
    return this;
  }

  public int getFragmentSize() {
    return fragmentSize;
  }

  public HighlightField setFragmentSize(int fragmentSize) {
    this.fragmentSize = fragmentSize;
    return this;
  }

  public int getNumberOfFragments() {
    return numberOfFragments;
  }

  public HighlightField setNumberOfFragments(int numberOfFragments) {
    this.numberOfFragments = numberOfFragments;
    return this;
  }

  public int getNoMatchSize() {
    return noMatchSize;
  }

  public HighlightField setNoMatchSize(int noMatchSize) {
    this.noMatchSize = noMatchSize;
    return this;
  }
}