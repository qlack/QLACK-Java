package com.eurodyn.qlack.fuse.mailing.mappers;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;

@RunWith(MockitoJUnitRunner.class)
public class ContactMapperTest {

  @InjectMocks
  private ContactMapperImpl contactMapperImpl;

  private InitTestValues initTestValues;
  private Contact contact;
  private ContactDTO contactDTO;
  private Set<Contact> contacts;
  private List<ContactDTO> contactsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    contact = initTestValues.createContact();
    contactDTO = initTestValues.createContactDTO();
    contacts = initTestValues.createContacts();
    contactsDTO = initTestValues.createContactsDTO();
  }

  @Test
  public void testMapToDTOId() {
    ContactDTO contactDTO = contactMapperImpl.mapToDTO(contact);
    assertEquals(contact.getId(), contactDTO.getId());
  }

  @Test
  public void testMapToDTOUserID() {
    ContactDTO contactDTO = contactMapperImpl.mapToDTO(contact);
    assertEquals(contact.getUserId(), contactDTO.getUserID());
  }

  @Test
  public void testMapToDTOFirstName() {
    ContactDTO contactDTO = contactMapperImpl.mapToDTO(contact);
    assertEquals(contact.getFirstName(), contactDTO.getFirstName());
  }

  @Test
  public void testMapToDTOLastName() {
    ContactDTO contactDTO = contactMapperImpl.mapToDTO(contact);
    assertEquals(contact.getLastName(), contactDTO.getLastName());
  }

  @Test
  public void testMapToDTOEmail() {
    ContactDTO contactDTO = contactMapperImpl.mapToDTO(contact);
    assertEquals(contact.getEmail(), contactDTO.getEmail());
  }

  @Test
  public void testMapToDTOLocale() {
    ContactDTO contactDTO = contactMapperImpl.mapToDTO(contact);
    assertEquals(contact.getLocale(), contactDTO.getLocale());
  }

  @Test
  public void testMapToDTOList() {
    List<Contact> contactsList = contacts.stream().collect(Collectors.toList());
    contactsDTO = contactMapperImpl.mapToDTO(contactsList);
    assertEquals(contacts.size(), contactsDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    Contact contact = contactMapperImpl.mapToEntity(contactDTO);
    assertEquals(contactDTO.getId(), contact.getId());
  }

  @Test
  public void testMapToEntityUserID() {
    Contact contact = contactMapperImpl.mapToEntity(contactDTO);
    assertEquals(contactDTO.getUserID(), contact.getUserId());
  }

  @Test
  public void testMapToEntityFirstName() {
    Contact contact = contactMapperImpl.mapToEntity(contactDTO);
    assertEquals(contactDTO.getFirstName(), contact.getFirstName());
  }

  @Test
  public void testMapToEntityLastName() {
    Contact contact = contactMapperImpl.mapToEntity(contactDTO);
    assertEquals(contactDTO.getLastName(), contact.getLastName());
  }

  @Test
  public void testMapToEntityEmail() {
    Contact contact = contactMapperImpl.mapToEntity(contactDTO);
    assertEquals(contactDTO.getEmail(), contact.getEmail());
  }

  @Test
  public void testMapToEntityLocale() {
    Contact contact = contactMapperImpl.mapToEntity(contactDTO);
    assertEquals(contactDTO.getLocale(), contact.getLocale());
  }

  @Test
  public void testMapToEntityList() {
    contacts = contactMapperImpl.mapToEntity(contactsDTO).stream().collect(Collectors.toSet());
    assertEquals(contactsDTO.size(), contacts.size());
  }
}
