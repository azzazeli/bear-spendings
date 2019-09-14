package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.BillItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author AlexM
 */
public interface BillItemRepository extends CrudRepository<BillItem, Long> {

    /**
     * get a page of more recent bill items from a store
     * @param storeId - store id
     * @param pageable - how much bill items to return
     * @return
     */
    @Query("select bi from BillItem bi " +
            "where bi.bill.store.id = ?1 " +
            "order by bi.bill.orderDate desc")
    public List<BillItem> lastItemsWithDistinctProducts(Long storeId, Pageable pageable);
}
