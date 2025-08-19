package com.example.kviz.model.supporting;

public class QuestionTypeFactory {
    public static QuestionType getType(String category) {
        switch(category) {
            case "multiple_correct": {
                return QuestionType.MULTIPLE_CORRECT;
            }
            default:{
                return QuestionType.CLASSIC;
            }
        }
    }}
