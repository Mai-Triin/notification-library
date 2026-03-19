package com.notification;

import com.notification.core.EmailMessage;
import com.notification.core.EmailProvider;
import com.notification.core.NotificationException;
import com.notification.core.SendResult;
import com.notification.core.SmsMessage;
import com.notification.core.SmsProvider;
import com.notification.factory.NotificationFactory;

public class NotificationService {

    private final SmsProvider smsSender;
    private final EmailProvider emailSender;

    public NotificationService() {
        NotificationFactory factory = new NotificationFactory();
        this.smsSender = factory.createSmsSender();
        this.emailSender = factory.createEmailSender();
    }

    public NotificationService(SmsProvider smsSender, EmailProvider emailSender) {
        this.smsSender = smsSender;
        this.emailSender = emailSender;
    }

    public SendResult sendSms(String to, String body) {
        if (to == null || to.isBlank()) {
            throw new NotificationException("SMS", "Saaja telefoninumber ei tohi olla tühi");
        }
        if (body == null || body.isBlank()) {
            throw new NotificationException("SMS", "Sõnumi sisu ei tohi olla tühi");
        }
        return smsSender.send(new SmsMessage(to, body));
    }

    public SendResult sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            throw new NotificationException("Email", "Saaja e-posti aadress ei tohi olla tühi");
        }
        if (subject == null || subject.isBlank()) {
            throw new NotificationException("Email", "E-posti teema ei tohi olla tühi");
        }
        if (body == null || body.isBlank()) {
            throw new NotificationException("Email", "E-posti sisu ei tohi olla tühi");
        }
        return emailSender.send(new EmailMessage(to, subject, body));
    }
}
