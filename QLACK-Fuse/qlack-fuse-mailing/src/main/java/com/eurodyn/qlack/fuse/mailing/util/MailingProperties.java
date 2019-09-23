package com.eurodyn.qlack.fuse.mailing.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Properties class for qlack-fuse-mailing module.
 * This class extends the default Spring Boot {@link MailProperties} class.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@Primary
@Configuration
public class MailingProperties extends MailProperties {

    /**
     * Whether sending emails is activated.
     */
    private boolean polling;

    /**
     * The number of tries before marking an email as failed.
     */
    private byte maxTries;

    /**
     * Whether to output extra logging information. Use only during debugging.
     */
    private boolean debug;
}
