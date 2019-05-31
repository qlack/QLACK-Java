package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;

@Mapper(componentModel = "spring")
public interface InternalMessageMapper extends MailingMapper<InternalMessage, InternalMessageDTO> {

}
