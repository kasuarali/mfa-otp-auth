package com.example.mfa.otp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public final class OtpUtil {
  private static final SecureRandom RAND = new SecureRandom();
  private static final BCryptPasswordEncoder ENC = new BCryptPasswordEncoder();

  private OtpUtil() {}

  public static String generate6Digit() {
    int n = 100000 + RAND.nextInt(900000);
    return String.valueOf(n);
  }

  public static String hash(String code) {
    return ENC.encode(code);
  }

  public static boolean verify(String code, String hash) {
    return ENC.matches(code, hash);
  }
}
