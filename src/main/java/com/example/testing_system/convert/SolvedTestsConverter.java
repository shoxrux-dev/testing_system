package com.example.testing_system.convert;

import com.example.testing_system.dto.solved_tests.SolvedTestsResponseDto;
import com.example.testing_system.model.SolvedTest;
import com.example.testing_system.model.SolvedTests;
import com.example.testing_system.util.TimeFormatter;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SolvedTestsConverter {
    public SolvedTestsResponseDto convertToSolvedTestsResponseDto(SolvedTests solvedTests) {
        LocalDateTime createdAt = solvedTests.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return SolvedTestsResponseDto.builder()
                .id(solvedTests.getId())
                .truePercentage(solvedTests.getTruePercentage())
                .falsePercentage(solvedTests.getFalsePercentage())
                .createdAt(TimeFormatter.formatDateTime(createdAt))
                .solvedTestList(SolvedTestConverter.convertToSolvedTestResponseDtoList(solvedTests.getSolvedTestList()))
                .build();
    }

    public SolvedTests convertToSolvedTests(
            List<SolvedTest> solvedTestList,
            int correctPercentage,
            int incorrectPercentage,
            Instant now) {
        return SolvedTests.builder()
                .solvedTestList(solvedTestList)
                .truePercentage(correctPercentage)
                .falsePercentage(incorrectPercentage)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public List<SolvedTestsResponseDto> convertToSolvedTestsResponseDtoList(List<SolvedTests> solvedTests) {
        return solvedTests.stream()
                .map(SolvedTestsConverter::convertToSolvedTestsResponseDto)
                .collect(Collectors.toList());
    }

}
