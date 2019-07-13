package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author AlexM created on 7/11/19
 */
@ExtendWith(MockitoExtension.class)
public class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    private BillServiceImpl billService;

    @BeforeEach
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