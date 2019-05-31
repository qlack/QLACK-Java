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

@Entity
@Table(name = "mai_contact")
@Getter
@Setter
public class Contact extends MailingModel {

	private static final long serialVersionUID = 1L;

	@Column(name = "email", nullable = false, length = 45)
	private String email;

	@Column(name = "first_name", length = 254)
	private String firstName;

	@Column(name = "last_name", length = 254)
	private String lastName;

	@Column(name = "locale", length = 5)
	private String locale;

	@Column(name = "user_id", length = 36)
	private String userId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "mai_distr_list_has_contact", joinColumns = {
			@JoinColumn(name = "contact_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "distribution_list_id", nullable = false, updatable = false) })
	private Set<DistributionList> distributionLists = new HashSet<DistributionList>(0);

}
