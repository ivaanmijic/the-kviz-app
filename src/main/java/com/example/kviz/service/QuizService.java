package com.example.kviz.service;

import com.example.kviz.model.Admin;
import com.example.kviz.model.Quiz;
import com.example.kviz.repository.QuizRepository;

import java.util.List;
import java.util.Optional;

public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> findByTitle(String title) {
        return quizRepository.findByTitle(title);
    }

    public List<Quiz> findByOwner(Admin owner) {
        return quizRepository.findAllByAdmin(owner);
    }

    public void delete(Quiz quiz) {
        quizRepository.delete(quiz);
    }

}
