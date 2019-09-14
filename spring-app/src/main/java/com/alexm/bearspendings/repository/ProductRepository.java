package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author AlexM
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String productName);

    /**
     * Return a list of product from requested store. <br/>
     * Implementation is very simple - and need to be enhanced according to method name (top store products) <br/>
     * Enhancements will be part of future version > 0.2
     * @param storeId - store id
     * @param page - Pageable with size of page
     * @return
     */
    @Query("select prod from Product prod where prod.id in ( " +
                "select p.id from Product p " +
                "left join p.billItems bitems " +
                "where bitems.bill.store.id=?1 " +
            ")"
    )
    List<Product> topByStore(Long storeId, Pageable page);
}
