package com.example.testing_system.dto.user;

import com.example.testing_system.dto.role.RoleResponseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseForAdminDto extends BaseUserResponseDto implements Comparable<UserResponseForAdminDto> {
    List<RoleResponseDto> roles;

    @Override
    public int compareTo(UserResponseForAdminDto o) {
        return super.getId().compareTo(o.getId());
    }
}
