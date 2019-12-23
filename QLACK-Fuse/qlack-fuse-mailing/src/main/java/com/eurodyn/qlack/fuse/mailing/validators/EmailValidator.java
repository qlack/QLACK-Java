package com.eurodyn.qlack.fuse.mailing.validators;

import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class EmailValidator {

  public boolean isValid(EmailDTO emailDTO) {

    // At least one recipient must be defined
    return !CollectionUtils.isEmpty(emailDTO.getToEmails()) || !CollectionUtils
      .isEmpty(emailDTO.getCcEmails()) || !CollectionUtils
      .isEmpty(emailDTO.getBccEmails());
  }
}
