package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentLessonDTO {
    private Integer id;

    private Integer userId;

    private LessonDTO lesson;

    private LocalDateTime lastAccessed;

    public RecentLessonDTO(RecentLesson recentLesson) {
        this.id = recentLesson.getId();
        this.userId = recentLesson.getUser().getId();
        this.lesson = recentLesson.getLesson().toDTO();
        this.lastAccessed = recentLesson.getLastAccessed();
    }
}
