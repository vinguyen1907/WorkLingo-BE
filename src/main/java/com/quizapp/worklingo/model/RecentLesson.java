package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentLesson {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Lesson lesson;

    private LocalDateTime lastAccessed;

    public RecentLessonDTO toDTO() {
        return new RecentLessonDTO(this);
    }
}
