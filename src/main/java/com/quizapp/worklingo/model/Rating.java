package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.RatingDTO;
import com.quizapp.worklingo.enums.RatingType;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @EmbeddedId
    private RatingId id;

    @Enumerated(EnumType.STRING)
    private RatingType ratingType;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("lessonId")
    private Lesson lesson;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @PreUpdate
    public void preUpdate() {
        updatedTime = LocalDateTime.now();
    }

    public Rating(RatingId ratingId, RatingType ratingType) {
        this.id = ratingId;
        this.ratingType = ratingType;
    }

    public RatingDTO toDTO() {
        return new RatingDTO(this);
    }
}
