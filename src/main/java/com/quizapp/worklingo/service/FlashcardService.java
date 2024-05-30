package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.repository.FlashcardRepository;
import com.quizapp.worklingo.service.interfaces.IFlashcardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardService implements IFlashcardService {
    private final FlashcardRepository flashcardRepository;

    @Override
    public FlashcardDTO getFlashcardById(int id) {
        return flashcardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flashcard not found")).toDTO();
    }

    @Override
    public List<FlashcardDTO> getAllFlashcards() {
        return flashcardRepository.findAll().stream().map(Flashcard::toDTO).toList();
    }

    @Override
    public void deleteFlashcardById(int id) {
        flashcardRepository.deleteById(id);
    }
}
