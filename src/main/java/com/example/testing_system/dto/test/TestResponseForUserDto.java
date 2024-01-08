package com.example.testing_system.dto.test;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestResponseForUserDto implements Comparable<TestResponseForUserDto> {
    Integer id;
    Integer testId;
    String question;
    List<String> answers;

    @Override
    public int compareTo(TestResponseForUserDto o) {
        return this.getId().compareTo(o.getId());
    }
}
