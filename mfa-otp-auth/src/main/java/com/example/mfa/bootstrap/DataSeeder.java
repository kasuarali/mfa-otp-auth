package com.example.mfa.bootstrap;

import com.example.mfa.user.AppUser;
import com.example.mfa.user.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

  private final UserRepo userRepo;
  private final PasswordEncoder encoder;

  public DataSeeder(UserRepo userRepo, PasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) {
    if (userRepo.findByUsername("kausar").isPresent()) return;

    AppUser u = new AppUser();
    u.setUsername("kausar");
    u.setPasswordHash(encoder.encode("Password123!"));
    u.setEmail("your_email@gmail.com"); // change for real email testing
    u.setPhone("+923001234567");        // change to your phone (E.164)
    u.setMfaEnabled(true);
    u.setRole("ADMIN");
    userRepo.save(u);

    System.out.println("Seeded user: kausar / Password123!");
  }
}
