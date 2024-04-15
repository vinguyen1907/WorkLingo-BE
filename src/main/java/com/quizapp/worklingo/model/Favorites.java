package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.FavoritesDTO;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorites {
    @EmbeddedId
    private FavoritesId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("lessonId")
    private Lesson lesson;

    public FavoritesDTO toDTO() {
        return new FavoritesDTO(
            this.user.getId(),
            this.lesson.getId()
        );
    }
}
