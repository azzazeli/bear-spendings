package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Category;

/**
 * @author AlexM
 * Date: 2/17/21
 **/
public enum TestImportCategories {
    HEALTH(1L, "Health"),
    BEBE(2L, "Bebe");

    public final Long id;
    public final String categoryName;

    TestImportCategories(Long id, String name) {
        this.id = id;
        this.categoryName = name;
    }

    public Category category() {
        return Category.builder().id(this.id).name(this.categoryName).build();
    }
}
