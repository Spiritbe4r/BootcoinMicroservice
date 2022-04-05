package com.bootcamp.bootcoinservice.services.impl;

import com.bootcamp.bootcoinservice.models.dto.YankiDTO;
import com.bootcamp.bootcoinservice.services.YankiDTOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class YankiDTOServiceImpl implements YankiDTOService {

  @Autowired
  private WebClient webClient;

  private static final Logger log = LoggerFactory.getLogger(YankiDTOServiceImpl.class);

  @Override
  public Mono<YankiDTO> updateYanki(YankiDTO yanki) {
    return webClient
          .put()
          .uri("/amount/{id}", yanki.getId())
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(yanki)
          .retrieve()
          .bodyToMono(YankiDTO.class);
  }

  @Override
  public Mono<YankiDTO> findByPhoneNumber(String phoneNumber) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("phoneNumber", phoneNumber);
    return webClient
          .get()
          .uri("/phone/{phoneNumber}", params)
          .accept(MediaType.APPLICATION_JSON)
          .exchangeToMono(clientResponse -> clientResponse.bodyToMono(YankiDTO.class))
          .doOnNext(c -> log.info("Phone Number Response: Phone number={}", c.getPhoneNumber()))
          .doOnNext(c -> log.info("SavingAccount Response: Account Amounth={}", c.getOwnerName()));
  }
}
