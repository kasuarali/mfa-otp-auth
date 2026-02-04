package com.example.mfa.security;

import com.example.mfa.user.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Configuration
public class UserDetailsServiceConfig {

  @Bean
  public UserDetailsService userDetailsService(UserRepo userRepo) {
    return username -> userRepo.findByUsername(username)
      .map(u -> new org.springframework.security.core.userdetails.User(
        u.getUsername(),
        u.getPasswordHash(),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))
      ))
      .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
  }
}
