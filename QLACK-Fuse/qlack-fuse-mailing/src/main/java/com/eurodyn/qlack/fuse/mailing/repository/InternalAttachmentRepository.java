package com.eurodyn.qlack.fuse.mailing.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;

public interface InternalAttachmentRepository extends MailingRepository<InternalAttachment, String> {

	List<InternalAttachment> findByMessagesId(String messageId);
}
