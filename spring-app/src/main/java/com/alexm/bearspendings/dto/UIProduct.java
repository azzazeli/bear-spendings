package com.alexm.bearspendings.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author AlexM
 * Date: 9/17/19
 **/
@Data
@Builder
public class UIProduct { //todo: rename to ProductCommand
    @NotNull(message = "Id is mandatory")
    @Positive(message = "Id be a positive number")
    private Long id;
    @NotNull(message = "Name is mandatory")
    private String name;
    private String comment;
}
