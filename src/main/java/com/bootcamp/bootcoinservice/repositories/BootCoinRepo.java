package com.bootcamp.bootcoinservice.repositories;

import com.bootcamp.bootcoinservice.models.entities.BootCoin;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BootCoinRepo extends ReactiveMongoRepository<BootCoin, String> {

  Mono<BootCoin> findByPhoneNumber(String clientIdNumber);
}
