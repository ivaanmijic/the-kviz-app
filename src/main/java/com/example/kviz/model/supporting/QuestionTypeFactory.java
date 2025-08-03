package com.example.kviz.model.supporting;

public class QuestionTypeFactory {
    public static QuestionType getType(String category) {
        switch(category) {
            case "more_correct": {
                return QuestionType.MULTIPLE_CORRECT;
            }
            case "slider": {
                return QuestionType.SLIDER;
            }
            default:{
                return QuestionType.CLASSIC;
            }
        }
    }}
