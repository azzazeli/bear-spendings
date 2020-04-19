package com.alexm.bearspendings.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 9/17/19
 **/
class UIProductTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validate() {
        UIProduct uiProduct = UIProduct.builder().build();
        Set<ConstraintViolation<UIProduct>> constraintViolations = validator.validate(uiProduct);
        assertEquals(2, constraintViolations.size());
        List<String> errorMsgs = constraintViolations.stream().map(constraint -> constraint.getMessage()).collect(Collectors.toList());
        assertThat(errorMsgs, containsInAnyOrder("Id is mandatory", "Name is mandatory"));
    }
}