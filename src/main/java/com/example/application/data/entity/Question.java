package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Question extends AbstractEntity {

    private String category;
    private String subcategory;
    private String question;

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

}
