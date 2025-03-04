package com.eurodyn.qlack.fuse.mailing.model;

import java.io.Serializable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 * Superclass for all model classes. Holds an Id field.
 *
 * @author European Dynamics SA.
 */
@MappedSuperclass
@Getter
@Setter
public class MailingModel implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Id
   */
  @Id
  @UuidGenerator
  private String id;
}
