package com.bootcamp.bootcoinservice.services.impl;

import com.bootcamp.bootcoinservice.models.dto.BootCoinTransactionDTO;
import com.bootcamp.bootcoinservice.services.TransactionBootCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TransactionBootCoinServiceImpl implements TransactionBootCoinService {

    @Autowired
    private WebClient.Builder client;

    @Value("${microservices-urls.api-bootcoin-transac}")
    private String apiBootcointransac;
    @Override
    public Mono<BootCoinTransactionDTO> save(BootCoinTransactionDTO transactionDTO) {
        return  client
                .build()
                .post()
                .uri(apiBootcointransac)
                .body(Mono.just(transactionDTO), BootCoinTransactionDTO.class)
                .retrieve()
                .bodyToMono(BootCoinTransactionDTO.class);
    }
}
