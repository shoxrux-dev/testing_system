package com.example.testing_system.controller;

import com.example.testing_system.convert.CategoryConverter;
import com.example.testing_system.dto.category.CategoryCreateRequestDto;
import com.example.testing_system.dto.category.CategoryResponseDto;
import com.example.testing_system.dto.category.CategoryUpdateRequestDto;
import com.example.testing_system.dto.response.PageResponse;
import com.example.testing_system.model.Category;
import com.example.testing_system.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String get(
            Model model,
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize
    ) {
        PageResponse<CategoryResponseDto> categories = categoryService.getAll(pageNum, pageSize);
        model.addAttribute("categories", categories);
        return "views/admin/category/index";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String add(
            @ModelAttribute CategoryCreateRequestDto categoryCreateRequestDto,
            RedirectAttributes redirectAttributes
    ) {
        Category category = CategoryConverter.convertToDomain(categoryCreateRequestDto);

        if(categoryService.create(category, categoryCreateRequestDto.getImage())) {
            redirectAttributes.addFlashAttribute("success", "Category created successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category could not be saved successfully or may already exist!");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(
            @PathVariable Integer id,
            Model model
    ) {
        Optional<Category> category = categoryService.getById(id);
        model.addAttribute("category", category.orElseGet(Category::new));
        return "views/admin/category/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
            @PathVariable Integer id,
            @ModelAttribute CategoryUpdateRequestDto categoryUpdateRequestDto,
            RedirectAttributes redirectAttributes
    ) {
        Category category = CategoryConverter.convertToDomain(categoryUpdateRequestDto);
        if(categoryService.update(id, category, categoryUpdateRequestDto.getImage())) {
            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category failed to update!");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        if(categoryService.deleteById(id)) {
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category failed to delete!");
        }
        return "redirect:/admin/category";
    }

}
