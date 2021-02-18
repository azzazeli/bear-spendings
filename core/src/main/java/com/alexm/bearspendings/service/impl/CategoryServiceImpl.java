package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.repository.CategoryRepository;
import com.alexm.bearspendings.service.CategoryService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author AlexM
 * Date: 2/18/21
 **/
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getOrInsert(@NonNull String categoryName, @NonNull String subCategoryName) {
        log.debug("get or insert category: {} , subcategory: {}", categoryName, subCategoryName);
        return categoryRepository.findOneByName(subCategoryName).orElseGet(() -> createSubcategory(categoryName, subCategoryName));
    }

    @Override
    public Category defaultCategory() {
        return categoryRepository.findOneByName(Category.DEFAULT)
                .orElseGet(() -> categoryRepository.save(Category.builder().name(Category.DEFAULT).build()));

    }

    private Category createSubcategory(String categoryName, String subCategoryName) {
        final Category parentCategory = categoryRepository.findOneByName(categoryName).orElseGet(() ->
                categoryRepository.save(Category.builder().name(categoryName).build()));
        return categoryRepository.save(Category.builder().name(subCategoryName).parent(parentCategory).build());
    }
}
