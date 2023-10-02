package com.eurodyn.qlack.fuse.fd.model;


import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.fd.util.Reaction;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

/**
 * The Thread Entity that holds data for Thread
 *
 * @author European Dynamics SA
 */
@Entity
@Cacheable
@Table(name = "fd_vote")
@Getter
@Setter
public class Vote extends QlackBaseModel {


  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * The comment of the status
   */
  @Column(name = "voter_id")
  private String voterId;

  /**
   * The creation Date.
   */
  @CreatedDate
  @Column(name = "created_on", updatable = false)
  private Instant createdOn;

  /**
   * The vote reaction.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "reaction")
  private Reaction reaction;


  /**
   * The related ThreadMessage
   */
  @ManyToOne
  @JoinColumn(name = "thread")
  private ThreadMessage threadMessage;


}
