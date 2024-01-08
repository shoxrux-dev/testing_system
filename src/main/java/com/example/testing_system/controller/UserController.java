package com.example.testing_system.controller;

import com.example.testing_system.convert.SolvedTestsConverter;
import com.example.testing_system.convert.UserConverter;
import com.example.testing_system.dto.response.PageResponse;
import com.example.testing_system.dto.solved_tests.SolvedTestsResponseDto;
import com.example.testing_system.dto.user.*;
import com.example.testing_system.model.SolvedTests;
import com.example.testing_system.model.User;
import com.example.testing_system.service.SolvedTestsService;
import com.example.testing_system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    SolvedTestsService solvedTestsService;

    @PostMapping("/add")
    public String register(
            RedirectAttributes redirectAttributes,
            @ModelAttribute UserCreateRequestDto userCreateRequestDto
    ) {
        User user = UserConverter.convertToDomain(userCreateRequestDto);
        if(userService.save(user)) {
            redirectAttributes.addFlashAttribute("success", "Registration successfully ");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Register failure. Username may already exist");
            return "redirect:/register";
        }
    }

    @GetMapping("/profile")
    public String getProfile(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "all", required = false) String date,
            Model model
    ) {
        Optional<User> userByUsername = userService.getByUsername(user.getUsername());
        if (userByUsername.isPresent()) {
            UserResponseForUserDto userResponseForUserDto = UserConverter.convertToUserResponseForUserDto(userByUsername.get());
            model.addAttribute("user", userResponseForUserDto);

            if(date.equals("all")) {
                List<SolvedTests> solvedTests = solvedTestsService.getTopSolvedTests5ByUser(userByUsername.get());
                List<SolvedTestsResponseDto> solvedTestsResponseDtoList = SolvedTestsConverter.convertToSolvedTestsResponseDtoList(solvedTests);
                model.addAttribute("solvedTestsHistory", solvedTestsResponseDtoList);
            } else {
                LocalDate localDate = LocalDate.of(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7)), Integer.parseInt(date.substring(8)));
                Instant startTime = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                Instant endTime = localDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
                List<SolvedTests> topSolvedTestsByCreatedAtAndUser = solvedTestsService.getTopSolvedTestsByCreatedAtAndUser(startTime, endTime, userByUsername.get());
                List<SolvedTestsResponseDto> solvedTestsResponseDtoList = SolvedTestsConverter.convertToSolvedTestsResponseDtoList(topSolvedTestsByCreatedAtAndUser);
                model.addAttribute("solvedTestsHistory", solvedTestsResponseDtoList);
            }
        }

        return "views/user/profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(
            @ModelAttribute UpdatePasswordOfUserRequestDto updatePasswordOfUserRequestDto,
            RedirectAttributes redirectAttributes
    ) {

        if (updatePasswordOfUserRequestDto.getCurrentPassword().equals(updatePasswordOfUserRequestDto.getNewPassword())) {
            redirectAttributes.addFlashAttribute("error", "Password not updated successfully");
            return "redirect:/profile";
        }

        boolean result = userService.updatePassword(updatePasswordOfUserRequestDto);

        if (result) {
            redirectAttributes.addFlashAttribute("success", "Password updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Password not updated successfully");
        }
        return "redirect:/profile";
    }

    @PostMapping("/update-username")
    public String updateUsername(
            @ModelAttribute UpdateUsernameOfUserRequestDto updateUsernameOfUserRequestDto,
            RedirectAttributes redirectAttributes
    ) {

        if (updateUsernameOfUserRequestDto.getNewUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            redirectAttributes.addFlashAttribute("warning", "New username is already in use");
            return "redirect:/profile";
        }

        boolean result = userService.updateUsername(updateUsernameOfUserRequestDto);

        if (result) {
            redirectAttributes.addFlashAttribute("success", "Username updated successfully");
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("error", "Username not updated successfully. Username may already exist.");
            return "redirect:/profile";
        }
    }

    @GetMapping("/solved-tests-history/{solvedTestsId}")
    public String getSolvedTestsHistory(
            @PathVariable Integer solvedTestsId,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Optional<User> userByUsername = userService.getByUsername(user.getUsername());
        if(userByUsername.isPresent()) {
            Optional<SolvedTests> solvedTestsByIdAndUser = solvedTestsService.getSolvedTestsByIdAndUser(solvedTestsId, userByUsername.get());
            solvedTestsByIdAndUser.ifPresent(solvedTests -> model.addAttribute("solvedTests", SolvedTestsConverter.convertToSolvedTestsResponseDto(solvedTests)));
        }
        return "views/user/solved-tests-history";
    }

    @PostMapping("/update-image")
    public String updateImage(
            @ModelAttribute UpdateImageOfUserRequestDto updateImageOfUserRequestDto,
            RedirectAttributes redirectAttributes
    ) {
        boolean result = userService.updateImage(updateImageOfUserRequestDto);
        if(result) {
            redirectAttributes.addFlashAttribute("success", "Image updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Image not updated successfully");
        }
        return "redirect:/profile";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUser(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<UserResponseForAdminDto> users = userService.getAll(pageNum, pageSize);
        model.addAttribute("users", users);
        return "views/admin/users/index";
    }

}