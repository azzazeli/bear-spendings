package com.alexm.bearspendings.dto;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AlexM
 * Date: 11/24/19
 **/
class BillCommandTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("validate total")
    @Test
    void validateTotal() {
        BillCommand billCmd = new BillCommand();
        billCmd.setStoreId(100L);
        billCmd.setOrderDate(LocalDateTime.now());
        billCmd.setTotal(-2.9);
        billCmd.setItems(ImmutableSet.of(BillItemCommand.builder().productId(234L).price(22.2).quantity(1.0).build()));
        Set<ConstraintViolation<BillCommand>> validationResult = validator.validate(billCmd);
        assertNotNull(validationResult);
        assertEquals(1, validationResult.size());
        ConstraintViolation<BillCommand> violation = validationResult.iterator().next();
        assertEquals("Total must be greater than zero", violation.getMessage());
    }

    @DisplayName("validate that builder builds valid objects")
    @Test
    void buildValidation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> BillCommand.builder().total(-20.0).build());
        assertEquals("Provided total:-20.0 is invalid. Total must be positive", exception.getMessage());
    }
}
