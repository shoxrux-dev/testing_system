package com.example.testing_system.dto.test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseTestRequestDto {
    @NotBlank String question;
    @NotBlank String answer;
    @NotBlank String answers;
    @NotNull Integer categoryId;
}
