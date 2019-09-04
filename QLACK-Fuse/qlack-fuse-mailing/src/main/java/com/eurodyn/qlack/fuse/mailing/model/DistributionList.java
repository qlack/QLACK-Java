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
@Table(name = "mai_distribution_list")
@Getter
@Setter
public class DistributionList extends MailingModel {

	private static final long serialVersionUID = 1L;

	@Column(name = "list_name", nullable = false, length = 45)
	private String name;

	@Column(name = "description", length = 45)
	private String description;

	@Column(name = "created_by", length = 254)
	private String createdBy;

	@Column(name = "created_on")
	private Long createdOn;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "mai_distr_list_has_contact", joinColumns = {
			@JoinColumn(name = "distribution_list_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "contact_id", nullable = false, updatable = false) })
	private Set<Contact> contacts = new HashSet<>(0);

}
