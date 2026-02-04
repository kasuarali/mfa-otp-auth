package com.example.mfa.notify;

public interface NotificationSender {
  void sendEmail(String to, String subject, String body);
  void sendSms(String to, String body);
}
