package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.BillItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 4/5/20
 **/
@DataJpaTest
class BillItemRepositoryTest {

    @Autowired
    BillItemRepository billItemRepository;

    @Test
    void lastItemsWithDistinctProducts() {
        final List<BillItem> billItems = billItemRepository.lastItemsWithDistinctProducts(1L, PageRequest.of(0, 20));
        assertEquals(3, billItems.size());
        assertThat(billItems).extracting(billItem -> billItem.getProduct().getId())
                .containsExactly(1L, 2L, 3L);
    }
}