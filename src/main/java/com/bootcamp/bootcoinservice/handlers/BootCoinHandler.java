package com.bootcamp.bootcoinservice.handlers;

import com.bootcamp.bootcoinservice.models.dto.BootCoinTransactionDTO;
import com.bootcamp.bootcoinservice.models.entities.BootCoin;
import com.bootcamp.bootcoinservice.services.BootCoinService;
import com.bootcamp.bootcoinservice.services.TransactionBootCoinService;
import com.bootcamp.bootcoinservice.services.YankiDTOService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.bootcamp.bootcoinservice.utils.Constants.BOOTCOIN_PURCHASE;
import static com.bootcamp.bootcoinservice.utils.Constants.BOOTCOIN_SALE;

@Component
@AllArgsConstructor
public class BootCoinHandler {

  private static final Logger log = LoggerFactory.getLogger(BootCoinHandler.class);

  private BootCoinService bootCoinServices;

  private TransactionBootCoinService transactionService;

  private YankiDTOService yankiDTOService;

  public Mono<ServerResponse> buyBootCoin(ServerRequest request) {
    Mono<BootCoin> bootCoinMono = request.bodyToMono(BootCoin.class);
    String phoneNumber = request.pathVariable("phoneNumber");
    return bootCoinMono
          .flatMap(b -> bootCoinServices.findByPhoneNumber(phoneNumber)
                .flatMap(bootCoin ->
                      yankiDTOService.findByPhoneNumber(phoneNumber)
                            .flatMap(yanki -> {
                              if (yanki.getAmount() - b.getAmount() * BOOTCOIN_PURCHASE < 0) {
                                return Mono.empty();
                              }
                              yanki.setAmount(yanki.getAmount() - b.getAmount() * BOOTCOIN_PURCHASE);
                              bootCoin.setAmount(bootCoin.getAmount() + b.getAmount());
                              return yankiDTOService.updateYanki(yanki)
                                    .flatMap(y -> bootCoinServices.update(bootCoin))
                                    .flatMap(y -> {
                                      return transactionService.save(BootCoinTransactionDTO
                                            .builder()
                                            .amount(b.getAmount())
                                            .exchange(b.getAmount() * BOOTCOIN_PURCHASE)
                                            .typeTransaction("BUY")
                                            .phoneNumber(y.getPhoneNumber())
                                            .build());
                                    });
                            })
                ))
          .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c)))
          .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> sellBootCoin(ServerRequest request) {
    Mono<BootCoin> bootCoinMono = request.bodyToMono(BootCoin.class);
    String phoneNumber = request.pathVariable("phoneNumber");
    return bootCoinMono
          .flatMap(b -> bootCoinServices.findByPhoneNumber(phoneNumber)
                .flatMap(bootCoin ->
                      yankiDTOService.findByPhoneNumber(bootCoin.getPhoneNumber())
                            .flatMap(yanki -> {
                              if (bootCoin.getAmount() - b.getAmount() < 0) {
                                return Mono.empty();
                              }
                              yanki.setAmount(yanki.getAmount() + b.getAmount() * BOOTCOIN_SALE);
                              bootCoin.setAmount(bootCoin.getAmount() - b.getAmount());
                              return yankiDTOService.updateYanki(yanki)
                                    .flatMap(y -> bootCoinServices.update(bootCoin)
                                    )
                                    .flatMap(y -> {
                                      return transactionService.save(BootCoinTransactionDTO
                                            .builder()
                                            .amount(b.getAmount())
                                            .exchange(b.getAmount() * BOOTCOIN_SALE)
                                            .typeTransaction("SELL")
                                            .phoneNumber(y.getPhoneNumber())
                                            .build());
                                    });
                            })
                ))

          .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c)))
          .switchIfEmpty(ServerResponse.notFound().build());
  }


  public Mono<ServerResponse> findAll(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(bootCoinServices.findAll(), BootCoin.class);
  }

  public Mono<ServerResponse> findById(ServerRequest request) {
    String bootCoinNumber = request.pathVariable("id");
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(bootCoinServices.findById(bootCoinNumber), BootCoin.class);
  }

  public Mono<ServerResponse> findByPhoneNumber(ServerRequest request) {
    String bootCoinNumber = request.pathVariable("phoneNumber");
    log.info("The Phone Number is " + bootCoinNumber);
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(bootCoinServices.findByPhoneNumber(bootCoinNumber), BootCoin.class);
  }

  public Mono<ServerResponse> newAccountBootCoin(ServerRequest request) {

    Mono<BootCoin> bootCoinMono = request.bodyToMono(BootCoin.class);

    return bootCoinMono.flatMap(bootCoin -> bootCoinServices.createBootCoin(bootCoin))
          .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c))
          ).switchIfEmpty(ServerResponse.badRequest().build());
  }

  public Mono<ServerResponse> updateAccountBootCoin(ServerRequest request) {
    Mono<BootCoin> bootCoinMono = request.bodyToMono(BootCoin.class);
    String id = request.pathVariable("id");

    return bootCoinMono.flatMap(bc -> bootCoinServices.updateBootCoin(id, bc))
          .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c))
          ).switchIfEmpty(ServerResponse.badRequest().build());

  }

  public Mono<ServerResponse> delete(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<BootCoin> accountMono = bootCoinServices.findById(id);
    return accountMono
          .doOnNext(c -> log.info("Delete BootCoin Account", c.getId()))
          .flatMap(c -> bootCoinServices.delete(c).then(ServerResponse.noContent().build()))
          .switchIfEmpty(ServerResponse.notFound().build());
  }
}
