package com.bootcamp.bootcoinservice.services.impl;

import com.bootcamp.bootcoinservice.models.dto.YankiCommand;
import com.bootcamp.bootcoinservice.models.entities.BootCoin;
import com.bootcamp.bootcoinservice.repositories.BootCoinRepo;
import com.bootcamp.bootcoinservice.services.BootCoinService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.bootcamp.bootcoinservice.utils.Constants.BOOTCOIN_PURCHASE;

@Service
@AllArgsConstructor
public class BootCoinServiceImpl implements BootCoinService {

  private static final Logger log = LoggerFactory.getLogger(BootCoinServiceImpl.class);

  private final RedisTemplate redisTemplate;

  private final ReactiveRedisOperations<String, BootCoin> operations;

  private final YankiDTOServiceImpl service;

  private final BootCoinRepo repository;

  @Override
  public Mono<BootCoin> findByBootcoinById(String id) {

    // Obtener información el bootcoin del caché
    String key = "bootcoin_" + id;
    ValueOperations<String, BootCoin> operations = redisTemplate.opsForValue();

    // existe caché
    boolean hasKey = redisTemplate.hasKey(key);
    if (hasKey) {
      BootCoin bootCoin = operations.get(key);

      log.info("BootCoinServiceImpl.findByBootcoinById : obtuvo el bootcoin del caché  >> " + bootCoin.toString());
      return Mono.create(bootcoinMonoSink -> bootcoinMonoSink.success(bootCoin));
    }

    // Obtener información del tipo de bootcoin de MongoDB
    Mono<BootCoin> bootcoinMono = repository.findById(id);

    if (bootcoinMono == null)
      return bootcoinMono;

    // insertar búfer
    bootcoinMono.subscribe(bootcoinObj -> {
      operations.set(key, bootcoinObj);
      log.info("CityHandler.findCityById() : clientType insertar caché >> " + bootcoinObj.toString());
    });

    return bootcoinMono;
  }


  @Override
  public Mono<BootCoin> createBootCoin(BootCoin bootCoinDocument) {
    return service.findByPhoneNumber(bootCoinDocument.getPhoneNumber())
          .flatMap(c -> {
            if (c.getOwnerName() == null) {
              log.info("Required Yanki Account");
              return Mono.just(BootCoin.builder().build());
            }
            bootCoinDocument.setTypeOfAccount("BOOT_COIN");
            bootCoinDocument.setYanki(YankiCommand
                  .builder()
                  .phoneNumber(c.getPhoneNumber())
                  .ownerName(c.getOwnerName())
                  .build());
            c.setAmount(c.getAmount() - bootCoinDocument.getAmount() * BOOTCOIN_PURCHASE);
            return service.updateYanki(c).flatMap(d -> repository.save(bootCoinDocument));
          });
  }

  @Override
  public Mono<BootCoin> updateBootCoin(String id, BootCoin bootCoinDocument) {
    return repository.findById(id).flatMap(c -> {
      c.setAmount(bootCoinDocument.getAmount());
      return repository.save(c);
    });
  }

  @Override
  public Mono<BootCoin> findByPhoneNumber(String phoneNumber) {
    return repository.findByPhoneNumber(phoneNumber);
  }

  @Override
  public Mono<BootCoin> create(BootCoin obj) {

    return repository.save(obj);

  }

  @Override
  public Flux<BootCoin> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<BootCoin> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<BootCoin> update(BootCoin obj) {
    return repository.save(obj);
  }

  @Override
  public Mono<Void> delete(BootCoin obj) {
    return repository.delete(obj);
  }

  public Boolean validateBootcoin(BootCoin obj) {
    return true;
  }

  @Override
  public Mono<BootCoin> buyBootCoin(BootCoin bootCoin) {
    return null;
  }
}
