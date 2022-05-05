# QLACK Fuse - Mailing

This module is responsible for configuring, storing and sending internal/external mailing of the application.

## Integration

### Add qlack-fuse-mailing dependency to your pom.xml:

```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-mailing</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add qlack-fuse-mailing changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-mailing/qlack.fuse.mailing.changelog.xml"/>
```

### Add the mail configuration properties in the Spring boot application.properties file:
```properties
# Spring mail
# The address of the mail server to use.
spring.mail.host = <mailserver_hostname>
# The port of the mail server to use.
spring.mail.port = <mailserver_port>
# Server authentication
spring.mail.properties.mail.smtp.auth = true
# The user to connect with to the mail server.
spring.mail.username = <mailserver_username>
# The password of the user to connect to the mail server.
spring.mail.password = <mailserver_pasword>
# Whether the email server requires TLS.
spring.mail.properties.mail.smtp.starttls.enable = false
# The number of tries before marking an email as failed.
spring.mail.max_tries = 3
# Whether to output extra logging information. Use only during debugging.
spring.mail.debug = true
# Whether sending emails is activated. By setting this property to `false` you can still use
# qlack-fuse-mailing as a dependency in your project in order to queue emails, however your own
# application will not actually send any emails (in that case, you need another application instance
# running with this property set to `true`)
spring.mail.polling = true
#The initial delay before sending the queued mails. (Optional - if not set default value is 30000)
qlack.fuse.mailing.sendQueuedInitialDelay = 12345
#The interval between sending queued mails. (Optional - if not set default value is 5000)
qlack.fuse.mailing.sendQueuedInterval =  1234 
```

### Add the packages in the Spring boot application main class declaration:

```java
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.mailing.repository")
@EntityScan("com.eurodyn.qlack.fuse.mailing.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.mailing"
})
```

### Example

```java

import com.eurodyn.qlack.fuse.mailing.dto.AttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// ..

   @Autowired
   private MailService mailService;
// ..

    public void queueEmail(){
        String emailId = mailService.queueEmail(createEmailDTO());
        System.out.println("Email with id "+emailId+" has been queued.");
        sendOne(emailId);
    }

    private void sendOne(String emailId){
        mailService.sendOne(emailId);
        System.out.println("Mail with id "+emailId+" has been sent.");
    }

    private List<AttachmentDTO> createAttachmentsDTO(){
        List<AttachmentDTO> attachments = new ArrayList<>();

        try {
            BufferedImage bImage = ImageIO.read(new File("E:\\pic.jpg"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(bImage, "jpg", bos);
            byte [] data = bos.toByteArray();

            AttachmentDTO attachmentDTO = new AttachmentDTO();
            attachmentDTO.setData(data);
            attachmentDTO.setContentType("image/jpeg");
            attachmentDTO.setFilename("pic.jpg");
            attachments.add(attachmentDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return attachments;
    }

    private EmailDTO createEmailDTO(){
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject("QLACK test email");
        emailDTO.setBody("<p>Email content</p>");
        emailDTO.setFromEmail("sender@eurodyn.com");
        emailDTO.setToEmails(Arrays.asList("recipient@eurodyn.com"));
        emailDTO.setEmailType(EmailDTO.EMAIL_TYPE.HTML);
        emailDTO.setAttachments(createAttachmentsDTO());
        return emailDTO;
    }
// ..
```
