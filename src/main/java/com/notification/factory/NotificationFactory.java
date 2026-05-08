package com.notification.factory;

import com.notification.adapter.AmazonSesAdapter;
import com.notification.adapter.SendGridAdapter;
import com.notification.adapter.TwilioAdapter;
import com.notification.core.EmailProvider;
import com.notification.core.SmsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class NotificationFactory {

    private static final Logger logger = LoggerFactory.getLogger(NotificationFactory.class);

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
            logger.debug("Konfiguratsioon laaditud edukalt");
        } catch (IOException e) {
            throw new RuntimeException("Konfiguratsiooni laadimine ebaõnnestus", e);
        }
        return props;
    }

    public SmsProvider createSmsSender() {
        String provider = properties.getProperty("notification.provider.sms");
        logger.debug("SMS teenusepakkuja valimine: {}", provider);
        if ("twilio".equalsIgnoreCase(provider)) {
            logger.debug("TwilioAdapter loomine");
            return new TwilioAdapter(
                properties.getProperty("twilio.account.sid"),
                properties.getProperty("twilio.auth.token"),
                properties.getProperty("twilio.phone.number")
            );
        }
        throw new RuntimeException("Tundmatu SMS teenusepakkuja: " + provider);
    }

    public EmailProvider createEmailSender() {
        String provider = properties.getProperty("notification.provider.email");
        logger.debug("E-posti teenusepakkuja valimine: {}", provider);
        if ("sendgrid".equalsIgnoreCase(provider)) {
            logger.debug("SendGridAdapter loomine");
            return new SendGridAdapter(
                properties.getProperty("sendgrid.api.key"),
                properties.getProperty("sendgrid.from.email")
            );
        }
        if ("ses".equalsIgnoreCase(provider)) {
            logger.debug("AmazonSesAdapter loomine");
            return new AmazonSesAdapter(
                properties.getProperty("ses.from.email"),
                Region.of(properties.getProperty("ses.region"))
            );
        }
        throw new RuntimeException("Tundmatu e-posti teenusepakkuja: " + provider);
    }
}
