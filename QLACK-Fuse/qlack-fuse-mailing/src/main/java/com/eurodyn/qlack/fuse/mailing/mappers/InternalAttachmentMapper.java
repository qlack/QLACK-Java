package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;

/**
 * Mapping interface for <tt>InternalAttachment</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring")
public interface InternalAttachmentMapper extends MailingMapper<InternalAttachment, InternalAttachmentDTO> {

}
