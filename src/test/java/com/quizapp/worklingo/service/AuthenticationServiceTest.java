package com.quizapp.worklingo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizapp.worklingo.config.JwtService;
import com.quizapp.worklingo.dto.request.AuthenticationRequest;
import com.quizapp.worklingo.dto.AuthenticationResponse;
import com.quizapp.worklingo.dto.request.RegisterRequest;
import com.quizapp.worklingo.model.OTP;
import com.quizapp.worklingo.model.token.Token;
import com.quizapp.worklingo.model.token.TokenRepository;
import com.quizapp.worklingo.model.token.TokenType;
import com.quizapp.worklingo.model.user.Role;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.OtpRepository;
import com.quizapp.worklingo.repository.UserRepository;
import com.quizapp.worklingo.utils.EmailUtil;
import com.quizapp.worklingo.utils.OtpUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private OtpUtil otpUtil;

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        Integer id = 1;
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        String password = "encodedPassword";
        String jobTitle = "USER";
        String avatarUrl = "avatarUrl";
        String bio = "bio";
        Role role = Role.USER;
        RegisterRequest registerRequest = new RegisterRequest(firstname, lastname, email, password, role);
        User user = User.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .role(role)
                .tokens(null)
                .build();
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(user.toDTO(), response.getUser());

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testAuthenticate() {
        Integer id = 1;
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        String password = "encodedPassword";
        String jobTitle = "USER";
        String avatarUrl = "avatarUrl";
        String bio = "bio";
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "password");
        User user = User.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .jobTitle(jobTitle)
                .avatarUrl(avatarUrl)
                .bio(bio)
                .build();
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(user.toDTO(), response.getUser());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testForgotPassword() {
        Integer id = 1;
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        String password = "encodedPassword";
        String jobTitle = "USER";
        String avatarUrl = "avatarUrl";
        String bio = "bio";
        User user = User.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .jobTitle(jobTitle)
                .avatarUrl(avatarUrl)
                .bio(bio)
                .build();
        String otp = "123456";

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(otpUtil.generateOtp()).thenReturn(otp);
        doNothing().when(emailUtil).sendForgotPasswordEmail(anyString(), anyString());

        String result = authenticationService.forgotPassword(email);

        assertEquals("Email sent successfully", result);

        ArgumentCaptor<OTP> otpCaptor = ArgumentCaptor.forClass(OTP.class);
        verify(otpRepository).save(otpCaptor.capture());
        OTP savedOtp = otpCaptor.getValue();

        assertNotNull(savedOtp);
        assertEquals(user, savedOtp.getUser());
        assertEquals(otp, savedOtp.getOtp());
        assertTrue(savedOtp.getExpiryTime().isAfter(LocalDateTime.now()));

        verify(emailUtil, times(1)).sendForgotPasswordEmail(anyString(), anyString());
    }

    @Test
    void testVerifyOtp() {
        Integer id = 1;
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        String password = "encodedPassword";
        String jobTitle = "USER";
        String avatarUrl = "avatarUrl";
        String bio = "bio";
        String otp = "123456";
        LocalDateTime currentTime = LocalDateTime.now();
        User user = User.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .jobTitle(jobTitle)
                .avatarUrl(avatarUrl)
                .bio(bio)
                .build();
        OTP otpEntity = new OTP(1, user, otp, currentTime.plusMinutes(1));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(otpRepository.findByUserId(anyInt())).thenReturn(otpEntity);

        Boolean result = authenticationService.verifyOtp(email, otp, currentTime);

        assertTrue(result);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(otpRepository, times(1)).findByUserId(anyInt());
    }
}

