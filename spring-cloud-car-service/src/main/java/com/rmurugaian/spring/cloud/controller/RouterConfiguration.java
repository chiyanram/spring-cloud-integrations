package com.rmurugaian.spring.cloud.controller;

import com.rmurugaian.spring.cloud.handler.CarHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    private final CarHandler handler;

    public RouterConfiguration(final CarHandler handler) {
        this.handler = handler;
    }


    @Bean
    public RouterFunction<ServerResponse> routerFunction() {

        return route()
                .GET("/cars/{id}", accept(APPLICATION_JSON), handler::find)
                .GET("/cars", accept(APPLICATION_JSON), handler::findAll)
                .POST("/cars", handler::save)
                .DELETE("/cars/{id}",handler::delete)
                .build();
    }

}