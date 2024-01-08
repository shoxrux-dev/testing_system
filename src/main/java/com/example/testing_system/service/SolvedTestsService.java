package com.example.testing_system.service;

import com.example.testing_system.convert.SolvedTestsConverter;
import com.example.testing_system.model.*;
import com.example.testing_system.repository.SolvedTestsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SolvedTestsService {
    SolvedTestsRepository solvedTestsRepository;
    SolvedTestService solvedTestService;
    TestService testService;
    UserService userService;

    public SolvedTests solveTests(List<String> answers, User user) {
        List<SolvedTest> solvedTestList = new ArrayList<>();
        Map<Integer, String> testIdAndAns = getTestIdAndAns(answers);
        AtomicInteger correctCount = new AtomicInteger(0);

        Instant now;

        try (Stream<Test> testStream = testIdAndAns.keySet().stream()
                .map(testService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
        ) {
            now = Instant.now();
            testStream.forEach(test -> {
                SolvedTest solvedTest = solvedTestService.solve(test, testIdAndAns.get(test.getId()), user, now);
                solvedTestList.add(solvedTest);

                if (solvedTest.isRight()) {
                    correctCount.incrementAndGet();
                }
            });
        }

        int totalTests = solvedTestList.size();
        int truePercentage = correctCount.get() * 100 / totalTests;
        int falsePercentage = (totalTests - correctCount.get()) * 100 / totalTests;

        SolvedTests solvedTests = SolvedTestsConverter.convertToSolvedTests(
                solvedTestList,
                truePercentage,
                falsePercentage,
                now
        );

        if (Objects.nonNull(user)) {
            Optional<User> userByUsername = userService.getByUsername(user.getUsername());
            userByUsername.ifPresent(solvedTests::setUser);
            solvedTestsRepository.save(solvedTests);
        }

        return solvedTests;
    }

    public Map<Integer, String> getTestIdAndAns(List<String> answers) {
        return answers.stream()
                .map(answer -> answer.split("-", 2))
                .collect(Collectors.toMap(
                        parts -> Integer.valueOf(parts[0]),
                        parts -> parts[1]
                ));
    }

    public List<SolvedTests> getTopSolvedTests5ByUser(User user) {
        return solvedTestsRepository.findTop5ByUserOrderByCreatedAtDesc(user);
    }

    public Optional<SolvedTests> getSolvedTestsByIdAndUser(Integer id, User user) {
        return solvedTestsRepository.findSolvedTestsByIdAndUser(id, user);
    }

    public List<SolvedTests> getTopSolvedTestsByCreatedAtAndUser(Instant startTime, Instant endTime, User user) {
        return solvedTestsRepository.findSolvedTestsByCreatedAtAndUser(startTime, endTime, user);
    }

    public List<User> getTop3UsersByTruePercentage() {
        List<String> top3UsersByTruePercentage = solvedTestsRepository.findTop3UsersByTruePercentage();
        return top3UsersByTruePercentage.stream()
                .map(userService::getByUsername)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
