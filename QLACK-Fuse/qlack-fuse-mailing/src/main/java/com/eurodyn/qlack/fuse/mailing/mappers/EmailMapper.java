package com.eurodyn.qlack.fuse.mailing.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.model.Email;

@Mapper(componentModel = "spring")
public interface EmailMapper extends MailingMapper<Email, EmailDTO> {

	default Email mapToEntityWithRecipilents(EmailDTO dto, boolean icludeReceipliens) {
		Email email = mapToEntity(dto);
		if (icludeReceipliens) {
			email.setToEmails(mapListToCsv(dto.getToEmails()));
			email.setCcEmails(mapListToCsv(dto.getCcEmails()));
			email.setBccEmails(mapListToCsv(dto.getBccEmails()));
			email.setReplyToEmails(mapListToCsv(dto.getReplyToEmails()));
		}
		return email;
	}

	default String mapListToCsv(List<String> emails) {
		StringBuilder emailAddress = new StringBuilder();
		if (emails != null && !emails.isEmpty()) {
			for (String email : emails) {
				if (emailAddress.length() > 0) {
					emailAddress.append(",");
				}
				emailAddress.append(email);
			}
		}
		return emailAddress.length() > 0 ? emailAddress.toString() : null;
	}

	default List<String> mapCsvToList(String emails) {
		List<String> contacts = new ArrayList<>();
		if (emails == null) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(emails, ",");
		while (st.hasMoreElements()) {
			String next = (String) st.nextElement();
			contacts.add(next);
		}
		return contacts.isEmpty() ? null : contacts;
	}

	default EmailDTO mapToDTOyWithRecipilents(Email email, boolean icludeReceipliens) {

		EmailDTO maildto = mapToDTO(email);
		if (icludeReceipliens) {
			maildto.setToEmails(mapCsvToList(email.getToEmails()));
			maildto.setBccEmails(mapCsvToList(email.getBccEmails()));
			maildto.setCcEmails(mapCsvToList(email.getCcEmails()));
			maildto.setReplyToEmails(mapCsvToList(email.getReplyToEmails()));
		}
		return maildto;
	}

}
