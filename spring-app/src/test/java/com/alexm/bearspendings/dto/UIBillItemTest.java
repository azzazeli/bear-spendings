package com.alexm.bearspendings.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 9/9/19
 **/
class UIBillItemTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("Validate that product id can be null")
    @Test
    void validateProductId() {
        UIBillItem uiBillItem = UIBillItem.builder().quantity(1.0).productName("Peste").price(2.9).build();
        assertValidUiBillItem(validator.validate(uiBillItem));
    }

    @DisplayName("BillItem product must contains product id or product name")
    @Test
    void validateProductInfo() {
        assertValidUiBillItem(validator.validate(UIBillItem.builder().productId(1L).quantity(1.0).price(2.2).build()));
        assertValidUiBillItem(validator.validate(UIBillItem.builder().productName("Lapte").quantity(1.0).price(2.2).build()));
        UIBillItem invalid = UIBillItem.builder().quantity(1.0).price(2.2).build();
        Set<ConstraintViolation<UIBillItem>> constraintViolations = validator.validate(invalid);
        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<UIBillItem> uiBillItemConstraintViolation = constraintViolations.iterator().next();
        assertEquals("UiBillItem must contains product id or product name", uiBillItemConstraintViolation.getMessage());
    }

    void assertValidUiBillItem(Set<ConstraintViolation<UIBillItem>> constraintViolations) {
        constraintViolations.forEach(uiBillItemConstraintViolation -> System.out.println(uiBillItemConstraintViolation.getMessage()));
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}