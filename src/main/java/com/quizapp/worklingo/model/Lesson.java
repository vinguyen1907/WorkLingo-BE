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

    @ManyToOne
    private Topic topic;

    @ManyToOne
    private User author;

    private Integer numberOfUpVotes;

    private Integer numberOfDownVotes;

    private Integer numberOfFlashcards;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Flashcard> flashcards;

    public LessonDTO toDTO() {
        return new LessonDTO(this);
    }
}
