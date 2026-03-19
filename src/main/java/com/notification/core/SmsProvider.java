package com.notification.core;

public interface SmsProvider {
    SendResult send(SmsMessage message);
}
