package com.example.testing_system.dto.solved_tests;

import com.example.testing_system.dto.BaseResponseDto;
import com.example.testing_system.dto.solved_test.SolvedTestResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SolvedTestsResponseDto extends BaseResponseDto {
    int truePercentage;
    int falsePercentage;
    List<SolvedTestResponseDto> solvedTestList;
}
