package com.example.testing_system.dto.solved_test;

import com.example.testing_system.dto.test.TestResponseForUserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SolvedTestResponseDto {
    Integer id;
    boolean isRight;
    String ansByUser;
    TestResponseForUserDto test;
}
