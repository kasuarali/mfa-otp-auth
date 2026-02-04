package com.example.mfa.otp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@Entity
@Table(name = "otp_challenge")
public class OtpChallenge {

  @Id
  private String id; // UUID

  @Column(nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OtpChannel channel;

  @Column(nullable = false)
  private String otpHash;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private int attempts;

  private Instant consumedAt;

  /*public String getId() { return id; }
  public Long getUserId() { return userId; }
  public OtpChannel getChannel() { return channel; }
  public String getOtpHash() { return otpHash; }
  public Instant getExpiresAt() { return expiresAt; }
  public int getAttempts() { return attempts; }
  public Instant getConsumedAt() { return consumedAt; }

  public void setId(String id) { this.id = id; }
  public void setUserId(Long userId) { this.userId = userId; }
  public void setChannel(OtpChannel channel) { this.channel = channel; }
  public void setOtpHash(String otpHash) { this.otpHash = otpHash; }
  public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
  public void setAttempts(int attempts) { this.attempts = attempts; }
  public void setConsumedAt(Instant consumedAt) { this.consumedAt = consumedAt; }*/
}
