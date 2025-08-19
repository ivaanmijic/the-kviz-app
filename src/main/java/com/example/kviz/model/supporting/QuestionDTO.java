package com.example.kviz.model.supporting;

import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.google.gson.annotations.Expose;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuestionDTO {
    @Expose private Long id;
    @Expose private String question;
    @Expose private String thumbnail;
    @Expose private Integer points;
    @Expose private Integer time;
    @Expose private String category;
    @Expose private List<String> answers;
    @Expose private List<String> correctAnswers;
    @Expose private String createdAt;
    @Expose private String updatedAt;

    public static QuestionDTO fromEntity(Question question){
        QuestionDTO dto = new QuestionDTO();
        dto.id = question.getId();
        dto.question = question.getQuestion();
        dto.thumbnail = question.getImage();
        dto.points = question.getPoints();
        dto.time = question.getTime();
        dto.category = question.getType().name();
        dto.answers = question.getAnswersAsString();
        dto.correctAnswers = question.getCorrectAnswersAsString();
        dto.createdAt = question.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);
        dto.updatedAt = question.getUpdatedAt() != null
                ? question.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null;
        return dto;
    }
}

