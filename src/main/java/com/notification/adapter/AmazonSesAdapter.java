package com.notification.adapter;

import com.notification.core.EmailMessage;
import com.notification.core.EmailProvider;
import com.notification.core.SendResult;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

public class AmazonSesAdapter implements EmailProvider {

    private final String fromEmail;
    private final Region region;

    public AmazonSesAdapter(String fromEmail, Region region) {
        this.fromEmail = fromEmail;
        this.region = region;
    }

    @Override
    public SendResult send(EmailMessage message) {
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
            return SendResult.success("AmazonSES");
        } catch (SesException e) {
            return SendResult.failure("AmazonSES", "E-posti saatmine ebaõnnestus: " + e.getMessage());
        }
    }
}
