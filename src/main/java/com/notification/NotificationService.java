package com.notification;

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

    public void sendSms(String to, String body) {
        smsSender.send(new NotificationMessage(to, body));
    }

    public void sendEmail(String to, String subject, String body) {
        emailSender.send(new NotificationMessage(to, subject, body));
    }
}
