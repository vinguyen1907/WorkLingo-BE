package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import lombok.Data;

import java.util.List;

@Data
public class LessonDTO {
    private Integer id;

    private String title;

    private Topic topic;

    private Integer authorId;

    private Integer numberOfUpVotes;

    private Integer numberOfDownVotes;

    private Integer numberOfFlashcards;

    private LessonVisibility visibility;

    public LessonDTO(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.topic = lesson.getTopic();
        this.authorId = lesson.getAuthor().getId();
        this.numberOfUpVotes = lesson.getNumberOfUpVotes();
        this.numberOfDownVotes = lesson.getNumberOfDownVotes();
        this.numberOfFlashcards = lesson.getNumberOfFlashcards();
        this.visibility = lesson.getVisibility();
    }
}
