package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.request.AuthenticationRequest;
import com.quizapp.worklingo.dto.AuthenticationResponse;
import com.quizapp.worklingo.dto.request.RegisterRequest;
import com.quizapp.worklingo.service.interfaces.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "This method is used to register a new user.")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "This method is used to authenticate a user.")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "This method is used to refresh token.")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "This method is used to send an email to reset password.")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "This method is used to verify otp.")
    public ResponseEntity<Boolean> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam LocalDateTime time
    ) {
        return ResponseEntity.ok(authenticationService.verifyOtp(email, otp, time));
    }
}
