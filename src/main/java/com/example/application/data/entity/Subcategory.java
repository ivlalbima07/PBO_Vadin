package com.example.application.data.entity;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Subcategory extends AbstractEntity {

    private LocalDate tanggal;
    private String subcategory;

    public LocalDate getTanggal() {
        return tanggal;
    }
    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }
    public String getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

}
