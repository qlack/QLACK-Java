package com.eurodyn.qlack.fuse.aaa.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_user_attributes database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_user_attributes")
@Getter
@Setter
public class UserAttribute extends AAAModel {

  private static final long serialVersionUID = 1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * the bin data
   */
  private byte[] bindata;

  /**
   * the content type
   */
  @Column(name = "content_type")
  private String contentType;

  /**
   * the data
   */
  private String data;

  /**
   * the name
   */
  private String name;

  /**
   * bi-directional many-to-one association to User
   **/
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public UserAttribute() {
    setId(UUID.randomUUID().toString());
  }

}
