package com.example.application.data.service;

import com.example.application.data.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long>, JpaSpecificationExecutor<Subcategory> {

}
