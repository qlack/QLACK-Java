package com.eurodyn.qlack.fuse.acv.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VersionDTO {

  private String author;
  private long version;
  private String commitMessage;
  private Instant commitDate;

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("VersionDTO{");
    sb.append("author='").append(author).append('\'');
    sb.append(", version='").append(version).append('\'');
    sb.append(", commitMessage='").append(commitMessage).append('\'');
    sb.append(", commitDate=").append(commitDate);
    sb.append('}');
    return sb.toString();
  }

}
