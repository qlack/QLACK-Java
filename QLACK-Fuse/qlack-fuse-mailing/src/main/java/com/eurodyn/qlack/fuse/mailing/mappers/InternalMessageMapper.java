package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;

/**
 * Mapping interface for <tt>InternalMessage</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring")
public interface InternalMessageMapper extends MailingMapper<InternalMessage, InternalMessageDTO> {

}
