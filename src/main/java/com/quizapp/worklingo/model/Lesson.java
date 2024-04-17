package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToOne
    private Topic topic;

    @ManyToOne
    private User author;

    private Integer numberOfUpVotes;

    private Integer numberOfDownVotes;

    private Integer numberOfFlashcards;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Flashcard> flashcards;

    public Lesson(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.topic = lesson.getTopic();
        this.author = lesson.getAuthor();
        this.numberOfUpVotes = lesson.getNumberOfUpVotes();
        this.numberOfDownVotes = lesson.getNumberOfDownVotes();
        this.numberOfFlashcards = lesson.getNumberOfFlashcards();
        this.flashcards = lesson.getFlashcards();
    }

    public LessonDTO toDTO() {
        return new LessonDTO(this);
    }
}
