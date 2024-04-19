package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Topic topic;

    @ManyToOne(cascade = CascadeType.ALL)
    private User author;

    private Integer numberOfUpVotes;

    private Integer numberOfDownVotes;

    private Integer numberOfFlashcards;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Flashcard> flashcards;

    @Enumerated(EnumType.STRING)
    private LessonVisibility visibility;

    public Lesson(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.topic = lesson.getTopic();
        this.author = lesson.getAuthor();
        this.numberOfUpVotes = lesson.getNumberOfUpVotes();
        this.numberOfDownVotes = lesson.getNumberOfDownVotes();
        this.numberOfFlashcards = lesson.getNumberOfFlashcards();
        this.flashcards = lesson.getFlashcards();
        this.visibility = lesson.getVisibility();
    }

    public LessonDTO toDTO() {
        return new LessonDTO(this);
    }
}
