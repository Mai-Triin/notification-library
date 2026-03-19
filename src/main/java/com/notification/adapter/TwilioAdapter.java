package com.notification.adapter;

import com.notification.core.SmsMessage;
import com.notification.core.SmsProvider;
import com.notification.core.SendResult;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioAdapter implements SmsProvider {

    private final String accountSid;
    private final String authToken;
    private final String fromNumber;

    public TwilioAdapter(String accountSid, String authToken, String fromNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
    }

    @Override
    public SendResult send(SmsMessage message) {
        Twilio.init(accountSid, authToken);
        try {
            Message.creator(
                new PhoneNumber(message.getTo()),
                new PhoneNumber(fromNumber),
                message.getBody()
            ).create();
            return SendResult.success("Twilio");
        } catch (TwilioException e) {
            return SendResult.failure("Twilio", "SMS saatmine ebaõnnestus: " + e.getMessage());
        }
    }
}
