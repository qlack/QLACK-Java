package com.eurodyn.qlack.fuse.fd.model;


import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.fd.util.ThreadStatus;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * The Thread Entity that holds data for Thread
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@Table(name = "fd_thread_message")
@Getter
@Setter
public class ThreadMessage extends QlackBaseModel {


  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * The title of the Thread
   */
  @Column(name = "title")
  private String title;

  /**
   * The author of the Thread
   */
  @Column(name = "author")
  private String author;

  /**
   * The creation date
   */
  @Column(name = "created_on", updatable = false)
  @CreatedDate
  private Instant createdOn;

  /**
   * The last update date
   */
  @Column(name = "last_update")
  @LastModifiedDate
  private Instant lastUpdate;

  /**
   * The text body of the Thread
   */
  @Column(name = "body")
  private String body;


  /**
   * The attributes mask
   */
  @Column(name = "attributes_mask")
  private String attributesMask;

  /**
   * The ownership mask
   */
  @Column(name = "ownership_mask")
  private String ownershipMask;

  /**
   * The Thread status
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private ThreadStatus status;

  /**
   * The comment of the status
   */
  @Column(name = "status_comment")
  private String statusComment;

  /**
   * The parent Thread
   */
  @ManyToOne
  @JoinColumn(name = "parent_thread_id")
  private ThreadMessage parentThreadMessage;


  /**
   * Collection of child ThreadMessage instances
   */
  @OneToMany(mappedBy = "parentThreadMessage", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
  private List<ThreadMessage> childThreadMessages;


  /**
   * Collection of the related Votes.
   */
  @OneToMany(mappedBy = "threadMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Vote> votes;

}
