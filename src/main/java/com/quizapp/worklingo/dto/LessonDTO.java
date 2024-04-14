package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import lombok.Data;

import java.util.List;

@Data
public class LessonDTO {
    private Integer id;

    private Topic topic;

    private Integer authorId;

    private Integer numberOfUpVotes;

    private Integer numberOfDownVotes;

    private Integer numberOfFlashcards;

    public LessonDTO(Lesson lesson) {
        this.id = lesson.getId();
        this.topic = lesson.getTopic();
        this.authorId = lesson.getAuthor().getId();
        this.numberOfUpVotes = lesson.getNumberOfUpVotes();
        this.numberOfDownVotes = lesson.getNumberOfDownVotes();
        this.numberOfFlashcards = lesson.getNumberOfFlashcards();
    }
}
