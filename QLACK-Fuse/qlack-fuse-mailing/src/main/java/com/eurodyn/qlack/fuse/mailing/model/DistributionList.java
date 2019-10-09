package com.eurodyn.qlack.fuse.mailing.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A distribution list is assigned of contacts that emails are distributed to when sent to it. A
 * contact can be assigned to more than one distribution lists.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "mai_distribution_list")
@Getter
@Setter
public class DistributionList extends MailingModel {

  private static final long serialVersionUID = 1L;

  /**
   * Distribution list name
   */
  @Column(name = "list_name", nullable = false, length = 45)
  private String name;

  /**
   * Description of the distribution list
   */
  @Column(name = "description", length = 45)
  private String description;

  /**
   * The username of the user that created the distribution list
   */
  @Column(name = "created_by", length = 254)
  private String createdBy;

  /**
   * The date the distribution list was created represented as a {@link java.lang.Long} number
   */
  @Column(name = "created_on")
  private Long createdOn;

  /**
   * The list of the contacts assigned to the distribution list
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "mai_distr_list_has_contact", joinColumns = {
      @JoinColumn(name = "distribution_list_id", nullable = false, updatable = false)}, inverseJoinColumns = {
      @JoinColumn(name = "contact_id", nullable = false, updatable = false)})
  private Set<Contact> contacts = new HashSet<>(0);

}
