package com.example.mfa.notify;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationSenderImpl implements NotificationSender {

  private final JavaMailSender mailSender;

  private final String smsProvider; // TWILIO or CONSOLE
  private final String sid;
  private final String token;
  private final String from;

  public NotificationSenderImpl(JavaMailSender mailSender,
                                @Value("${app.sms.provider}") String smsProvider,
                                @Value("${app.sms.twilio.accountSid}") String sid,
                                @Value("${app.sms.twilio.authToken}") String token,
                                @Value("${app.sms.twilio.fromNumber}") String from) {
    this.mailSender = mailSender;
    this.smsProvider = smsProvider;
    this.sid = sid;
    this.token = token;
    this.from = from;
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText(body);
    mailSender.send(msg);
  }

  @Override
  public void sendSms(String to, String body) {
    if ("TWILIO".equalsIgnoreCase(smsProvider)) {
      Twilio.init(sid, token);
      Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
    } else {
      System.out.println("[SMS CONSOLE] to=" + to + " body=" + body);
    }
  }
}
