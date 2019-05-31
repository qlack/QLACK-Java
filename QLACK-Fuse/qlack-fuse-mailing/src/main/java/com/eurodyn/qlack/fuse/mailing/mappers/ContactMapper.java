package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper extends MailingMapper<Contact, ContactDTO> {

}
