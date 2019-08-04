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

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        Store nr1 = initStore();
        List<Product> products = initProducts();
        Random random = ThreadLocalRandom.current();

        for (int i=0; i <=random.nextInt(10); i++)  {
            List<BillItem> items = new LinkedList<>();
            for (int j=0; j <=random.nextInt(10) + 3; j++)  {
                items.add(buildBillItem(products.get(random.nextInt(products.size()-1)),
                        (double)Math.round(random.nextDouble()*100*100)/100, random.nextInt(5))
                );
            }
            Bill nr1Bill = Bill.builder().orderDate(LocalDateTime.of(2019, 4, random.nextInt(30) + 1, 1, 1))
                    .store(nr1)
                    .items(items)
                    .build();
            billRepository.save(nr1Bill);
        }
    }

    private BillItem buildBillItem(Product product, Double price, Integer quantiy) {
        BillItem item = BillItem.builder().product(product).price(price).quantity(quantiy).build();
        return billItemRepository.save(item);
    }

    private Store initStore() {
        final Store nr1 = Store.builder().name("Nr.1").build();
        return storeRepository.save(nr1);
    }

    private List<Product> initProducts() {
        List<Product> generated = new LinkedList<>();
        final Product chefir = Product.builder().name("Chefir JLS 2.5%").build();
        final Product cascaval = Product.builder().name("Cascaval Masdam").build();
        generated.add(productRepository.save(chefir));
        generated.add(productRepository.save(cascaval));
        generated.add(productRepository.save(Product.builder().name("Chefir JLS 2.5%").build()));
        generated.add(productRepository.save(Product.builder().name("Seminte dovleac").build()));
        return generated;
    }
}
