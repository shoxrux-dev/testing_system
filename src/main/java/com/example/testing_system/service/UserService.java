package com.example.testing_system.service;


import com.example.testing_system.constant.FilePathConstant;
import com.example.testing_system.constant.RoleConstant;
import com.example.testing_system.convert.UserConverter;
import com.example.testing_system.dto.response.PageResponse;
import com.example.testing_system.dto.user.UpdateImageOfUserRequestDto;
import com.example.testing_system.dto.user.UpdatePasswordOfUserRequestDto;
import com.example.testing_system.dto.user.UpdateUsernameOfUserRequestDto;
import com.example.testing_system.dto.user.UserResponseForAdminDto;
import com.example.testing_system.model.Role;
import com.example.testing_system.model.User;
import com.example.testing_system.repository.UserRepository;
import com.example.testing_system.util.FileUtil;
import com.example.testing_system.validation.CommonSchemaValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    CommonSchemaValidator commonSchemaValidator;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleService roleService;

    public boolean save(User user) {
        if(commonSchemaValidator.userExist(user.getUsername())) {
            return false;
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Instant now = Instant.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setPassword(hashedPassword);
        user.setImage("avatar.png");

        Optional<Role> role = commonSchemaValidator.isAdmin()
                ? roleService.getByName(RoleConstant.ADMIN.toString())
                : roleService.getByName(RoleConstant.USER.toString());

        if(role.isPresent()) {
            user.setRoles(Collections.singletonList(role.get()));
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public boolean updatePassword(UpdatePasswordOfUserRequestDto updatePasswordOfUserRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> byUsername = getByUsername(authentication.getName());
        if (byUsername.isPresent()) {
            boolean matches = passwordEncoder.matches(
                    updatePasswordOfUserRequestDto.getCurrentPassword(),
                    byUsername.get().getPassword()
            );
            if (matches) {
                String hashedPassword = passwordEncoder.encode(updatePasswordOfUserRequestDto.getNewPassword());
                byUsername.get().setPassword(hashedPassword);
                User updatedUser = userRepository.save(byUsername.get());
                updateSecurityContextHolder(updatedUser, authentication);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean updateUsername(UpdateUsernameOfUserRequestDto updateUsernameOfUserRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> byUsername = getByUsername(authentication.getName());
        if (byUsername.isPresent()) {
            byUsername.get().setUsername(updateUsernameOfUserRequestDto.getNewUsername());
            User updatedUser = userRepository.save(byUsername.get());
            updateSecurityContextHolder(updatedUser, authentication);
            return true;
        }
        return false;
    }

    public boolean updateImage(UpdateImageOfUserRequestDto updateImageOfUserRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userByUsername = getByUsername(authentication.getName());

        return userByUsername.map(user -> {
            if(!user.getImage().equals("avatar.png")) {
                FileUtil.deleteImage(user.getImage(), FilePathConstant.USER);
            }
            String imageName = FileUtil.uploadImage(updateImageOfUserRequestDto.getImage(), FilePathConstant.USER);
            userByUsername.get().setImage(imageName);
            User updatedUser = userRepository.save(userByUsername.get());
            updateSecurityContextHolder(updatedUser, authentication);
            return true;
        }).orElse(false);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public PageResponse<UserResponseForAdminDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<User> users = userRepository.findAll(pageRequest);

        return new PageResponse<>(
                UserConverter.convertToUserResponseForAdminDtoList(users.getContent()),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
    }

    public long getAllUsersCount() {
        return userRepository.count();
    }

    public long getUsersCountAddedLastMonth() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusMonths(1);
        return userRepository.findByCreatedAtBetween(start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC)).size();
    }

    private void updateSecurityContextHolder(User user, Authentication authentication) {
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                user, authentication.getCredentials(), authentication.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }
}
