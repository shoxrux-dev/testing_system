package com.example.testing_system.dto.test;

import com.example.testing_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestResponseForAdminDto extends BaseResponseDto implements Comparable<TestResponseForAdminDto> {
    String answer;
    String categoryName;
    String question;
    List<String> answers;

    @Override
    public int compareTo(TestResponseForAdminDto o) {
        return this.getId().compareTo(o.getId());
    }
}
