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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final IFavoritesService favoritesService;

    @PatchMapping
    @Operation(summary = "This method is used to change password of an user.")
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
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
}
