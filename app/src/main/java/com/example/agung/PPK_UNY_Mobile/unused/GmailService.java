package com.example.agung.PPK_UNY_Mobile.unused;

import java.io.IOException;

import javax.mail.MessagingException;

public interface GmailService {
    void setGmailCredentials(GmailCredentials gmailCredentials);

    boolean sendMessage(String recipientAddress, String subject, String body) throws MessagingException, IOException;
}
