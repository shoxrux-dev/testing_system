package com.example.testing_system.controller;

import com.example.testing_system.convert.CategoryConverter;
import com.example.testing_system.convert.SolvedTestsConverter;
import com.example.testing_system.convert.TestConverter;
import com.example.testing_system.convert.UserConverter;
import com.example.testing_system.dto.category.CategoryResponseDto;
import com.example.testing_system.dto.test.TestResponseForUserDto;
import com.example.testing_system.dto.solved_tests.SolvedTestsResponseDto;
import com.example.testing_system.dto.user.UserResponseForUserDto;
import com.example.testing_system.model.SolvedTests;
import com.example.testing_system.model.User;
import com.example.testing_system.service.CategoryService;
import com.example.testing_system.service.TestService;
import com.example.testing_system.service.SolvedTestsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovyjarjarantlr4.v4.codegen.model.ModelElement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    ObjectMapper objectMapper;
    CategoryService categoryService;
    TestService testService;
    SolvedTestsService solvedTestsService;

    @GetMapping
    public String get(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if(Objects.nonNull(user)) {
            UserResponseForUserDto userResponseForUserDto = objectMapper.convertValue(user, UserResponseForUserDto.class);
            model.addAttribute("user", userResponseForUserDto);
        }

        List<User> top3UsersByTruePercentageList = solvedTestsService.getTop3UsersByTruePercentage();
        if(!top3UsersByTruePercentageList.isEmpty()) {
            List<UserResponseForUserDto> userResponseForUserDtoList = UserConverter.convertToUserResponseForUserDto(top3UsersByTruePercentageList);
            model.addAttribute("top3UsersByTruePercentageList", userResponseForUserDtoList);
        }

        List<CategoryResponseDto> categories = CategoryConverter.convertToDtoList(categoryService.getTop6());
        model.addAttribute("categories", categories);

        return "views/index";
    }

    @GetMapping("/category")
    public String getAllCategories(
            Model model
    ) {
        List<CategoryResponseDto> categories = CategoryConverter.convertToDtoList(categoryService.getAll());
        model.addAttribute("categories", categories);

        return "views/category";
    }

    @GetMapping("/{categoryName}/ready-to-test")
    public String readyToTest(
            Model model,
            @PathVariable String categoryName
    ) {
        model.addAttribute("categoryName", categoryName);
        return "views/ready-to-test";
    }

    @PostMapping("/test")
    public String getTestsByCategory(
            @ModelElement String categoryName,
            @ModelElement Integer count,
            Model model
    ) {

        List<TestResponseForUserDto> tests = TestConverter.convertToUserResponseDtoList(testService.getTestByCategory(categoryName, count));
        model.addAttribute("tests", tests);

        return "views/test";
    }


    @PostMapping("/solve")
    public String getSolveTests(
        HttpServletRequest request,
        @AuthenticationPrincipal User user,
        Model model
    ) {
        Enumeration<String> parameterNames = request.getParameterNames();
        List<String> answers = new ArrayList<>();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("ans")) {
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues != null && paramValues.length > 0) {
                    answers.add(paramValues[0]);
                }
            }
        }

        if(answers.isEmpty()) {
            model.addAttribute("notSolved", "You have not solved any tests");
            return "views/result";
        }

        SolvedTests solvedTests = solvedTestsService.solveTests(answers, user);
        SolvedTestsResponseDto solvedTestsResponseDto = SolvedTestsConverter.convertToSolvedTestsResponseDto(solvedTests);
        model.addAttribute("solvedTests", solvedTestsResponseDto);

        return "views/result";
    }

}
