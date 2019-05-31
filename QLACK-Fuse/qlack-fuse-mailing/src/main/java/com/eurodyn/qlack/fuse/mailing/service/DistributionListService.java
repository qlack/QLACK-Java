package com.eurodyn.qlack.fuse.mailing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.ContactMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.DistributionListMapper;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.repository.ContactRepository;
import com.eurodyn.qlack.fuse.mailing.repository.DistributionListRepository;

/**
 * Provide distribution list related services. For details regarding the
 * functionality offered see the respective interfaces.
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
@Transactional
public class DistributionListService {

	private final DistributionListRepository distributionListRepository;
	private final ContactRepository contactRepository;

	private DistributionListMapper distributionListMapper;
	private ContactMapper contactMapper;

	public DistributionListService(DistributionListRepository distributionListRepository,
			ContactRepository contactRepository, DistributionListMapper distributionListMapper,
			ContactMapper contactMapper) {
		this.distributionListRepository = distributionListRepository;
		this.contactRepository = contactRepository;
		this.distributionListMapper = distributionListMapper;
		this.contactMapper = contactMapper;
	}

	/**
	 * Create a new distribution list.
	 */
	public void createDistributionList(DistributionListDTO dto) {
		DistributionList dlist = distributionListMapper.mapToEntity(dto);
		distributionListRepository.save(dlist);
	}

	/**
	 * Edit an existing distribution list.
	 */
	public void editDistributionList(DistributionListDTO dto) {
		// String id = dto.getId();
		DistributionList dlist = distributionListMapper.mapToEntity(dto);
		// dlist.setId(id);
		distributionListRepository.save(dlist);
	}

	/**
	 * Delete a distribution list.
	 */
	public void deleteDistributionList(String id) {
		DistributionList dlist = findById(id);
		distributionListRepository.delete(dlist);

	}

	/**
	 * Find a specific distribution list.
	 */
	public DistributionListDTO find(Object id) {
		DistributionList dlist = findById(id);
		return distributionListMapper.mapToDTO(dlist);
	}

	/**
	 * Find DistributionList Entity object for provided id.
	 *
	 * @return DistributionList
	 */
	private DistributionList findById(Object id) {
		return distributionListRepository.fetchById(id.toString());
	}

	/**
	 * Search for a specific distribution list, with the criteria provided. (Only
	 * the name can be provided as criteria at the moment.)
	 */
	public List<DistributionListDTO> search(String name) {
		List<DistributionList> distributionList = null;
		if (name == null) {
			distributionList = distributionListRepository.findAll();
		} else {
			distributionList = distributionListRepository.findByName(name);
		}

		List<DistributionListDTO> distributionDtoList = new ArrayList<>();
		for (DistributionList distribution : distributionList) {
			DistributionListDTO distributionListDto = distributionListMapper.mapToDTO(distribution);
			distributionDtoList.add(distributionListDto);
		}
		return distributionDtoList;
	}

	/**
	 * Create a new contact.
	 *
	 * @return id of contact
	 */
	public String createContact(ContactDTO dto) {
		Contact contact = contactMapper.mapToEntity(dto);
		contactRepository.save(contact);
		return contact.getId();
	}

	/**
	 * Add a contact to a distribution list.
	 */
	public void addContactToDistributionList(String distributionId, String contactId) {
		DistributionList dlist = findById(distributionId);
		Contact contact = findContactById(contactId);
		dlist.getContacts().add(contact);
	}

	/**
	 * Remove a contact from a distribution list.
	 */
	public void removeContactFromDistributionList(String distributionId, String contactId) {
		DistributionList dlist = findById(distributionId);
		Contact contact = findContactById(contactId);
		dlist.getContacts().remove(contact);
	}

	private Contact findContactById(String contactId) {
		return contactRepository.fetchById(contactId);
	}
}
