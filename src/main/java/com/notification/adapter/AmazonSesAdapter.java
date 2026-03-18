package com.notification.adapter;

import com.notification.core.NotificationException;
import com.notification.core.NotificationMessage;
import com.notification.core.NotificationSender;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

public class AmazonSesAdapter implements NotificationSender {

    private final String fromEmail;
    private final Region region;

    public AmazonSesAdapter(String fromEmail, Region region) {
        this.fromEmail = fromEmail;
        this.region = region;
    }

    @Override
    public void send(NotificationMessage message) {
        SesClient client = SesClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder()
                        .toAddresses(message.getTo())
                        .build())
                .message(software.amazon.awssdk.services.ses.model.Message.builder()
                        .subject(Content.builder().data(message.getSubject()).build())
                        .body(Body.builder()
                                .text(Content.builder().data(message.getBody()).build())
                                .build())
                        .build())
                .source(fromEmail)
                .build();

        try {
            client.sendEmail(request);
        } catch (SesException e) {
            throw new NotificationException("AmazonSES", "E-posti saatmine ebaõnnestus", e);
        }
    }
}
