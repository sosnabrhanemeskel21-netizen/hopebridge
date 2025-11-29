package com.humanitarian.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "your-email@gmail.com"; // Configure this
    private static final String SMTP_PASSWORD = "your-app-password"; // Configure this
    private static final String FROM_EMAIL = "your-email@gmail.com";

    public static boolean sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Email sending failed: " + e.getMessage());
            return false;
        }
    }

    public static void sendNotificationEmail(String toEmail, String title, String message) {
        String htmlBody = "<html><body>" +
                "<h2>" + title + "</h2>" +
                "<p>" + message + "</p>" +
                "<hr>" +
                "<p><small>Humanitarian Support Platform - Tigray Crisis Support</small></p>" +
                "</body></html>";
        sendEmail(toEmail, title, htmlBody);
    }
}

