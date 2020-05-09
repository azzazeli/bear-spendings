package com.alexm.bearspendings.bootstrap;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillRepository;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author AlexM
 */
@Profile("dev")
@Component
@Slf4j
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ProductRepository productRepository;
    private StoreRepository storeRepository;
    private BillRepository billRepository;

    @Value("#{new Boolean('${com.alexm.bearspendings.initTestData}')}")
    private boolean initWithTestData = false;

    public DevBootstrap(ProductRepository productRepository, StoreRepository storeRepository, BillRepository billRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.billRepository = billRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (initWithTestData && storeRepository.count() <=0 ) {
            initData();
        }
    }

    private void initData() {
        log.debug("Developer bootstrap. Initialize with sample data.");
        initStores();
    }

    private void initBills(Store nr1, List<Product> products) {
        Random random = ThreadLocalRandom.current();

        for (int i=0; i <=random.nextInt(10); i++)  {
            Set<BillItem> items = new HashSet<>();
            for (int j=0; j <=random.nextInt(10) + 3; j++)  {
                items.add(buildBillItem(products.get(random.nextInt(products.size()-1)),
                        (double)Math.round(random.nextDouble()*100*100)/100, (double) random.nextInt(5))
                );
            }
            Bill nr1Bill = Bill.builder().orderDate(LocalDateTime.of(2019, 4, random.nextInt(30) + 1, 1, 1))
                    .store(nr1)
                    .items(items)
                    .build();
            billRepository.save(nr1Bill);
        }
    }

    private BillItem buildBillItem(Product product, Double price, Double quantiy) {
        return BillItem.builder().product(product).pricePerUnit(price).quantity(quantiy).build();
    }

    private void initStores() {
        String chisinauCity = "Chisinau";
        String moldovaCountry = "Moldova";
        storeRepository.save(Store.builder()
                .name("Nr.1")
                .country(moldovaCountry)
                .location("Viaduc")
                .city(chisinauCity)
                .build());
       storeRepository.save(Store.builder()
                .name("Pegas")
                .country(moldovaCountry)
                .location("Creanga")
                .city(chisinauCity)
                .build());
        storeRepository.save(Store.builder()
                .name("Ali Market")
                .country(moldovaCountry)
                .location("Matei Basarab")
                .city(chisinauCity)
                .build());
        storeRepository.save(Store.builder()
                .name("Piata Ciocana")
                .country(moldovaCountry)
                .location("Ciocana")
                .city(chisinauCity)
                .build());
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
