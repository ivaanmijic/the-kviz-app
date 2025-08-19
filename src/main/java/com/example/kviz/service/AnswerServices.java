package com.example.kviz.service;

import com.example.kviz.model.Answer;
import com.example.kviz.model.Question;
import com.example.kviz.repository.AnswerRepository;
import com.example.kviz.repository.QuestionRepository;

import java.util.List;

public class AnswerServices {
    private final AnswerRepository answerRepository;

    public AnswerServices() {
        this.answerRepository = new AnswerRepository();
    }

    public Answer save(Answer answer){
        return answerRepository.save(answer);
    }

    public List<Answer> findByQuestionId(long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
}
