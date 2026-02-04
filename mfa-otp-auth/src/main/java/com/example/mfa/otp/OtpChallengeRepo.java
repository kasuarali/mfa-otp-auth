package com.example.mfa.otp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpChallengeRepo extends JpaRepository<OtpChallenge, String> { }
