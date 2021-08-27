package com.bootcamp.msDebitService.models.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * The type Fixed term accound.
 */
@Document(collection = "fixedTermAccound")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class FixedTermAccount {

    private String typeOfAccount;

    private String accountNumber;

    private double amount;


}
