package com.example.kviz.model;

import com.google.gson.annotations.Expose;
import com.example.kviz.model.Admin;
import com.example.kviz.model.Question;
import jakarta.persistence.*;
import java.util.List;

import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(nullable = false)
    private String title;

    @Expose
    @Column(nullable = false)
    private String thumbnail;

    @Expose
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin owner;

    @OneToMany
    @JoinColumn(name = "questions", nullable = false)
    private List<Question> questions;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }
    //MARK: - Initialization

    public Quiz(){
    }

    public Quiz(String title, String thumbnail, String description, Admin owner) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.owner = owner;
    }

    //MARK - Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Admin getOwner() {
        return owner;
    }
    public void setOwner(Admin owner) {
        this.owner = owner;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions){
        this.questions = questions;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
