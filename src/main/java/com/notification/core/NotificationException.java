package com.notification.core;

public class NotificationException extends RuntimeException {

    private final String provider;

    public NotificationException(String provider, String message, Throwable cause) {
        super(message, cause);
        this.provider = provider;
    }

    public NotificationException(String provider, String message) {
        super(message);
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}
