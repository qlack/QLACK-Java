package com.eurodyn.qlack.fuse.mailing.mapper;

import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>Contact</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactMapper extends MailingMapper<Contact, ContactDTO> {

}
