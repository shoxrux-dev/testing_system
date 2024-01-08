package com.example.testing_system.convert;

import com.example.testing_system.dto.solved_test.SolvedTestResponseDto;
import com.example.testing_system.model.SolvedTest;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class SolvedTestConverter {

    public List<SolvedTestResponseDto> convertToSolvedTestResponseDtoList(List<SolvedTest> solvedTestList) {
        return IntStream.range(0, solvedTestList.size())
                .mapToObj(index -> {
                    SolvedTest solvedTest = solvedTestList.get(index);
                    return SolvedTestResponseDto.builder()
                            .id(solvedTest.getId())
                            .isRight(solvedTest.isRight())
                            .ansByUser(solvedTest.getAnsByUser())
                            .test(
                                    TestConverter.convertToUserResponseDtoList(solvedTest, index + 1)
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

}
