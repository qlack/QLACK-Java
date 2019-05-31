package com.eurodyn.qlack.fuse.fileupload.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class DBFilePK implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * Uploaded file id
   */
  @Column
  private String id;
  /**
   * Current chunk order number
   */
  @Column(name = "chunk_order")
  private long chunkOrder;

  public DBFilePK(String id, long chunkOrder) {
    super();
    this.id = id;
    this.chunkOrder = chunkOrder;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (chunkOrder ^ (chunkOrder >>> 32));
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DBFilePK other = (DBFilePK) obj;
    if (chunkOrder != other.chunkOrder) {
      return false;
    }
    if (id == null) {
      return other.id == null;
    } else {
      return id.equals(other.id);
    }
  }

}
