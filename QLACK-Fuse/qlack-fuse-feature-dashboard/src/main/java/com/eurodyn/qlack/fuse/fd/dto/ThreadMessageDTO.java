package com.eurodyn.qlack.fuse.fd.dto;

import com.eurodyn.qlack.fuse.fd.util.ThreadStatus;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A simple DTO(Data Transfer Object) that does not contain any business logic but is used in order
 * to retrieve and store ThreadMessage data information.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class ThreadMessageDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 58806752120029887L;

  /**
   * The Thread id
   */
  private String id;
  /**
   * The title refers the section that mapping is taking place
   */
  private String title;


  /**
   * The author of the Thread
   */
  private String author;

  /**
   * The creation time
   */
  Instant createdOn;

  /**
   * The last update time
   */
  private Instant lastUpdate;

  /**
   * The text body of the Thread
   */
  private String body;


  /**
   * The attributes mask
   */
  private String attributesMask;

  /**
   * The ownership mask
   */
  private String ownershipMask;

  /**
   * The Thread status
   */
  private ThreadStatus status;

  /**
   * The comment of the status
   */
  private String statusComment;

  /**
   * The parent Thread-Message
   */
  private ThreadMessageDTO parentThread;


  /**
   * Related votes
   */
  private List<VoteDTO> votes;


  /**
   * To String method to print the textual representation of the ThreadMessage
   *
   * @return Text format of ThreadMessage.
   */
  @Override
  public String toString() {
    return "ThreadMessageDTO{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", createdOn=" + createdOn +
        ", lastUpdate=" + lastUpdate +
        ", status=" + status +
        '}';
  }

}
