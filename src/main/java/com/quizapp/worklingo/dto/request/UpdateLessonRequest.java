package com.quizapp.worklingo.dto.request;

import com.quizapp.worklingo.enums.LessonVisibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateLessonRequest {
    private String title;

    @Enumerated(EnumType.STRING)
    private LessonVisibility visibility;
}
