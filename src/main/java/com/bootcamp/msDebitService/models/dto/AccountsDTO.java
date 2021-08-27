package com.bootcamp.msDebitService.models.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountsDTO {

   private String numberOfAccount;
   private String typeOfAccount;
   private int order;
}
