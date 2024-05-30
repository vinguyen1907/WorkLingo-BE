package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.UserDTO;
import com.quizapp.worklingo.model.user.ChangePasswordRequest;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.UserRepository;
import com.quizapp.worklingo.service.interfaces.ICloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ICloudinaryService cloudinaryService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        Integer userId = 1;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("newPassword");

        User user = new User();
        user.setPassword("oldPassword");

        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.matches("oldPassword", "oldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        assertDoesNotThrow(() -> userService.changePassword(request, principal));
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void testUpdateProfile() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        UserDTO result = userService.updateProfile(userId, "John", "Doe", "Engineer", "Bio");

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("Engineer", user.getJobTitle());
        assertEquals("Bio", user.getBio());
    }

    @Test
    void testChangeAvatarImage() {
        Integer userId = 1;
        MultipartFile avatarFile = mock(MultipartFile.class);
        String imageUrl = "avatarImageUrl";
        User user = new User();

        when(cloudinaryService.uploadUserAvatar(userId, avatarFile)).thenReturn(Collections.singletonMap("url", imageUrl));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.changeAvatarImage(userId, avatarFile);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        assertEquals(imageUrl, user.getAvatarUrl());
    }
}
