package com.example.kviz.service;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Admin;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.dto.QuizDTO;
import com.example.kviz.repository.QuizRepository;
import jakarta.persistence.EntityTransaction;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService() {
        this.quizRepository = new QuizRepository();
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

    public List<Quiz> findByOwnerId(Long id) {
        return quizRepository.findAllByAdminId(id);
    }

    public List<Quiz> findAllPublic(Long forAdminWithId) {
        return quizRepository.findAllPublic(forAdminWithId);
    }

    public void delete(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    public void deleteById(Long id) {
        quizRepository.deleteById(id);
    }

}
