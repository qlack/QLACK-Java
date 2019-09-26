package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseRequest {

  private boolean async = false;

}
