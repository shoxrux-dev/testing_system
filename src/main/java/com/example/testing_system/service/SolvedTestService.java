package com.example.testing_system.service;

import com.example.testing_system.model.SolvedTest;
import com.example.testing_system.model.Test;
import com.example.testing_system.model.User;
import com.example.testing_system.repository.SolvedTestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SolvedTestService {
    SolvedTestRepository solvedTestRepository;

    public SolvedTest solve(Test test, String ans, User user, Instant time) {
        SolvedTest solvedTest = SolvedTest.builder()
                .ansByUser(ans)
                .isRight(test.getAnswer().equals(ans))
                .question(test.getQuestion())
                .createdAt(time)
                .updatedAt(time)
                .build();

        if(Objects.nonNull(user)) {
            solvedTestRepository.save(solvedTest);
        }

        return solvedTest;
    }

}
