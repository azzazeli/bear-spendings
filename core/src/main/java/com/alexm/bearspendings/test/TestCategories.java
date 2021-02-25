package com.alexm.bearspendings.test;

import com.alexm.bearspendings.entity.Category;

import java.util.Arrays;

/**
 * @author AlexM
 * Date: 2/17/21
 **/
public enum TestCategories {
    HEALTH(2L, "Health"),
    BEBE(   3L, "Bebe"),
    MISC(4L, "Misc"),
    HOUSEHOLD(5L, "Household"),
    FOOD_AND_DRINK(6L, "Food & Drink"),
    CONSUMABLE(7L, "Consumable"),
    APPA(8L, "Appa"),
    LEGUME(9L, "Legume"),
    FRUITS(10L, "Fruits"),
    LACTATE(11L, "Lactate"),
    SNACKS(12L, "Snacks"),
    DESERT(13L, "Desert"),
    ALCOHOL(14L, "Alcohol"),
    PIINE(15L, "Piine"),
    MISC2(16L, "misc");

    public final Long id;
    public final String categoryName;

    TestCategories(Long id, String name) {
        this.id = id;
        this.categoryName = name;
    }

    public static Category of(String subCategoryName) {
        return Arrays.stream(TestCategories.values())
                .filter(cat -> cat.categoryName.equals(subCategoryName))
                .findFirst()
                .map(testCategory -> Category.builder().id(testCategory.id).name(testCategory.categoryName).build())
                .orElseThrow();
    }

    public Category category() {
        return Category.builder().id(this.id).name(this.categoryName).build();
    }
}
