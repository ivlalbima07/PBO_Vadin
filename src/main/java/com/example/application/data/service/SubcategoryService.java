package com.example.application.data.service;

import com.example.application.data.entity.Subcategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SubcategoryService {

    private final SubcategoryRepository repository;

    public SubcategoryService(SubcategoryRepository repository) {
        this.repository = repository;
    }

    public Optional<Subcategory> get(Long id) {
        return repository.findById(id);
    }

    public Subcategory update(Subcategory entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Subcategory> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Subcategory> list(Pageable pageable, Specification<Subcategory> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
