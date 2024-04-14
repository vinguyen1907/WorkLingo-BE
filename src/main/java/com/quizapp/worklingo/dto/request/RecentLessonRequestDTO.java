package com.quizapp.worklingo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentLessonRequestDTO {
    private Integer userId;

    private Integer lessonId;

    private LocalDateTime lastAccessed;
}
