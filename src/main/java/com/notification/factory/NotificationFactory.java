package com.notification.factory;

import com.notification.adapter.AmazonSesAdapter;
import com.notification.adapter.SendGridAdapter;
import com.notification.adapter.TwilioAdapter;
import com.notification.core.NotificationSender;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class NotificationFactory {

    private final Properties properties;

    public NotificationFactory() {
        this.properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("notification.properties")) {
            if (input == null) {
                throw new RuntimeException("notification.properties faili ei leitud");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Konfiguratsiooni laadimine ebaõnnestus", e);
        }
        return props;
    }

    public NotificationSender createSmsSender() {
        String provider = properties.getProperty("notification.provider.sms");
        if ("twilio".equalsIgnoreCase(provider)) {
            return new TwilioAdapter(
                properties.getProperty("twilio.account.sid"),
                properties.getProperty("twilio.auth.token"),
                properties.getProperty("twilio.phone.number")
            );
        }
        throw new RuntimeException("Tundmatu SMS teenusepakkuja: " + provider);
    }

    public NotificationSender createEmailSender() {
        String provider = properties.getProperty("notification.provider.email");
        if ("sendgrid".equalsIgnoreCase(provider)) {
            return new SendGridAdapter(
                properties.getProperty("sendgrid.api.key"),
                properties.getProperty("sendgrid.from.email")
            );
        }
        if ("ses".equalsIgnoreCase(provider)) {
            return new AmazonSesAdapter(
                properties.getProperty("ses.from.email"),
                Region.of(properties.getProperty("ses.region"))
            );
        }
        throw new RuntimeException("Tundmatu e-posti teenusepakkuja: " + provider);
    }
}
