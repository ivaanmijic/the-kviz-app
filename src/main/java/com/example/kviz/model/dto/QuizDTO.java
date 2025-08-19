package com.example.kviz.model.dto;

import com.example.kviz.model.Quiz;

public class QuizDTO {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private String category;
    private boolean visible;

    public static QuizDTO fromEntity(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.id = String.valueOf(quiz.getId());
        quizDTO.title = quiz.getTitle();
        quizDTO.description = quiz.getDescription();
        quizDTO.thumbnail = quiz.getThumbnail();
        quizDTO.category = quiz.getCategory().name();
        quizDTO.visible = quiz.isVisible();
        return quizDTO;
    }
}
