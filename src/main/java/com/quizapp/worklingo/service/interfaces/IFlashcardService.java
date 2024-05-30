package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.model.Flashcard;

import java.util.List;

public interface IFlashcardService {
    FlashcardDTO getFlashcardById(int id);

    List<FlashcardDTO> getAllFlashcards();

    void deleteFlashcardById(int id);
}
