package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.repository.CategoryRepository;
import com.alexm.bearspendings.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Category getOrInsert(String... categoryNames) {
        Category category = null;
        for (String name : categoryNames) {
            if (!StringUtils.isEmpty(name)) {
                Category finalCategory = category;
                category = categoryRepository.findOneByName(name).orElseGet(() -> createCategory(name, finalCategory));
            }
        }
        return category;
    }

    private Category createCategory(String name, Category parent) {
        return categoryRepository.save(Category.builder().name(name).parent(parent).build());
    }

    @Override
    public Category defaultCategory() {
        return categoryRepository.findOneByName(Category.DEFAULT)
                .orElseGet(() -> categoryRepository.save(Category.builder().name(Category.DEFAULT).build()));

    }
}
