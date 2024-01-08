package com.example.testing_system.repository;

import com.example.testing_system.model.SolvedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedTestRepository extends JpaRepository<SolvedTest, Integer> {
}
