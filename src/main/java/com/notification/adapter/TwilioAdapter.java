package com.notification.adapter;

import com.notification.core.NotificationMessage;
import com.notification.core.NotificationSender;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioAdapter implements NotificationSender {

    private final String accountSid;
    private final String authToken;
    private final String fromNumber;

    public TwilioAdapter(String accountSid, String authToken, String fromNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
    }

    @Override
    public void send(NotificationMessage message) {
        Twilio.init(accountSid, authToken);
        try {
            Message.creator(
                new PhoneNumber(message.getTo()),
                new PhoneNumber(fromNumber),
                message.getBody()
            ).create();
        } catch (TwilioException e) {
            throw new RuntimeException("Twilio saatmine ebaõnnestus", e);
        }
    }
}
