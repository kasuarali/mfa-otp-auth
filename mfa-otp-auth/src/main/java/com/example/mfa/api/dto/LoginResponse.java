package com.example.mfa.api.dto;

import com.example.mfa.otp.OtpChannel;

public record LoginResponse(
  boolean mfaRequired,
  String challengeId,
  OtpChannel channel,
  int codeLength,
  int expiresInSeconds,
  String accessToken,
  String tokenType
) {}
