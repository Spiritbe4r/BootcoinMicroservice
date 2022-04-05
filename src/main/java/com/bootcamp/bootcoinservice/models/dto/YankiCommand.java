package com.bootcamp.bootcoinservice.models.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YankiCommand implements Serializable {

  private static final long serialVersionUID= 5952021849504920188L;

  private String ownerName;

  private String phoneNumber;
}
