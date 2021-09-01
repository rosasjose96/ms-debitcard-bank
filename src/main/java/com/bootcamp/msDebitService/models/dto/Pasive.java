package com.bootcamp.msDebitService.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pasive {

    @Id
    private String id;
    private String typeOfAccount;
    private String accountNumber;
    private double amount;
}
