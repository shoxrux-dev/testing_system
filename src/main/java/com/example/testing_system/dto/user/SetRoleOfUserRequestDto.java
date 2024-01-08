package com.example.testing_system.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetRoleOfUserRequestDto {
    @NotNull Integer roleId;
    @NotNull Integer userId;
}
