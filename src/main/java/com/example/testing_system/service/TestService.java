package com.example.testing_system.service;

import com.example.testing_system.convert.TestConverter;
import com.example.testing_system.dto.response.PageResponse;
import com.example.testing_system.dto.test.TestResponseForAdminDto;
import com.example.testing_system.model.Category;
import com.example.testing_system.model.Test;
import com.example.testing_system.repository.TestRepository;
import com.example.testing_system.validation.CommonSchemaValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestService {
    TestRepository testRepository;
    CategoryService categoryService;
    CommonSchemaValidator commonSchemaValidator;

    public boolean create(Test test, Integer categoryId) {
        Optional<Category> category = categoryService.getById(categoryId);

        if (category.isEmpty() || commonSchemaValidator.testExist(test) || commonSchemaValidator.answersIsNotValid(test)) {
            return false;
        }
        Instant now = Instant.now();

        test.setCategory(category.get());
        test.setCreatedAt(now);
        test.setUpdatedAt(now);

        try {
            testRepository.save(test);
            return true;
        } catch (RuntimeException e) {
            return false;
        }

    }

    public PageResponse<TestResponseForAdminDto> getAllForAdmin(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Test> tests = testRepository.findAll(pageRequest);

        return new PageResponse<>(
                TestConverter.convertToAdminResponseDtoList(tests.getContent()),
                tests.getNumber(),
                tests.getSize(),
                tests.getTotalElements(),
                tests.getTotalPages(),
                tests.isLast()
        );
    }

    public boolean update(Integer id, Test test, Integer categoryId) {
        Optional<Category> category = categoryService.getById(categoryId);
        Optional<Test> test1 = getById(id);

        if (category.isEmpty() || test1.isEmpty() || commonSchemaValidator.answersIsNotValid(test)) {
            return false;
        }
        Instant now = Instant.now();

        test1.get().setCategory(category.get());
        test1.get().setQuestion(test.getQuestion());
        test1.get().setAnswer(test.getAnswer());
        test1.get().setAnswers(test.getAnswers());
        test1.get().setUpdatedAt(now);

        try {
            testRepository.save(test1.get());
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean deleteById(Integer id) {
        Optional<Test> test = getById(id);

        if (test.isPresent()) {
            try {
                testRepository.delete(test.get());
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }
        return false;
    }

    public List<Test> getTestByCategory(String categoryName, int count) {
        Optional<Category> category = categoryService.getByName(categoryName);
        if (category.isEmpty()) {
            return new ArrayList<>();
        }

        List<Test> tests = category.get().getTests();
        Collections.shuffle(tests);
        List<Test> countedTest = tests.subList(0, Math.min(count, tests.size()));

        Collections.sort(countedTest);
        return countedTest;
    }

    public Optional<Test> getById(Integer id) {
        return testRepository.findById(id);
    }

}
