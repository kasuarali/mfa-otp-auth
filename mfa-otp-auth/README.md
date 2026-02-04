# MFA OTP Auth (Spring Boot 3)

This is a minimal assignment-style backend that supports:
- Username/password login
- OTP via Email or SMS
- JWT token issued only after OTP verification

## Requirements
- Java 17
- Maven
- IntelliJ IDEA (Import as Maven project)

## Run
1) Update `src/main/resources/application.yml`:
   - `app.jwt.secret` (set a long random string)
   - SMTP settings under `spring.mail.*` for email OTP
   - SMS: keep `app.sms.provider=CONSOLE` (prints OTP in console) OR set to `TWILIO` and add credentials

2) Start:
   `mvn spring-boot:run`

## Test (curl)
### Login
EMAIL:
curl -X POST "http://localhost:8080/auth/login?channel=EMAIL" -H "Content-Type: application/json" -d '{"username":"alice","password":"Password123!"}'

SMS:
curl -X POST "http://localhost:8080/auth/login?channel=SMS" -H "Content-Type: application/json" -d '{"username":"alice","password":"Password123!"}'

### Verify OTP
curl -X POST "http://localhost:8080/auth/otp/verify" -H "Content-Type: application/json" -d '{"challengeId":"<id>","code":"123456"}'

### Protected endpoint
curl "http://localhost:8080/me" -H "Authorization: Bearer <token>"

## Seeded user
- username: kausar
- password: Password123!
Change in `DataSeeder.java` if needed.
