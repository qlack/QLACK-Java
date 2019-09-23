package com.eurodyn.qlack.fuse.mailing.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

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
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
}
