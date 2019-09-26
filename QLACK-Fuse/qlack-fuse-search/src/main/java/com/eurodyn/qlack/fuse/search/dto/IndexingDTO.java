package com.eurodyn.qlack.fuse.search.dto;

import com.eurodyn.qlack.fuse.search.service.IndexingService;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds a document to be indexed. The source object to be indexed is specified on the
 * <i>sourceObject</i> property and can be internally converted to a JSON string by the underling
 * methods of {@link IndexingService} when convertToJSON is true.
 */
@Getter
@Setter
@NoArgsConstructor
public class IndexingDTO extends ESDocumentIdentifierDTO implements Serializable {

  /**
   * the source object to be indexed.
   */
  private Object sourceObject;

  /**
   * whether to convert sourceObject to JSON or not
   */
  private boolean convertToJSON = true;

  public IndexingDTO(String index, String type, String id, Object sourceObject) {
    this(index, type, id, sourceObject, false);
  }

  public IndexingDTO(String index, String type, String id, Object sourceObject, boolean refresh) {
    super(index, type, id, refresh);
    this.sourceObject = sourceObject;
  }
}
