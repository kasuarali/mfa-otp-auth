package com.example.mfa.auth;

import com.example.mfa.api.dto.*;
import com.example.mfa.notify.NotificationSender;
import com.example.mfa.otp.*;
import com.example.mfa.security.JwtService;
import com.example.mfa.user.AppUser;
import com.example.mfa.user.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

  private final AuthenticationManager authManager;
  private final UserRepo userRepo;
  private final OtpChallengeRepo challengeRepo;
  private final NotificationSender notifier;
  private final JwtService jwtService;

  private final int otpTtlSeconds;
  private final int maxAttempts;

  public AuthService(AuthenticationManager authManager,
                     UserRepo userRepo,
                     OtpChallengeRepo challengeRepo,
                     NotificationSender notifier,
                     JwtService jwtService,
                     @Value("${app.otp.ttlSeconds}") int otpTtlSeconds,
                     @Value("${app.otp.maxAttempts}") int maxAttempts) {
    this.authManager = authManager;
    this.userRepo = userRepo;
    this.challengeRepo = challengeRepo;
    this.notifier = notifier;
    this.jwtService = jwtService;
    this.otpTtlSeconds = otpTtlSeconds;
    this.maxAttempts = maxAttempts;
  }

  public LoginResponse login(LoginRequest req, OtpChannel channel) {
    Authentication auth = authManager.authenticate(
      new UsernamePasswordAuthenticationToken(req.username(), req.password())
    );

    AppUser user = userRepo.findByUsername(req.username()).orElseThrow();

    if (!user.isMfaEnabled()) {
      String token = jwtService.issueAccessToken(user.getId(), user.getUsername(), user.getRole());
      return new LoginResponse(false, null, null, 0, 0, token, "Bearer");
    }

    String otp = OtpUtil.generate6Digit();
    String hash = OtpUtil.hash(otp);

    OtpChallenge c = new OtpChallenge();
    c.setId(UUID.randomUUID().toString());
    c.setUserId(user.getId());
    c.setChannel(channel);
    c.setOtpHash(hash);
    c.setAttempts(0);
    c.setExpiresAt(Instant.now().plusSeconds(otpTtlSeconds));
    challengeRepo.save(c);

    String msg = "Your OTP code is: " + otp + " (expires in " + otpTtlSeconds + " seconds)";

    if (channel == OtpChannel.EMAIL) {
      notifier.sendEmail(user.getEmail(), "Your OTP Code", msg);
    } else {
      notifier.sendSms(user.getPhone(), msg);
    }

    return new LoginResponse(true, c.getId(), channel, 6, otpTtlSeconds, null, null);
  }

  public TokenResponse verify(OtpVerifyRequest req) {
    OtpChallenge c = challengeRepo.findById(req.challengeId())
      .orElseThrow(() -> new RuntimeException("Invalid challenge"));

    if (c.getConsumedAt() != null) throw new RuntimeException("Challenge already used");
    if (Instant.now().isAfter(c.getExpiresAt())) throw new RuntimeException("Challenge expired");
    if (c.getAttempts() >= maxAttempts) throw new RuntimeException("Too many attempts");

    c.setAttempts(c.getAttempts() + 1);

    boolean ok = OtpUtil.verify(req.code(), c.getOtpHash());
    if (!ok) {
      challengeRepo.save(c);
      throw new RuntimeException("Invalid code");
    }

    c.setConsumedAt(Instant.now());
    challengeRepo.save(c);

    AppUser user = userRepo.findById(c.getUserId()).orElseThrow();
    String token = jwtService.issueAccessToken(user.getId(), user.getUsername(),user.getRole());

    return new TokenResponse(token, "Bearer");
  }
}
