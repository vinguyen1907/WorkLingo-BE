package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.UserDTO;
import com.quizapp.worklingo.model.user.ChangePasswordRequest;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface IUserService {
    UserDTO getUserById(Integer userId);
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    UserDTO updateProfile(Integer userId, String firstName, String lastName, String jobTitle, String bio);
    UserDTO changeAvatarImage(Integer userId, MultipartFile avatarFle);
}
