package com.example.testing_system.convert;

import com.example.testing_system.dto.role.RoleRequestDto;
import com.example.testing_system.dto.role.RoleResponseDto;
import com.example.testing_system.model.Role;
import com.example.testing_system.util.TimeFormatter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RoleConverter {
    public List<RoleResponseDto> convertToRoleResponseDtoList(List<Role> roles) {
        return roles.stream()
                .map(role -> {
                    LocalDateTime createdAt = role.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime updatedAt = role.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    return RoleResponseDto.builder()
                            .id(role.getId())
                            .name(role.getName())
                            .createdAt(TimeFormatter.formatDateTime(createdAt))
                            .updatedAt(TimeFormatter.formatDateTime(updatedAt))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Role convertToRole(RoleRequestDto roleRequestDto) {
        return Role.builder()
                .name(roleRequestDto.getName())
                .build();
    }

}
