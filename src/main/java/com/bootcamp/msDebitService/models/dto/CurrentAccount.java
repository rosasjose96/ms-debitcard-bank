package com.bootcamp.msDebitService.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Account.
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccount {

    private String typeOfAccount;

    private String accountNumber;

    private double amount;
}
