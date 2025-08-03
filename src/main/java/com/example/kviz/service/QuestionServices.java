package com.example.kviz.service;

import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.repository.QuestionRepository;
import com.example.kviz.repository.QuizRepository;

import java.util.List;
import java.util.Optional;

public class QuestionServices {
    private final QuestionRepository questionRepository;

    public QuestionServices() {
        this.questionRepository = new QuestionRepository();
    }

    public Question save(Question question){
        return questionRepository.save(question);
    }

    public Optional<Question> findById(Long id){
        return questionRepository.findById(id);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public List<Question> findByQuiz(Quiz quiz) {
        return questionRepository.findByQuiz(quiz);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
