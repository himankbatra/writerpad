package com.xebia.fs101.writerpad.services.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class NoOpMailService implements MailService {

    @Override
    public void sendEmail(String subject, String body) {
        System.out.println("Successfully Sent mail !! with " + subject + " and body " + body);
    }
}
