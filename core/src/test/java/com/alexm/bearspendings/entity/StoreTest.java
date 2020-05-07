package com.alexm.bearspendings.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author AlexM
 * Date: 4/15/20
 **/
public class StoreTest {

    public static final long FIRST_STORE_ID = 1L;

    @Test
    void nameIsRequired() {
        assertThrows(NullPointerException.class, () -> Store.builder().build());
    }

}
