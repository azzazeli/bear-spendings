package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Bill;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author AlexM
 */
public interface BillRepository extends PagingAndSortingRepository<Bill,Long> {
}
