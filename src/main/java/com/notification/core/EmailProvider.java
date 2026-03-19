package com.notification.core;

public interface EmailProvider {
    SendResult send(EmailMessage message);
}
