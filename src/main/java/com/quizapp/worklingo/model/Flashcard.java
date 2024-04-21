package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.FlashcardDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE flashcard SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String question;

    private String answer;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Lesson lesson;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private boolean isDeleted = Boolean.FALSE;

    @PrePersist
    public void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

    public FlashcardDTO toDTO() {
        return new FlashcardDTO(this);
    }
}
