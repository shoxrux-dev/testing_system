package com.example.testing_system.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordOfUserRequestDto {
    @NotBlank String currentPassword;
    @NotBlank String newPassword;
}
