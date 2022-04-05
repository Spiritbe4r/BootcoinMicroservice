package com.bootcamp.bootcoinservice.services;

import com.bootcamp.bootcoinservice.models.dto.YankiDTO;
import reactor.core.publisher.Mono;

public interface YankiDTOService {

  Mono<YankiDTO> findByPhoneNumber(String clientIdNumber);

  Mono<YankiDTO> updateYanki(YankiDTO yankiDocument);
}
