package com.alexm.bearspendings.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author AlexM
 * Date: 4/15/20
 **/
class StoreTest {

    @Test
    void nameIsRequired() {
        assertThrows(NullPointerException.class, () -> Store.builder().build());
    }

}