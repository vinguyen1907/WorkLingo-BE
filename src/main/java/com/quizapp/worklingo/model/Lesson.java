package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_id")
    private List<Flashcard> flashcards;

    @Enumerated(EnumType.STRING)
    private LessonVisibility visibility;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

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
        this.createdTime = lesson.getCreatedTime();
        this.updatedTime = lesson.getUpdatedTime();
    }

    public LessonDTO toDTO() {
        return new LessonDTO(this);
    }

    @PrePersist
    public void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}
