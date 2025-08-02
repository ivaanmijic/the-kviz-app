package com.example.kviz.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.kviz.model.supporting.AdminRole;
import com.example.kviz.model.supporting.QuestionType;
import com.google.gson.annotations.Expose;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(nullable = false)
    private String question;

    @Expose
    @Column
    private List<String> answers;

    @Expose
    @Column
    private String correct_answer;

    @Expose
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // MARK: - Initialization

    public Question() {
    }

    public Question(String question, List<String> answers, String correct_answer, Quiz quiz, QuestionType type) {
        this.question = question;
        this.answers = answers;
        this.correct_answer = correct_answer;
        this.quiz = quiz;
        this.type = type;
    }

    // MARK: - Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }
    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public QuestionType getType() {
        return type;
    }
    public void setType(QuestionType type) {
        this.type = type;
    }

    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public boolean checkIfCorrectAnswer(String answer){
        return answer.equals(this.correct_answer);
    }

}
