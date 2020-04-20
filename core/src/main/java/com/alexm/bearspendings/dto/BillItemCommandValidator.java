package com.alexm.bearspendings.dto;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author AlexM
 * Date: 9/10/19
 **/
public class BillItemCommandValidator implements ConstraintValidator<ValidBillItemCommand, BillItemCommand> {

    @Override
    public boolean isValid(BillItemCommand value, ConstraintValidatorContext context) {
        return !(Objects.isNull(value.getProductId()) && StringUtils.isEmpty(value.getProductName()));
    }

    @Override
    public void initialize(ValidBillItemCommand constraintAnnotation) {
        // no initialization
    }
}
