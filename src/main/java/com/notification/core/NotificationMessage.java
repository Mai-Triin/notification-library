package com.notification.core;

public class NotificationMessage {
    private String to;
    private String subject;
    private String body;

    public NotificationMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public NotificationMessage(String to, String body) {
        this.to = to;
        this.subject = null;
        this.body = body;
    }

    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
}
