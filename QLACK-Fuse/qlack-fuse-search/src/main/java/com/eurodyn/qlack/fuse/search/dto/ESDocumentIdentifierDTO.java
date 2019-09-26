package com.eurodyn.qlack.fuse.search.dto;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds the minimum necessary information to uniquely identify a document in ES.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@NoArgsConstructor
public class ESDocumentIdentifierDTO implements Serializable {

  private static final long serialVersionUID = 3216613727616909251L;

  /**
   * the index at which this document resides.
   */
  protected String index;

  /**
   * the type of this document
   */
  protected String type;

  /**
   * The unique ID of this document.
   */
  @Id
  protected String id;

  /**
   * If set to true then wait for the changes made by the request to be made visible by a refresh
   * before replying. This does not force an immediate refresh, rather, it waits for a refresh to
   * happen. Elasticsearch automatically refreshes shards that have changed every
   * index.refresh_interval which defaults to one second. That setting is dynamic. Calling the
   * Refresh API or setting refresh to true on any of the APIs that support it will also cause a
   * refresh, in turn causing already running requests with refresh=wait_for to return.
   */
  protected boolean refresh;

  public ESDocumentIdentifierDTO(String index, String type, String id) {
    this(index, type, id, false);
  }

  public ESDocumentIdentifierDTO(String index, String type, String id, boolean refresh) {
    super();
    this.index = index;
    this.type = type;
    this.id = id;
    this.refresh = refresh;
  }

}
