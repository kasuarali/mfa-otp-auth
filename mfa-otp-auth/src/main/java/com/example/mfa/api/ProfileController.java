package com.example.mfa.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

  @GetMapping("/me")
  public Object me(Authentication auth) {
    return new Object() {
      public final String username = auth.getName();
      public final String message = "You are fully authenticated (password + OTP).";
    };
  }
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/view-user")
  public String viewUser() {
    return "Only ADMIN can see this";
  }
}
