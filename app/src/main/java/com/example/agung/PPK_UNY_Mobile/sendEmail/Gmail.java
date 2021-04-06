package com.example.agung.PPK_UNY_Mobile.sendEmail;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Gmail {

    private String fromEmail;
    private String fromPassword;
    private String toEmailList;
    private String emailSubject;
    private String emailBody;

    @SuppressWarnings("rawtypes")
    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    private Authenticator authenticator;

    public Gmail() {

    }

    Gmail(final String fromEmail, final String fromPassword,
          String toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();

        emailProperties.put("mail.transport.protocol", "smtp");
        emailProperties.put("mail.smtp.host", "smtp.example.com"); // smtp.gmail.com?
        emailProperties.put("mail.smtp.port", "25");
        // gmail's smtp port
        String emailPort = "587";
        emailProperties.put("mail.smtp.port", emailPort);
        String smtpAuth = "true";
        emailProperties.put("mail.smtp.auth", smtpAuth);
        String starttls = "true";
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");

       authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        };

    }

    void createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, authenticator);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
    //    for (String toEmail : toEmailList) {
            Log.i("GMail","toEmail: "+toEmailList);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmailList));
      //  }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
//         emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
    }

    void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        String emailHost = "smtp.gmail.com";
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail","allrecipients: "+ Arrays.toString(emailMessage.getAllRecipients()));
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }

}