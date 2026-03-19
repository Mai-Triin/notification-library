# notification-library

A Java library for sending SMS and email notifications through a unified interface. The library decouples application logic from specific providers — your application does not need to know whether Twilio, SendGrid, or Amazon SES is being used under the hood.

## Prerequisites

- Java 21
- Maven 3.6+
- A Twilio account (for SMS)
- A SendGrid or Amazon SES account (for email)

## Installation

### 1. Clone and install to local Maven repository

```bash
git clone <repo-url>
cd notification-library
mvn install
```

### 2. Add the dependency to your project's `pom.xml`

```xml
<dependency>
    <groupId>com.notification</groupId>
    <artifactId>notification-library</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Configuration

Create a file at `src/main/resources/notification.properties`:

```properties
# SMS provider: twilio
notification.provider.sms=twilio

# Email provider: sendgrid or ses
notification.provider.email=sendgrid

# Twilio settings
twilio.account.sid=YOUR_ACCOUNT_SID
twilio.auth.token=YOUR_AUTH_TOKEN
twilio.phone.number=YOUR_TWILIO_NUMBER

# SendGrid settings
sendgrid.api.key=YOUR_SENDGRID_API_KEY
sendgrid.from.email=YOUR_FROM_EMAIL

# Amazon SES settings (if using SES)
ses.from.email=YOUR_FROM_EMAIL
ses.region=eu-west-1
```

> **Note:** Do not commit `notification.properties` to version control. Add it to `.gitignore`.

## Usage

```java
NotificationService service = new NotificationService();

// Send SMS
SendResult smsResult = service.sendSms("+37255555555", "Hello!");
if (!smsResult.isSuccess()) {
    System.out.println("Error: " + smsResult.getErrorMessage());
}

// Send email
SendResult emailResult = service.sendEmail(
    "recipient@example.com",
    "Subject",
    "Email body"
);
if (!emailResult.isSuccess()) {
    System.out.println("Error: " + emailResult.getErrorMessage());
}
```

### SendResult

Both methods return a `SendResult` object:

| Method | Description |
|---|---|
| `isSuccess()` | `true` if the message was sent successfully |
| `getProvider()` | Provider name (e.g. `"Twilio"`) |
| `getErrorMessage()` | Error message if sending failed |

### Exceptions

If the input is invalid (blank or null), a `NotificationException` is thrown:

```java
try {
    service.sendSms("", "Hello");
} catch (NotificationException e) {
    System.out.println(e.getProvider()); // "SMS"
    System.out.println(e.getMessage());  // "Saaja telefoninumber ei tohi olla tühi"
}
```

## Supported providers

| Channel | Provider | Configuration key |
|---|---|---|
| SMS | Twilio | `notification.provider.sms=twilio` |
| Email | SendGrid | `notification.provider.email=sendgrid` |
| Email | Amazon SES | `notification.provider.email=ses` |

## Project structure

```
src/main/java/com/notification/
├── NotificationService.java       # Main entry point
├── adapter/
│   ├── TwilioAdapter.java         # Twilio SMS implementation
│   ├── SendGridAdapter.java       # SendGrid email implementation
│   └── AmazonSesAdapter.java      # Amazon SES email implementation
├── core/
│   ├── SmsProvider.java           # SMS interface
│   ├── EmailProvider.java         # Email interface
│   ├── SmsMessage.java            # SMS message model
│   ├── EmailMessage.java          # Email message model
│   ├── SendResult.java            # Send result
│   └── NotificationException.java # Input validation exception
└── factory/
    └── NotificationFactory.java   # Creates the correct adapter based on configuration
```

## Running tests

```bash
mvn test
```
