package com.example.kviz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(nullable = false)
    private Integer points;

    @Expose
    @Column(nullable = false)
    private Integer time;

    @Expose
    @Column
    private String image;


    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Answer> answers = new ArrayList<>();

    @Expose
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Expose
    @Column(nullable = false)
    private int orderNumber;

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

    public Question(String question, List<Answer> answers, String correctAnswer, Quiz quiz, QuestionType type) {
        this.question = question;
        this.answers = answers;
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

    public List<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
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

    public int getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean checkIfCorrectAnswer(String answer){
        return answer.equals(getCorrectAnswersAsString().getFirst());
    }
    public double checkIfCorrectAnswers(List<String> answers){
        int n = 0;
        for(Answer a : this.answers){
            if(a.isCorrect()) n++;
        }
        int m = 0;
        List<String> correctAnswers = getCorrectAnswersAsString();
        for(String a : answers){
            if(correctAnswers.contains(a)) m++;
            else m--;
        }
        if(m > 0) return ((double) m) /n;
        else return 0;
    }

    public List<String> getAnswersAsString(){
        List<String> answersAsString = new ArrayList<>();
        for(Answer a : this.answers){
            answersAsString.add(a.getAnswer());
        }
        return answersAsString;
    }
    public List<String> getCorrectAnswersAsString(){
        List<String> correctAnswersAsString = new ArrayList<>();
        for(Answer a : this.answers){
            if(a.isCorrect()){
                correctAnswersAsString.add(a.getAnswer());
            }
        }
        return  correctAnswersAsString;
    }

}
