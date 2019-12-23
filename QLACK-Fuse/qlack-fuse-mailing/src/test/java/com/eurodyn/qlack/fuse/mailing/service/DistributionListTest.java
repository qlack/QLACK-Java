package com.eurodyn.qlack.fuse.mailing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.mapper.ContactMapper;
import com.eurodyn.qlack.fuse.mailing.mapper.DistributionListMapper;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.repository.ContactRepository;
import com.eurodyn.qlack.fuse.mailing.repository.DistributionListRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class DistributionListTest {

  @InjectMocks
  private DistributionListService distributionListService;

  private DistributionListRepository distributionListRepository = mock(
    DistributionListRepository.class);
  private ContactRepository contactRepository = mock(ContactRepository.class);

  @Spy
  private DistributionListMapper distributionListMapper;
  @Spy
  private ContactMapper contactMapper;

  private InitTestValues initTestValues;

  private DistributionList distributionList;
  private DistributionListDTO distributionListDTO;
  private List<DistributionList> distributionLists;
  private List<DistributionListDTO> distributionListsDTO;
  private Contact contact;
  private ContactDTO contactDTO;

  private final String distributionListId = "49f150a5-478b-440d-b29c-ff8bf54e7015";
  private final String distributionListName = "Qlack Test Distribution List";
  private final String contactId = "0f9a2472-cde0-44a6-ba3d-8e609929043d";

  @Before
  public void init() {
    distributionListService = new DistributionListService(
      distributionListRepository,
      contactRepository,
      distributionListMapper, contactMapper
    );

    initTestValues = new InitTestValues();
    distributionList = initTestValues.createDistributionList();
    distributionListDTO = initTestValues.createDistributionListDTO();
    distributionLists = initTestValues.createDistributionLists();
    distributionListsDTO = initTestValues.createDistributionListsDTO();
    contact = initTestValues.createContact();
    contactDTO = initTestValues.createContactDTO();
  }

  @Test
  public void testCreateDistributionList() {
    when(distributionListMapper.mapToEntity(distributionListDTO))
      .thenReturn(distributionList);
    distributionListService.createDistributionList(distributionListDTO);
    verify(distributionListRepository, times(1)).save(distributionList);
  }

  @Test
  public void testEditDistributionList() {
    when(distributionListMapper.mapToEntity(distributionListDTO))
      .thenReturn(distributionList);
    distributionListService.editDistributionList(distributionListDTO);
    verify(distributionListRepository, times(1)).save(distributionList);
  }

  @Test
  public void testDeleteDistributionList() {
    when(distributionListRepository.fetchById(distributionListId))
      .thenReturn(distributionList);
    distributionListService.deleteDistributionList(distributionListId);
    verify(distributionListRepository, times(1)).delete(distributionList);
  }

  @Test
  public void testFind() {
    when(distributionListRepository.fetchById(distributionListId))
      .thenReturn(distributionList);
    when(distributionListMapper.mapToDTO(distributionList))
      .thenReturn(distributionListDTO);
    DistributionListDTO findDListDTO = distributionListService
      .find(distributionListId);
    assertEquals(distributionListDTO, findDListDTO);
  }

  @Test
  public void testSearchWithName() {
    when(distributionListRepository.findByName(distributionListName))
      .thenReturn(distributionLists);
    for (DistributionList distribution : distributionLists) {
      when(distributionListMapper.mapToDTO(distribution))
        .thenReturn(distributionListDTO);
    }
    List<DistributionListDTO> dlistDTO = distributionListService
      .search(distributionListName);
    assertEquals(distributionListsDTO.size(), dlistDTO.size());
  }

  @Test
  public void testSearchWithNullName() {
    when(distributionListRepository.findAll()).thenReturn(distributionLists);
    for (DistributionList distribution : distributionLists) {
      when(distributionListMapper.mapToDTO(distribution))
        .thenReturn(distributionListDTO);
    }
    List<DistributionListDTO> dlistDTO = distributionListService.search(null);
    assertEquals(distributionListsDTO.size(), dlistDTO.size());
  }

  @Test
  public void testCreateContact() {
    when(contactMapper.mapToEntity(contactDTO)).thenReturn(contact);
    String contactId = distributionListService.createContact(contactDTO);
    verify(contactRepository, times(1)).save(contact);
    assertEquals(contactId, contact.getId());
  }

  @Test
  public void testAddContactToDistributionList() {
    when(distributionListRepository.fetchById(distributionListId))
      .thenReturn(distributionList);
    when(contactRepository.fetchById(contactId)).thenReturn(contact);
    distributionListService
      .addContactToDistributionList(distributionListId, contactId);
    assertTrue(distributionList.getContacts().contains(contact));
  }

  @Test
  public void testRemoveContactFromDistributionList() {
    when(distributionListRepository.fetchById(distributionListId))
      .thenReturn(distributionList);
    when(contactRepository.fetchById(contactId)).thenReturn(contact);
    distributionListService
      .removeContactFromDistributionList(distributionListId, contactId);
    assertFalse(distributionList.getContacts().contains(contact));
  }
}
