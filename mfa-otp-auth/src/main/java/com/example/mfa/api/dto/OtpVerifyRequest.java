package com.example.mfa.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OtpVerifyRequest(
  @NotBlank String challengeId,
  @NotBlank @Pattern(regexp = "\\d{6}") String code
) {}
