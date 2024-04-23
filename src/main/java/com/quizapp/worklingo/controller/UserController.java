package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.FavoritesDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.UserDTO;
import com.quizapp.worklingo.dto.request.UpdateUserProfileRequest;
import com.quizapp.worklingo.model.user.ChangePasswordRequest;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.service.UserService;
import com.quizapp.worklingo.service.interfaces.IFavoritesService;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import com.quizapp.worklingo.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final IUserService userService;
    private final IFavoritesService favoritesService;
    private final ILessonService lessonService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get information of an user.")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping
    @Operation(summary = "This method is used to change password of an user.")
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Update information of an user.")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer userId,
            @RequestBody UpdateUserProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(userId, request.getFirstName(), request.getLastName(), request.getJobTitle(), request.getBio()));
    }

    @PutMapping("/{userId}/avatar")
    @Operation(summary = "Change avatar image of an user.")
    public ResponseEntity<UserDTO> changeAvatarImage(
            @PathVariable Integer userId,
            @RequestParam MultipartFile avatarFile
    ) {
        return ResponseEntity.ok(userService.changeAvatarImage(userId, avatarFile));
    }

    @GetMapping("/{userId}/favorites")
    @Operation(summary = "This method is used to get favorite lessons of an user.")
    public ResponseEntity<PageDTO<LessonDTO>> getFavoriteLessons(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(favoritesService.getFavoriteLessons(userId, page, size));
    }

    @PostMapping("/{userId}/favorites")
    @Operation(summary = "This method is used to add a lesson to favorite list of an user.")
    public ResponseEntity<FavoritesDTO> addFavoriteLesson(
            @PathVariable Integer userId,
            @RequestParam Integer lessonId
    ) {
        return ResponseEntity.ok(favoritesService.addFavoriteLesson(userId, lessonId));
    }

    @DeleteMapping("/{userId}/favorites")
    @Operation(summary = "Remove a lesson from favorite list of an user.")
    public ResponseEntity<?> removeFavoriteLesson(
            @PathVariable Integer userId,
            @RequestParam Integer lessonId
    ) {
        favoritesService.removeFavoriteLesson(userId, lessonId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/lessons")
    @Operation(summary = "Get all own lessons of an user (order by updated time).")
    public ResponseEntity<PageDTO<LessonDTO>> getOwnLessons(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(lessonService.getOwnLessons(userId, page, size));
    }
}
