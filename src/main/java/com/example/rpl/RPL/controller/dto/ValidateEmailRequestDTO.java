package com.example.rpl.RPL.controller.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ValidateEmailRequestDTO {

    @NotNull
    private String validateEmailToken;
}
