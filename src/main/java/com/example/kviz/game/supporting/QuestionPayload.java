package com.example.kviz.game.supporting;

import java.util.List;

public class QuestionPayload {
    String type = "question";
    String questionType;
    String question;
    List<String> answers;
    String time;
    String deadline;
    String image;

    public QuestionPayload(String questionType, String question, List<String> answers, String time, String deadline, String image) {
        this.questionType = questionType;
        this.question = question;
        this.answers = answers;
        this.time = time;
        this.deadline = deadline;
        this.image = image;
    }
}
