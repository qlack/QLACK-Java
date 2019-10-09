package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContactMapperImplTest {

  @InjectMocks
  private ContactMapperImpl contactMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Contact contact = initTestValues.createContact();
    ContactDTO contactDTO = contactMapper.mapToDTO(contact);
    assertEquals(contact.getEmail(), contactDTO.getEmail());

  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null, contactMapper.mapToDTO((Contact) null));

    List<ContactDTO> contactDTOS = contactMapper.mapToDTO((List<Contact>) null);
    assertEquals(null, contactDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<Contact> contacts = new ArrayList<>();
    contacts.add(initTestValues.createContact());
    List<ContactDTO> contactDTOS = contactMapper.mapToDTO(contacts);

    assertEquals(contacts.size(), contactDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    ContactDTO contactDTO = initTestValues.createContactDTO();
    Contact contact = contactMapper.mapToEntity(contactDTO);
    assertEquals(contact.getEmail(), contactDTO.getEmail());

  }

  @Test
  public void mapToEntityNullTest() {
    assertEquals(null, contactMapper.mapToEntity((ContactDTO) null));

    Contact contact = contactMapper.mapToEntity((ContactDTO) null);
    assertEquals(null, contact);
  }


  @Test
  public void mapToEntityListTest() {
    List<ContactDTO> contactDTOS = new ArrayList<>();
    contactDTOS.add(initTestValues.createContactDTO());
    List<Contact> contacts = contactMapper.mapToEntity(contactDTOS);

    assertEquals(contactDTOS.size(), contacts.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertEquals(null, contactMapper.mapToEntity((List<ContactDTO>) null));
    List<Contact> contacts = contactMapper.mapToEntity((List<ContactDTO>) null);
    assertEquals(null, contacts);

  }


}
