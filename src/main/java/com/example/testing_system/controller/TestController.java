package com.example.testing_system.controller;

import com.example.testing_system.convert.CategoryConverter;
import com.example.testing_system.convert.TestConverter;
import com.example.testing_system.dto.category.CategoryResponseDto;
import com.example.testing_system.dto.response.PageResponse;
import com.example.testing_system.dto.test.TestCreateRequestDto;
import com.example.testing_system.dto.test.TestResponseForAdminDto;
import com.example.testing_system.dto.test.TestUpdateRequestDto;
import com.example.testing_system.model.Test;
import com.example.testing_system.service.CategoryService;
import com.example.testing_system.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/test")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    TestService testService;
    CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String get(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<TestResponseForAdminDto> tests = testService.getAllForAdmin(pageNum, pageSize);
        model.addAttribute("tests", tests);

        List<CategoryResponseDto> categories = CategoryConverter.convertToDtoList(categoryService.getAll());
        model.addAttribute("categories", categories);
        return "views/admin/test/index";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String add(
            @ModelAttribute TestCreateRequestDto testCreateRequestDto,
            RedirectAttributes redirectAttributes
    ) {
        Test test = TestConverter.convertToDomain(testCreateRequestDto);

        if(testService.create(test, testCreateRequestDto.getCategoryId())) {
            redirectAttributes.addFlashAttribute("success", "Test created successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Test could not be saved successfully or may already exist!");
        }

        return "redirect:/admin/test";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model
    ) {
        Optional<Test> test = testService.getById(id);
        model.addAttribute("test", test.orElseGet(Test::new));

        List<CategoryResponseDto> categories = CategoryConverter.convertToDtoList(categoryService.getAll());
        model.addAttribute("categories", categories);
        return "views/admin/test/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Integer id,
            @ModelAttribute TestUpdateRequestDto testUpdateRequestDto,
            RedirectAttributes redirectAttributes
    ) {
        Test test = TestConverter.convertToDomain(testUpdateRequestDto);

        if(testService.update(id, test, testUpdateRequestDto.getCategoryId())) {
            redirectAttributes.addFlashAttribute("success", "Test updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Test failed to update!");
        }
        return "redirect:/admin/test";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        if(testService.deleteById(id)) {
            redirectAttributes.addFlashAttribute("success", "Test deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Test failed to delete!");
        }

        return "redirect:/admin/test";
    }

}
