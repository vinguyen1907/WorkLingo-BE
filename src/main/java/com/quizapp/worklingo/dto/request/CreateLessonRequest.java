package com.quizapp.worklingo.dto.request;

import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonRequest {
    private String title;
    private Integer topicId;
    private Integer authorId;
    private List<Flashcard> flashcards;
    private LessonVisibility visibility;
}
