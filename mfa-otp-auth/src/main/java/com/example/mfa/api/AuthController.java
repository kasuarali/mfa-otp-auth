package com.example.mfa.api;

import com.example.mfa.api.dto.*;
import com.example.mfa.auth.AuthService;
import com.example.mfa.otp.OtpChannel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req,
                                             @RequestParam(defaultValue = "EMAIL") OtpChannel channel) {
    return ResponseEntity.ok(authService.login(req, channel));
  }

  @PostMapping("/otp/verify")
  public ResponseEntity<TokenResponse> verify(@Valid @RequestBody OtpVerifyRequest req) {
    return ResponseEntity.ok(authService.verify(req));
  }
}
