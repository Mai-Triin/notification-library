package com.notification;

import com.notification.core.EmailMessage;
import com.notification.core.EmailProvider;
import com.notification.core.NotificationException;
import com.notification.core.SendResult;
import com.notification.core.SmsMessage;
import com.notification.core.SmsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private SmsProvider smsSender;
    private EmailProvider emailSender;
    private NotificationService service;

    @BeforeEach
    void setUp() {
        smsSender = mock(SmsProvider.class);
        emailSender = mock(EmailProvider.class);
        when(smsSender.send(any())).thenReturn(SendResult.success("Twilio"));
        when(emailSender.send(any())).thenReturn(SendResult.success("SendGrid"));
        service = new NotificationService(smsSender, emailSender);
    }

    @Test
    void sendSms_emptyRecipient_throwsException() {
        assertThrows(NotificationException.class, () -> service.sendSms("", "Hello"));
    }

    @Test
    void sendSms_nullRecipient_throwsException() {
        assertThrows(NotificationException.class, () -> service.sendSms(null, "Hello"));
    }

    @Test
    void sendSms_emptyBody_throwsException() {
        assertThrows(NotificationException.class, () -> service.sendSms("+37255555555", ""));
    }

    @Test
    void sendSms_validInput_callsSend() {
        service.sendSms("+37255555555", "Hello world");
        ArgumentCaptor<SmsMessage> captor = ArgumentCaptor.forClass(SmsMessage.class);
        verify(smsSender).send(captor.capture());
        assertEquals("+37255555555", captor.getValue().getTo());
        assertEquals("Hello world", captor.getValue().getBody());
    }

    @Test
    void sendEmail_emptyRecipient_throwsException() {
        assertThrows(NotificationException.class, () -> service.sendEmail("", "Subject", "Body"));
    }

    @Test
    void sendEmail_emptySubject_throwsException() {
        assertThrows(NotificationException.class, () -> service.sendEmail("test@test.com", "", "Body"));
    }

    @Test
    void sendEmail_emptyBody_throwsException() {
        assertThrows(NotificationException.class, () -> service.sendEmail("test@test.com", "Subject", ""));
    }

    @Test
    void sendEmail_validInput_callsSend() {
        service.sendEmail("test@test.com", "Subject", "Body");
        ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);
        verify(emailSender).send(captor.capture());
        assertEquals("test@test.com", captor.getValue().getTo());
        assertEquals("Subject", captor.getValue().getSubject());
        assertEquals("Body", captor.getValue().getBody());
    }
}
