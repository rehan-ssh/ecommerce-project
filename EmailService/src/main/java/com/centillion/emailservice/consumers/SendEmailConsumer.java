package com.centillion.emailservice.consumers;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.centillion.emailservice.dtos.SendEmailDto;
import com.centillion.emailservice.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class SendEmailConsumer {

    ObjectMapper objectMapper;
    String mailPassword;

    SendEmailConsumer(ObjectMapper objectMapper, @Value("${mail.password}") String mailPassword) {
        this.objectMapper = objectMapper;
        this.mailPassword = mailPassword;
    }


    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void consume(String message) {
        System.out.println(message);
        SendEmailDto sendEmailDto = objectMapper.readValue(message, SendEmailDto.class);



        final String fromEmail = sendEmailDto.getFrom(); //requires valid gmail id
        final String password = mailPassword; // correct password for gmail id
        final String toEmail = sendEmailDto.getTo(); // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, toEmail, sendEmailDto.getSubject(), sendEmailDto.getBody());

    }
}
