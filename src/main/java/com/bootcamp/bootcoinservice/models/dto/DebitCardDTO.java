package com.bootcamp.bootcoinservice.models.dto;

import lombok.Data;

@Data
public class DebitCardDTO {

  private String pan;

  private String cvv;

  private ClientCommand client;

  private AccountCommand mainAccount;
}
