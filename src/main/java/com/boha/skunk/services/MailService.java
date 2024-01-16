package com.boha.skunk.services;


import com.boha.skunk.util.E;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//@RequiredArgsConstructor

@Service
public class MailService {

    private final JavaMailSender mailSender;
    public static final Logger LOGGER = LoggerFactory.getLogger(MailService.class.getSimpleName());

    @Value("${email}")
    private String email;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHtmlEmail(String to,
                              String message,
                              String subject) throws Exception {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
            mimeMessage.setRecipients(Message.RecipientType.BCC, email);

            mimeMessage.setSubject(subject);

            String htmlContent =
                    "<p>" + message + "</p>";

            mimeMessage.setContent(htmlContent, "text/html; charset=utf-8");

//            mailSender.send(mimeMessage);
            LOGGER.info(E.BELL + E.BELL + E.BELL + " Email message sent to " + to);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(E.RED_DOT+E.RED_DOT+" Error sending email to " + to);
        }
    }
}
