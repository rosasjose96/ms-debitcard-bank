package com.bootcamp.msDebitService.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pasive {

    private String id;
    private String typeOfAccount;

    private String accountNumber;

    private double amount;
}
