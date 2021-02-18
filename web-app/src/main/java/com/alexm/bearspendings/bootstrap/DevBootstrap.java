package com.alexm.bearspendings.bootstrap;

import com.alexm.bearspendings.service.CategoryService;
import com.alexm.bearspendings.service.StoreService;
import com.alexm.bearspendings.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author AlexM
 */
@Profile("dev")
@Component
@Slf4j
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final StoreService storeService;
    private final UnitOfMeasureService unitOfMeasureService;
    private final CategoryService categoryService;

    public DevBootstrap(StoreService storeService, UnitOfMeasureService unitOfMeasureService, CategoryService categoryService) {
        this.storeService = storeService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.categoryService = categoryService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        unitOfMeasureService.defaultUnit();
        storeService.defaultStore();
        categoryService.defaultCategory();
    }
}
