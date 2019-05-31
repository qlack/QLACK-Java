package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.Attachment;

@Mapper(componentModel = "spring")
public interface AttachmentMapper  extends MailingMapper<Attachment, AttachmentDTO> {

}
