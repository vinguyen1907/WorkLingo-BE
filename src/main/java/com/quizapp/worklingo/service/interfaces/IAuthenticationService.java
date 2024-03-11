package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.AuthenticationRequest;
import com.quizapp.worklingo.dto.AuthenticationResponse;
import com.quizapp.worklingo.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
//    void saveUserToken(User user, String jwtToken);
//    void revokeAllUserTokens(User user);
    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
    String forgotPassword(String email);
    Boolean verifyOtp(String email, String otp, LocalDateTime time);
}
