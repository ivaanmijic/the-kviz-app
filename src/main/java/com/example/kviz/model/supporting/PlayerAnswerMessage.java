package com.example.kviz.model.supporting;

import java.util.List;

public class PlayerAnswerMessage {
    private String type;
    private List<String> answer; // <-- change this

    public PlayerAnswerMessage(String type, List<String> answer) {
        this.type = type;
        this.answer = answer;
    }
    public PlayerAnswerMessage() {}
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<String> getAnswer() {
        return answer;
    }
    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
