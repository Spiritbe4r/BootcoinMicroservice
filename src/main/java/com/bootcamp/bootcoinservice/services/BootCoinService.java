package com.bootcamp.bootcoinservice.services;

import com.bootcamp.bootcoinservice.models.entities.BootCoin;
import reactor.core.publisher.Mono;

public interface BootCoinService extends CrudService<BootCoin, String> {

  Mono<BootCoin> createBootCoin(BootCoin bootCoinDocument);

  Mono<BootCoin> updateBootCoin(String id, BootCoin bootCoinDocument);

  Mono<BootCoin> findByPhoneNumber(String phoneNumber);

  Mono<BootCoin> buyBootCoin(BootCoin bootCoin);

}
