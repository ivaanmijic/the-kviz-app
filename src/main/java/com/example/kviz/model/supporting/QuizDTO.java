package com.example.kviz.model.supporting;

import com.example.kviz.model.Quiz;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuizDTO {
    @Expose private Long id;
    @Expose private String title;
    @Expose private String thumbnail;
    @Expose private String description;
    @Expose private String category;
    @Expose private boolean visible;
    @Expose private String createdAt;
    @Expose private String updatedAt;

    public static QuizDTO fromEntity(Quiz quiz){
        QuizDTO dto = new QuizDTO();
        dto.id = quiz.getId();
        dto.title = quiz.getTitle();
        dto.thumbnail = quiz.getThumbnail();
        dto.description = quiz.getDescription();
        dto.category = quiz.getCategory().name();
        dto.visible = quiz.isVisible();
        dto.createdAt = quiz.getCreateAt().format(DateTimeFormatter.ISO_DATE_TIME);
        dto.updatedAt = quiz.getUpdatedAt() != null
                ? quiz.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null;
        return dto;
    }
}
