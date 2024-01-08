package com.example.testing_system.convert;

import com.example.testing_system.dto.test.BaseTestRequestDto;
import com.example.testing_system.dto.test.TestResponseForAdminDto;
import com.example.testing_system.dto.test.TestResponseForUserDto;
import com.example.testing_system.model.SolvedTest;
import com.example.testing_system.model.Test;
import com.example.testing_system.util.TimeFormatter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class TestConverter {
    public <T extends BaseTestRequestDto> Test convertToDomain(T request) {
        return Test.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .answers(request.getAnswers())
                .build();
    }

    public List<TestResponseForUserDto> convertToUserResponseDtoList(List<Test> tests) {
        return IntStream.range(0, tests.size())
                .mapToObj(i -> convertToUserResponseDtoList(tests.get(i), i + 1))
                .collect(Collectors.toList());
    }

    public TestResponseForUserDto convertToUserResponseDtoList(Test test, int testId) {
        return TestResponseForUserDto.builder()
                .id(test.getId())
                .testId(testId)
                .question(test.getQuestion())
                .answers(getAnswers(test, true))
                .build();
    }

    public TestResponseForUserDto convertToUserResponseDtoList(SolvedTest solvedTest, int testId) {
        return TestResponseForUserDto.builder()
                .testId(testId)
                .question(solvedTest.getQuestion())
                .build();
    }

    public List<TestResponseForAdminDto> convertToAdminResponseDtoList(List<Test> tests) {
        return tests.stream()
                .map(test -> {
                    LocalDateTime createdAt = test.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime updatedAt = test.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    return TestResponseForAdminDto.builder()
                            .id(test.getId())
                            .question(test.getQuestion())
                            .answer(test.getAnswer())
                            .answers(getAnswers(test, false))
                            .categoryName(test.getCategory().getName())
                            .createdAt(TimeFormatter.formatDateTime(createdAt))
                            .updatedAt(TimeFormatter.formatDateTime(updatedAt))
                            .build();
                })
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getAnswers(Test test, boolean isShuffle) {
        List<String> answers = Arrays.asList(test.getAnswers().split(","));
        if (isShuffle) {
            Collections.shuffle(answers);
        }
        return answers;
    }

}
