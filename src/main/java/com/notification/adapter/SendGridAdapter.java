package com.notification.adapter;

import com.notification.core.NotificationException;
import com.notification.core.NotificationMessage;
import com.notification.core.NotificationSender;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class SendGridAdapter implements NotificationSender {

    private final String apiKey;
    private final String fromEmail;

    public SendGridAdapter(String apiKey, String fromEmail) {
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
    }

    @Override
    public void send(NotificationMessage message) {
        Email from = new Email(fromEmail);
        Email to = new Email(message.getTo());
        Content content = new Content("text/plain", message.getBody());
        Mail mail = new Mail(from, message.getSubject(), to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new NotificationException("SendGrid", "E-posti saatmine ebaõnnestus, HTTP " + response.getStatusCode());
            }
        } catch (IOException e) {
            throw new NotificationException("SendGrid", "E-posti saatmine ebaõnnestus", e);
        }
    }
}
