package com.example.testing_system.repository;

import com.example.testing_system.model.Category;
import com.example.testing_system.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    Optional<Test> findTestByQuestion(String question);

    Page<Test> findByCategory(Category category, Pageable pageable);
}
