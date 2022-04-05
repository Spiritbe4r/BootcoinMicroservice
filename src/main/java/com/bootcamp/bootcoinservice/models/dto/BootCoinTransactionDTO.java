package com.bootcamp.bootcoinservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BootCoinTransactionDTO {

  private String typeTransaction;

  private String phoneNumber;

  private Double amount;

  private Double exchange;
}
