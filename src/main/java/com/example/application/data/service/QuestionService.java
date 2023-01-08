package com.example.application.data.service;

import com.example.application.data.entity.Question;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public Optional<Question> get(Long id) {
        return repository.findById(id);
    }

    public Question update(Question entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Question> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Question> list(Pageable pageable, Specification<Question> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
