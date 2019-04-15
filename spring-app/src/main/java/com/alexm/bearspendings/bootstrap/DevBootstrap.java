package com.alexm.bearspendings.bootstrap;

import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author AlexM
 */
@Component
@AllArgsConstructor
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    ProductRepository productRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        productRepository.save(Product.builder().name("Chefir JLS 2.5%").build());
        productRepository.save(Product.builder().name("Chefir JLS 2.5%").build());
        productRepository.save(Product.builder().name("Seminte dovleac").build());
    }
}
