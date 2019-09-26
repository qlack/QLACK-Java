package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic abstract class for requests
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public abstract class BaseRequest {

  /**
   * Flag to indicate whether the request is asynchronous
   */
  private boolean async = false;

}
