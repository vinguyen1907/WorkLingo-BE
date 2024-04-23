package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.UserDTO;
import com.quizapp.worklingo.model.user.ChangePasswordRequest;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.service.interfaces.ICloudinaryService;
import com.quizapp.worklingo.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.quizapp.worklingo.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ICloudinaryService cloudinaryService;

    @Override
    public UserDTO getUserById(Integer userId) {
        return userRepository.findById(userId).map(UserDTO::new).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<UserDTO> getAllUser() {
        return userRepository.findAll().stream().map(UserDTO::new).toList();
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    @Override
    public UserDTO updateProfile(Integer userId, String firstName, String lastName, String jobTitle, String bio) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (firstName != null) {
            user.setFirstname(firstName);
        }
        if (lastName != null) {
            user.setLastname(lastName);
        }
        if (jobTitle != null) {
            user.setJobTitle(jobTitle);
        }
        if (bio != null) {
            user.setBio(bio);
        }
        return new UserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO changeAvatarImage(Integer userId, MultipartFile avatarFile) {
        String url = cloudinaryService.uploadUserAvatar(userId, avatarFile).get("url").toString();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setAvatarUrl(url);
        return new UserDTO(userRepository.save(user));
    }
}
