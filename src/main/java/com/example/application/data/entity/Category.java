package com.example.application.data.entity;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Category extends AbstractEntity {

    private LocalDate tanggal;
    private String category;

    public LocalDate getTanggal() {
        return tanggal;
    }
    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

}
