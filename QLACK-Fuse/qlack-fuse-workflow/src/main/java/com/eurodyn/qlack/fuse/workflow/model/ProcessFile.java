package com.eurodyn.qlack.fuse.workflow.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This model represents the resource files that have been persisted as activiti workflows. It is used to identify if changes have been to
 * the files, so that a new process version can be created.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "wor_process_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessFile {

  // the filename in the resources folder
  @Id
  private String filename;

  //the MD5 chechsum of the file
  private String checksum;

}
