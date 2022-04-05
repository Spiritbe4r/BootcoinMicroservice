package com.bootcamp.bootcoinservice.services;

import com.bootcamp.bootcoinservice.models.dto.BootCoinTransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionBootCoinService {

  Mono<BootCoinTransactionDTO> save(BootCoinTransactionDTO transactionDTO);
}
