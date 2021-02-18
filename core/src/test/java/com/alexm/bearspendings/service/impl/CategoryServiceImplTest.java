package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author AlexM
 * Date: 2/18/21
 **/
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;

    String healthCategoryName = "Health";
    String babeCategoryName = "Babe";

    @Test
    void getOrInsert() {
        doReturn(Optional.empty()).when(categoryRepository).findOneByName(healthCategoryName);
        doReturn(healthCategory()).when(categoryRepository).save(category(healthCategoryName));
        doReturn(Optional.empty()).when(categoryRepository).findOneByName(babeCategoryName);
        doReturn(category(babeCategoryName, 2L, healthCategory())).when(categoryRepository).save(category(babeCategoryName));

        final Category subCategory = categoryService.getOrInsert(healthCategoryName, babeCategoryName);

        verify(categoryRepository, times(1)).findOneByName(healthCategoryName);
        verify(categoryRepository, times(1)).save(category(healthCategoryName));
        verify(categoryRepository, times(1)).findOneByName(babeCategoryName);
        verify(categoryRepository, times(1)).save(category(babeCategoryName));
        assertNotNull(subCategory);
        assertEquals(healthCategory(), subCategory.getParent());

    }

    private Category healthCategory() {
        return category(healthCategoryName, 1L);
    }

    private Category category(String name) {
        return category(name, null, null);
    }

    private Category category(String name, Long id) {
        return category(name, null, null);
    }

    private Category category(String name, Long id, Category parent) {
        return Category.builder().name(name).id(id).parent(parent).build();
    }
}

