package com.notification.core;

public class SendResult {

    private final boolean success;
    private final String provider;
    private final String errorMessage;

    private SendResult(boolean success, String provider, String errorMessage) {
        this.success = success;
        this.provider = provider;
        this.errorMessage = errorMessage;
    }

    public static SendResult success(String provider) {
        return new SendResult(true, provider, null);
    }

    public static SendResult failure(String provider, String errorMessage) {
        return new SendResult(false, provider, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getProvider() {
        return provider;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
