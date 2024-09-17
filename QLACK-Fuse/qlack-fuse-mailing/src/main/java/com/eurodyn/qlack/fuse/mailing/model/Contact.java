package com.eurodyn.qlack.fuse.mailing.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a contact.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "mai_contact")
@Getter
@Setter
public class Contact extends MailingModel {

  private static final long serialVersionUID = 1L;

  /**
   * Email
   */
  @Column(name = "email", nullable = false, length = 45)
  private String email;

  /**
   * The contact first name
   */
  @Column(name = "first_name", length = 254)
  private String firstName;

  /**
   * The contact last name
   */
  @Column(name = "last_name", length = 254)
  private String lastName;

  /**
   * The contact default locale
   */
  @Column(name = "locale", length = 5)
  private String locale;

  /**
   * The id of the corresponding user
   */
  @Column(name = "user_id", length = 36)
  private String userId;

  /**
   * The distribution lists in which this contact is included
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "mai_distr_list_has_contact", joinColumns = {
          @JoinColumn(name = "contact_id", nullable = false, insertable=false , updatable = false)}, inverseJoinColumns = {
          @JoinColumn(name = "distribution_list_id", nullable = false, insertable = false, updatable = false)})
  private Set<DistributionList> distributionLists = new HashSet<>(0);

}
