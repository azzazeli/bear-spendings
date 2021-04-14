package com.alexm.bearspendings.test;

import com.alexm.bearspendings.entity.Category;

import java.util.Arrays;

/**
 * @author AlexM
 * Date: 2/17/21
 **/
public enum TestCategories {
    HEALTH(2L, "Health"),
    BEBE(   3L, 2L,"Bebe"),
    MISC(4L, "Misc"),
    HOUSEHOLD(5L, "Household"),
    FOOD_AND_DRINK(6L, "Food & Drink"),
    CONSUMABLE(7L, "Consumable"),
    APPA(8L, "Appa"),
    LEGUME(9L, 6L,"Legume"),
    FRUITS(10L, 6L,"Fruits"),
    LACTATE(11L, 6L,"Lactate"),
    SNACKS(12L, "Snacks"),
    DESERT(13L, "Desert"),
    ALCOHOL(14L, "Alcohol"),
    PIINE(15L, "Piine"),
    MISC2(16L, "misc"),
    IAURT_CHEFIR(17L, "Iaurt / Chefir"),
    LAPTE(18L, "Lapte"),
    SMINTINA(19L, 11L,"Smintina");

    Long parentId;
    public final Long id;
    public final String categoryName;

    TestCategories(Long id, String name) {
        this(id, null, name);
    }

    TestCategories(Long id, Long parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.categoryName = name;
    }

    public static Category of(String name) {
        return Arrays.stream(TestCategories.values())
                .filter(cat -> cat.categoryName.equals(name))
                .findFirst()
                .map(testCategory -> Category.builder().id(testCategory.id).name(testCategory.categoryName).build())
                .orElseThrow();
    }

    public Category category() {
        return categoryBuilder(id, categoryName).parent(byId(parentId)).build();
    }

    private Category.CategoryBuilder categoryBuilder(Long catId, String name) {
        return Category.builder().id(catId).name(name);
    }

    private Category byId(Long catId){
        return Arrays.stream(TestCategories.values())
                .filter(testCategory -> testCategory.id.equals(catId))
                .map(this::toCategory)
                .findFirst()
                .orElse(null);
    }

    private Category toCategory(TestCategories testCategory) {
        final Category build = categoryBuilder(testCategory.id, testCategory.categoryName).build();
        if (testCategory.parentId != null) {
            build.setParent(byId(testCategory.parentId));
        }
        return build;
    }
}
