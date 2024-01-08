package com.example.testing_system.service;

import com.example.testing_system.constant.FilePathConstant;
import com.example.testing_system.convert.CategoryConverter;
import com.example.testing_system.dto.category.CategoryResponseDto;
import com.example.testing_system.dto.response.PageResponse;
import com.example.testing_system.model.Category;
import com.example.testing_system.repository.CategoryRepository;
import com.example.testing_system.util.FileUtil;
import com.example.testing_system.validation.CommonSchemaValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CommonSchemaValidator commonSchemaValidator;

    public boolean create(Category category, MultipartFile file) {
        if(commonSchemaValidator.categoryExist(category)) {
            return false;
        }
        if(file != null && !file.isEmpty()){
            String fileName = FileUtil.uploadImage(file, FilePathConstant.CATEGORY);
            category.setImage(fileName);
        }
        Instant now = Instant.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        try {
            categoryRepository.save(category);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public PageResponse<CategoryResponseDto> getAll(int pageNum, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageRequest);

        return new PageResponse<>(
                CategoryConverter.convertToDtoList(categories.getContent()),
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isLast()
        );
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public List<Category> getTop6() {
        return categoryRepository.findTop6ByOrderById();
    }

    public boolean update(Integer id, Category category, MultipartFile file) {
        Optional<Category> category1 = getById(id);

        if(category1.isPresent()) {
            if(!file.isEmpty()){
                if (FileUtil.deleteImage(category1.get().getImage(), FilePathConstant.CATEGORY)) {
                    String fileName = FileUtil.uploadImage(file, FilePathConstant.CATEGORY);
                    category1.get().setImage(fileName);
                } else {
                    return false;
                }
            }
            category1.get().setName(category.getName());
            category1.get().setUpdatedAt(Instant.now());
            try {
                categoryRepository.save(category1.get());
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }
        return false;
    }

    public boolean deleteById(Integer id) {
        Optional<Category> category = getById(id);

        if(category.isPresent()) {
            if(FileUtil.deleteImage(category.get().getImage(), FilePathConstant.CATEGORY)) {
                try {
                    categoryRepository.delete(category.get());
                    return true;
                } catch (RuntimeException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public Optional<Category> getByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    public Optional<Category> getById(Integer id) {
        return categoryRepository.findById(id);
    }

}
