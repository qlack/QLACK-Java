package com.eurodyn.qlack.fuse.mailing.service;

import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.mappers.ContactMapper;
import com.eurodyn.qlack.fuse.mailing.mappers.DistributionListMapper;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.repository.ContactRepository;
import com.eurodyn.qlack.fuse.mailing.repository.DistributionListRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Provide distribution list related services. For details regarding the functionality offered see
 * the respective interfaces.
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
   *
   * @param dto a distribution list DTO
   */
  public void createDistributionList(DistributionListDTO dto) {
    createOrUpdateDitrubtionList(dto);
  }

  /**
   * Edit an existing distribution list.
   *
   * @param dto a distribution list DTO
   */
  public void editDistributionList(DistributionListDTO dto) {
    createOrUpdateDitrubtionList(dto);
  }

  /**
   * Delete a distribution list.
   *
   * @param id the distribution list Id
   */
  public void deleteDistributionList(String id) {
    DistributionList dlist = distributionListRepository.fetchById(id);
    distributionListRepository.delete(dlist);

  }

  /**
   * Find a specific distribution list.
   *
   * @param id the distribution list Id
   * @return the distribution list DTO
   */
  public DistributionListDTO find(String id) {
    DistributionList dlist = distributionListRepository.fetchById(id);
    return distributionListMapper.mapToDTO(dlist);
  }

  /**
   * Search for a specific distribution list, with the criteria provided. (Only the name can be
   * provided as criteria at the moment.)
   *
   * @param name the name as criteria
   * @return list of distribution list DTOs
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
   * @param dto the contact dto source object
   * @return id of contact
   */
  public String createContact(ContactDTO dto) {
    Contact contact = contactMapper.mapToEntity(dto);
    contactRepository.save(contact);
    return contact.getId();
  }

  /**
   * Add a contact to a distribution list.
   *
   * @param distributionId the distribution list Id
   * @param contactId the contact Id
   */
  public void addContactToDistributionList(String distributionId, String contactId) {
    DistributionList dlist = distributionListRepository.fetchById(distributionId);
    Contact contact = findContactById(contactId);
    dlist.getContacts().add(contact);
  }

  /**
   * Find contact by Id
   *
   * @param contactId the contact Id
   * @return a contact
   */
  private Contact findContactById(String contactId) {
    return contactRepository.fetchById(contactId);
  }

  /**
   * Remove a contact from a distribution list.
   *
   * @param distributionId the Id of the distribution list
   * @param contactId the Id of the contact
   */
  public void removeContactFromDistributionList(String distributionId, String contactId) {
    DistributionList dlist = distributionListRepository.fetchById(distributionId);
    Contact contact = findContactById(contactId);
    dlist.getContacts().remove(contact);
  }

  private void createOrUpdateDitrubtionList(DistributionListDTO dto){
    DistributionList dlist = distributionListMapper.mapToEntity(dto);
    distributionListRepository.save(dlist);
  }
}
