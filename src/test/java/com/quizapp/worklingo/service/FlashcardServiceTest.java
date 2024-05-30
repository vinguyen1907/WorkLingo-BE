package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.repository.FlashcardRepository;
import com.quizapp.worklingo.service.interfaces.IFlashcardService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @InjectMocks
    private FlashcardService flashcardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetFlashcardById_Found() {
        int flashcardId = 1;
        Flashcard flashcard = new Flashcard();
        flashcard.setId(flashcardId);
        Lesson lesson = new Lesson();
        lesson.setId(2);
        flashcard.setLesson(lesson);
        FlashcardDTO flashcardDTO = new FlashcardDTO();
        flashcardDTO.setId(flashcardId);

        when(flashcardRepository.findById(flashcardId)).thenReturn(Optional.of(flashcard));

        FlashcardDTO result = flashcardService.getFlashcardById(flashcardId);

        assertNotNull(result);
        assertEquals(flashcardId, result.getId());
        verify(flashcardRepository, times(1)).findById(flashcardId);
    }

    @Test
    public void testGetFlashcardById_NotFound() {
        int flashcardId = 1;

        when(flashcardRepository.findById(flashcardId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flashcardService.getFlashcardById(flashcardId);
        });

        assertEquals("Flashcard not found", exception.getMessage());
        verify(flashcardRepository, times(1)).findById(flashcardId);
    }
}

