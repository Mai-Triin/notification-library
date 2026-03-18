package com.notification.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationExceptionTest {

    @Test
    void exception_storesProvider() {
        NotificationException e = new NotificationException("SendGrid", "Sending failed");
        assertEquals("SendGrid", e.getProvider());
        assertEquals("Sending failed", e.getMessage());
    }

    @Test
    void exception_storesCause() {
        RuntimeException cause = new RuntimeException("API error");
        NotificationException e = new NotificationException("Twilio", "Sending failed", cause);
        assertEquals(cause, e.getCause());
    }
}
