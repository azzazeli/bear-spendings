package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Bill;
import org.springframework.data.repository.CrudRepository;

/**
 * @author AlexM
 */
public interface BillRepository extends CrudRepository<Bill,Long> {
}
