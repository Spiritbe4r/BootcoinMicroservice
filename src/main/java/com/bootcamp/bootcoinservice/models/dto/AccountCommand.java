package com.bootcamp.bootcoinservice.models.dto;

import lombok.Data;

@Data
public class AccountCommand {

  private String accountNumber;

  private String typeOfAccount;
}
