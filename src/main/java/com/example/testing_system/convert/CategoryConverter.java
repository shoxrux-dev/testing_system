package com.example.testing_system.convert;

import com.example.testing_system.dto.category.BaseCategoryRequestDto;
import com.example.testing_system.dto.category.CategoryResponseDto;
import com.example.testing_system.model.Category;
import com.example.testing_system.util.TimeFormatter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryConverter {
    public <T extends BaseCategoryRequestDto> Category convertToDomain(T request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }

    public List<CategoryResponseDto> convertToDtoList(List<Category> categories) {
        return categories.stream()
                .map(category -> {
                    LocalDateTime createdAt = category.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime updatedAt = category.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    return CategoryResponseDto.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .image(category.getImage())
                            .createdAt(TimeFormatter.formatDateTime(createdAt))
                            .updatedAt(TimeFormatter.formatDateTime(updatedAt))
                            .build();
                })
                .sorted()
                .collect(Collectors.toList());
    }

}
