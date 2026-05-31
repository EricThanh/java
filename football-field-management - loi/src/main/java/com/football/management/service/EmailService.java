package com.football.management.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String from;
    private final boolean auth;
    private final boolean starttls;

    public EmailService() {
        Properties appProps = new Properties();

        try (InputStream input = EmailService.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("Không tìm thấy file application.properties");
            }

            appProps.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được cấu hình email", e);
        }

        this.host = appProps.getProperty("mail.host");
        this.port = appProps.getProperty("mail.port");
        this.username = appProps.getProperty("mail.username");
        this.password = appProps.getProperty("mail.password");
        this.from = appProps.getProperty("mail.from");
        this.auth = Boolean.parseBoolean(appProps.getProperty("mail.auth", "true"));
        this.starttls = Boolean.parseBoolean(appProps.getProperty("mail.starttls", "true"));
    }

    public void guiEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", String.valueOf(auth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttls));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }
}