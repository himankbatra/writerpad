package com.xebia.fs101.writerpad.services.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class GMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.receiver}")
    private String receiver;


    public void sendEmail(String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiver);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }

}
