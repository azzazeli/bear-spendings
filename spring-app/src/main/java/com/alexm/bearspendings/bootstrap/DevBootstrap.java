package com.alexm.bearspendings.bootstrap;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillItemRepository;
import com.alexm.bearspendings.repository.BillRepository;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author AlexM
 */
@Component
@AllArgsConstructor
@Slf4j
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    ProductRepository productRepository;
    StoreRepository storeRepository;
    BillItemRepository billItemRepository;
    BillRepository billRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (productRepository.count() <=0) {
            initData();
        }
    }

    private void initData() {
        log.debug("Developer bootstrap. Initialize with sample data.");
        final Store nr1 = Store.builder().name("Nr.1").build();
        storeRepository.save(nr1);

        final Product chefir = Product.builder().name("Chefir JLS 2.5%").build();
        productRepository.save(chefir);
        productRepository.save(Product.builder().name("Chefir JLS 2.5%").build());
        productRepository.save(Product.builder().name("Seminte dovleac").build());

        BillItem chefirBi = BillItem.builder().product(chefir).price(7.85).quantity(1).build();
        billItemRepository.save(chefirBi);
        Bill nr1Bill = Bill.builder()
                .orderDate(LocalDate.of(2019, 4, 15))
                .store(nr1)
                .item(chefirBi)
                .build();
        billRepository.save(nr1Bill);
    }
}
