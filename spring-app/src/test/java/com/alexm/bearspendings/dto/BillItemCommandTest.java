package com.alexm.bearspendings.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author AlexM
 * Date: 9/9/19
 **/
class BillItemCommandTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("Validate that product id can be null")
    @Test
    void validateProductId() {
        BillItemCommand billItemCommand = BillItemCommand.builder().quantity(1.0).productName("Peste").price(2.9).build();
        assertValidUiBillItem(validator.validate(billItemCommand));
    }

    @DisplayName("BillItem product must contains product id or product name")
    @Test
    void validateProductInfo() {
        assertValidUiBillItem(validator.validate(BillItemCommand.builder().productId(1L).quantity(1.0).price(2.2).build()));
        assertValidUiBillItem(validator.validate(BillItemCommand.builder().productName("Lapte").quantity(1.0).price(2.2).build()));
        BillItemCommand invalid = BillItemCommand.builder().quantity(1.0).price(2.2).build();
        Set<ConstraintViolation<BillItemCommand>> constraintViolations = validator.validate(invalid);
        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<BillItemCommand> uiBillItemConstraintViolation = constraintViolations.iterator().next();
        assertEquals("UiBillItem must contains product id or product name", uiBillItemConstraintViolation.getMessage());
    }

    @Test
    void builderPerformValidation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> BillItemCommand.builder().productId(1L).quantity(-1.0).price(2.2).build());
        assertEquals("Provided quantity:-1.0 is invalid. Quantity must be positive", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class,
                () -> BillItemCommand.builder().productId(1L).quantity(1.0).price(-2.2).build());
        assertEquals("Provided price:-2.2 is invalid. Price must be positive", exception.getMessage());
    }

    void assertValidUiBillItem(Set<ConstraintViolation<BillItemCommand>> constraintViolations) {
        constraintViolations.forEach(uiBillItemConstraintViolation -> System.out.println(uiBillItemConstraintViolation.getMessage()));
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}