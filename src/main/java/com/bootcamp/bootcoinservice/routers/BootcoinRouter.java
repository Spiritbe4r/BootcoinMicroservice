package com.bootcamp.bootcoinservice.routers;

import com.bootcamp.bootcoinservice.handlers.BootCoinHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class BootcoinRouter {
  @Bean
  public RouterFunction<ServerResponse> routes(BootCoinHandler bootCoinHandler) {

    return route(GET("/api/bootcoin"), bootCoinHandler::findAll)
          .andRoute(GET("/api/bootcoin/{id}"), bootCoinHandler::findById)
          .andRoute(GET("/api/bootcoin/number/{phoneNumber}"), bootCoinHandler::findByPhoneNumber)
          .andRoute(PUT("/api/bootcoin/{id}"), bootCoinHandler::updateAccountBootCoin)
          .andRoute(DELETE("/api/bootcoin/{id}"), bootCoinHandler::delete)
          .andRoute(POST("/api/bootcoin"), bootCoinHandler::newAccountBootCoin)
          .andRoute(POST("/api/bootcoin/buy/{phoneNumber}"), bootCoinHandler::buyBootCoin)
          .andRoute(POST("/api/bootcoin/sell/{phoneNumber}"), bootCoinHandler::sellBootCoin);
  }
}
