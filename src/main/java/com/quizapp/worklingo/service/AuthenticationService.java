package com.quizapp.worklingo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizapp.worklingo.config.JwtService;
import com.quizapp.worklingo.dto.AuthenticationRequest;
import com.quizapp.worklingo.dto.AuthenticationResponse;
import com.quizapp.worklingo.dto.RegisterRequest;
import com.quizapp.worklingo.model.OTP;
import com.quizapp.worklingo.model.token.Token;
import com.quizapp.worklingo.model.token.TokenRepository;
import com.quizapp.worklingo.model.token.TokenType;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.OtpRepository;
import com.quizapp.worklingo.repository.UserRepository;
import com.quizapp.worklingo.service.interfaces.IAuthenticationService;
import com.quizapp.worklingo.utils.EmailUtil;
import com.quizapp.worklingo.utils.OtpUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailUtil emailUtil;
  private final OtpUtil otpUtil;
  private final OtpRepository otpRepository;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public String forgotPassword(String email) {
    User user = repository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    String otp = otpUtil.generateOtp();

    try {
      emailUtil.sendForgotPasswordEmail(email, otp);
    } catch (MailException e) {
      throw new RuntimeException("Unable to send email");
    }

    otpRepository.save(
            new OTP(
                    null,
//                    user.getId(),
                    111,
                    otp,
                    LocalDateTime.now().plusMinutes(1)
            )
    );

    return "Email sent successfully";
  }

  public Boolean verifyOtp(String email, String otp, LocalDateTime time) {
    User user = repository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    OTP otpEntity = otpRepository.findByUserId(user.getId());
    if (otpEntity.getExpiryTime().isBefore(time)) {
      throw new RuntimeException("OTP expired");
    }

    return otpEntity.getOtp().equals(otp);
  }
}
