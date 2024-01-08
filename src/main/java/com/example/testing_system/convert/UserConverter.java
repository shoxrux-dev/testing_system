package com.example.testing_system.convert;

import com.example.testing_system.dto.role.RoleResponseDto;
import com.example.testing_system.dto.user.UserCreateRequestDto;
import com.example.testing_system.dto.user.UserResponseForAdminDto;
import com.example.testing_system.dto.user.UserResponseForUserDto;
import com.example.testing_system.model.User;
import com.example.testing_system.util.TimeFormatter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserConverter {
    public User convertToDomain(UserCreateRequestDto userCreateRequestDto){
        return User.builder()
                .username(userCreateRequestDto.getUsername())
                .password(userCreateRequestDto.getPassword())
                .build();
    }

    public List<UserResponseForUserDto> convertToUserResponseForUserDto(List<User> users) {
        return users.stream()
                .map(UserConverter::convertToUserResponseForUserDto)
                .collect(Collectors.toList());
    }

    public UserResponseForUserDto convertToUserResponseForUserDto(User user){
        LocalDateTime createdAt = user.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime updatedAt = user.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return UserResponseForUserDto.builder()
                .username(user.getUsername())
                .image(user.getImage())
                .createdAt(TimeFormatter.formatDateTime(createdAt))
                .updatedAt(TimeFormatter.formatDateTime(updatedAt))
                .build();
    }

    public UserResponseForAdminDto convertToUserResponseForAdminDto(User user) {
        List<RoleResponseDto> roleResponseDto = RoleConverter.convertToRoleResponseDtoList(user.getRoles());

        LocalDateTime createdAt = user.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime updatedAt = user.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return UserResponseForAdminDto.builder()
                .id(user.getId())
                .image(user.getImage())
                .username(user.getUsername())
                .roles(roleResponseDto)
                .createdAt(TimeFormatter.formatDateTime(createdAt))
                .updatedAt(TimeFormatter.formatDateTime(updatedAt))
                .build();
    }

    public List<UserResponseForAdminDto> convertToUserResponseForAdminDtoList(List<User> users) {
        return users.stream()
                .map(UserConverter::convertToUserResponseForAdminDto)
                .sorted()
                .collect(Collectors.toList());
    }

}
