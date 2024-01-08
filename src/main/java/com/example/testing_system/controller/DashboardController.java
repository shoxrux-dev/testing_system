package com.example.testing_system.controller;

import com.example.testing_system.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class DashboardController {
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String get(Model model) {
        model.addAttribute("usersCount", userService.getAllUsersCount());
        model.addAttribute("usersCountAddedLastMonth", userService.getUsersCountAddedLastMonth());
        return "views/admin/index";
    }
}
