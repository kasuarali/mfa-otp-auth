package com.example.mfa.security;

import com.example.mfa.user.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

  private final SecretKey key;
  private final int accessMinutes;
  private final UserRepo userRepo;
  public JwtService(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.accessTokenMinutes}") int accessMinutes, UserRepo userRepo) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.accessMinutes = accessMinutes;
      this.userRepo = userRepo;
  }

  public String issueAccessToken(Long userId, String username, String role) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(accessMinutes * 60L);

    return Jwts.builder()
      .setSubject(username)
      .claim("uid", userId)
       .claim("role", role)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key)
      .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
      .parseClaimsJws(token).getBody().getSubject();
  }
  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}
