package com.notification;

import com.notification.core.NotificationException;
import com.notification.core.NotificationMessage;
import com.notification.core.NotificationSender;
import com.notification.factory.NotificationFactory;

public class NotificationService {

    private final NotificationSender smsSender;
    private final NotificationSender emailSender;

    public NotificationService() {
        NotificationFactory factory = new NotificationFactory();
        this.smsSender = factory.createSmsSender();
        this.emailSender = factory.createEmailSender();
    }

    public NotificationService(NotificationSender smsSender, NotificationSender emailSender) {
        this.smsSender = smsSender;
        this.emailSender = emailSender;
    }

    public void sendSms(String to, String body) {
        if (to == null || to.isBlank()) {
            throw new NotificationException("SMS", "Saaja telefoninumber ei tohi olla tühi");
        }
        if (body == null || body.isBlank()) {
            throw new NotificationException("SMS", "Sõnumi sisu ei tohi olla tühi");
        }
        smsSender.send(new NotificationMessage(to, body));
    }

    public void sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            throw new NotificationException("Email", "Saaja e-posti aadress ei tohi olla tühi");
        }
        if (subject == null || subject.isBlank()) {
            throw new NotificationException("Email", "E-posti teema ei tohi olla tühi");
        }
        if (body == null || body.isBlank()) {
            throw new NotificationException("Email", "E-posti sisu ei tohi olla tühi");
        }
        emailSender.send(new NotificationMessage(to, subject, body));
    }
}
