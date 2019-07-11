package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.repository.BillRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author AlexM created on 7/11/19
 */
@RunWith(MockitoJUnitRunner.class)
public class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    private BillServiceImpl billService;

    @Before
    public void setup() {
        billService = new BillServiceImpl(billRepository);
    }

    @Test
    public void allBills() {
        when(billRepository.findAll()).thenReturn(new LinkedList<>());
        final List<Bill> bills = billService.allBills();
        assertNotNull(bills);
    }

}