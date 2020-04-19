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
     */
    @Query(
            value = "select bi2.* " +
                    "from bill_item bi2 " +
                    "inner join (" +
                    "  select bi.product_id, max(bi.id) as id" +
                    "  from bill_item bi " +
                    "  inner join bill b on b.id = bi.bill_id" +
                    "  where b.store_id=?1" +
                    "  group by bi.product_id" +
                    ") res on bi2.id = res.id " +
                    "order by bi2.product_id",
            nativeQuery = true
    )

    List<BillItem> lastItemsWithDistinctProducts(Long storeId, Pageable pageable);
}
