package com.example.testing_system.validation;

import com.example.testing_system.exception.AlreadyExistsException;
import com.example.testing_system.exception.AuthenticationException;
import com.example.testing_system.model.*;
import com.example.testing_system.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonSchemaValidator {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    TestRepository testRepository;
    CategoryRepository categoryRepository;

    public void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("username or password is incorrect");
        }
    }

    public boolean userExist(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    public boolean isAdmin() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.isEmpty();
    }

    public boolean categoryExist(Category category) {
        return categoryRepository.findCategoryByName(category.getName()).isPresent();
    }

    public boolean testExist(Test test) {
        return testRepository.findTestByQuestion(test.getQuestion()).isPresent();
    }

    public boolean answersIsNotValid(Test test) {
        String[] split = test.getAnswers().split(",");
        Set<String> uniqueElements = new HashSet<>();
        for (String item : split) {
            if (!uniqueElements.add(item) || item.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

}