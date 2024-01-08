package com.example.testing_system.dto.category;

import com.example.testing_system.dto.BaseResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponseDto extends BaseResponseDto implements Comparable<CategoryResponseDto> {
    String name;
    String image;

    @Override
    public int compareTo(CategoryResponseDto o) {
        return this.getId().compareTo(o.getId());
    }

}
