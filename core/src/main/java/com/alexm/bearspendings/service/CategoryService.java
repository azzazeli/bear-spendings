package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Category;

/**
 * @author AlexM
 * Date: 2/17/21
 **/
public interface CategoryService {
    /**
     * Create category hierarchy category -> subcategory -> subSubcategory.
     * @return subcategory
     */
    Category getOrInsert(String ...categoryName);

    Category defaultCategory();
}
