package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.model.Favorites;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoritesDTO {
    private Integer userId;
    private Integer lessonId;

    public FavoritesDTO(Favorites favorites) {
        this.userId = favorites.getUser().getId();
        this.lessonId = favorites.getLesson().getId();
    }
}
