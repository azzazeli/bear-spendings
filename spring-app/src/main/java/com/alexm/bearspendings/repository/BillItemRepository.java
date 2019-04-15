package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.BillItem;
import org.springframework.data.repository.CrudRepository;

/**
 * @author AlexM
 */
public interface BillItemRepository extends CrudRepository<BillItem, Long> {
}
