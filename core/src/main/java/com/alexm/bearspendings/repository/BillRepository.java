package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author AlexM
 */
public interface BillRepository extends PagingAndSortingRepository<Bill,Long> {

    @Override
    @EntityGraph(attributePaths = { "items" })
    Page<Bill> findAll(Pageable pageable);

    @Query("select b from Bill b")
    @EntityGraph(attributePaths = { "store", "items.product.category.parent" })
    Page<Bill> allGraph(Pageable pageable);

}
