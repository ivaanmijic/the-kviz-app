package com.example.kviz.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column (nullable = false)
    private String answer;

    @Expose
    @Column (nullable = false)
    private boolean correct;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Answer(String answer, Question question, boolean correct) {
        this.answer = answer;
        this.question = question;
        this.correct = correct;
    }

    public Answer() {

    }

    public void setAnswer(String answer) {this.answer = answer;}
    public void setCorrect(boolean correct) {this.correct = correct;}
    public void setQuestion(Question question) {this.question = question;}
    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
    public String getAnswer() {return answer;}
    public boolean isCorrect() {return correct;}
    public Question getQuestion() {return question;}
}
