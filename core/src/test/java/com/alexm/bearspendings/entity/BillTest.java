package com.alexm.bearspendings.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 5/6/20
 **/
class BillTest {

    @Test
    void defaultTotal() {
        Bill bill = new Bill();
        assertEquals(0.0, bill.getTotal().doubleValue());
    }

}
