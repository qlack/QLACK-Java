package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;

@Mapper(componentModel = "spring")
public interface InternalAttachmentMapper extends MailingMapper<InternalAttachment, InternalAttachmentDTO> {

}
