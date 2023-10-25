package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContactMapperImplTest {

  @InjectMocks
  private ContactMapperImpl contactMapper;

  private InitTestValues initTestValues;

  @BeforeEach
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
    assertNull(contactMapper.mapToDTO((Contact) null));

    List<ContactDTO> contactDTOS = contactMapper.mapToDTO((List<Contact>) null);
    assertNull(contactDTOS);
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
    assertNull(contactMapper.mapToEntity((ContactDTO) null));

    Contact contact = contactMapper.mapToEntity((ContactDTO) null);
    assertNull(contact);
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
    assertNull(contactMapper.mapToEntity((List<ContactDTO>) null));
    List<Contact> contacts = contactMapper.mapToEntity((List<ContactDTO>) null);
    assertNull(contacts);

  }


}
