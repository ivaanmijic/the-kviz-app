package com.example.kviz.model;

import java.time.LocalDateTime;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Expose
    @Column(nullable = false)
    private Integer points;

    @Expose
    @Column(nullable = false)
    private Integer time;

    @Expose
    @Column
    private String image;

    @Expose
    @Column(nullable = false)
    private List<String> answers;

    @Expose
    @Column
    private String correct_answer;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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
        this.questionType = type;
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

    public QuestionType getQuestionType() {
        return questionType;
    }
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getPoints() {
        return points;
    }
    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTime() {
        return time;
    }
    public void setTime(Integer time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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
