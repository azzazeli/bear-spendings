package com.alexm.bearspendings.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AlexM
 * Date: 9/10/19
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UIBillItemValidator.class)
public @interface ValidUIBillItem {
    String message() default "UiBillItem must contains product id or product name";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}