package com.example.kviz.model.supporting;

public class QuizCategoryFactory {
    public static QuizCategory getCategory(String category) {
        switch(category) {
            case "technology": {
                return QuizCategory.TECHNOLOGY;
            }
            case "science": {
                return QuizCategory.SCIENCE;
            }
            case "pop_culture": {
                return QuizCategory.POP_CULTURE;
            }
            case "geography": {
                return QuizCategory.GEOGRAPHY;
            }
            case "sport": {
                return QuizCategory.SPORT;
            }
            case "history": {
                return QuizCategory.HISTORY;
            }
            default:{
                return QuizCategory.OTHER;
            }
        }
    }
}
