package com.notification;

import com.notification.core.EmailMessage;
import com.notification.core.EmailProvider;
import com.notification.core.NotificationException;
import com.notification.core.SendResult;
import com.notification.core.SmsMessage;
import com.notification.core.SmsProvider;
import com.notification.factory.NotificationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

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
        logger.info("SMS saatmine alustatud. Saaja: {}", to);
        SendResult result = smsSender.send(new SmsMessage(to, body));
        if (result.isSuccess()) {
            logger.info("SMS saatmine õnnestus. Saaja: {}", to);
        } else {
            logger.error("SMS saatmine ebaõnnestus. Saaja: {}. Viga: {}", to, result.getErrorMessage());
        }
        return result;
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
        logger.info("E-posti saatmine alustatud. Saaja: {}", to);
        SendResult result = emailSender.send(new EmailMessage(to, subject, body));
        if (result.isSuccess()) {
            logger.info("E-posti saatmine õnnestus. Saaja: {}", to);
        } else {
            logger.error("E-posti saatmine ebaõnnestus. Saaja: {}. Viga: {}", to, result.getErrorMessage());
        }
        return result;
    }
}
