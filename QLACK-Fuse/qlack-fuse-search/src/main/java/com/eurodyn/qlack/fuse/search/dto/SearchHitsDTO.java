package com.eurodyn.qlack.fuse.search.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchHitsDTO {

  private List<SearchHitDTO> hitsDTO = new ArrayList<>();
  private long totalHits;
  private float maxScore;
}
