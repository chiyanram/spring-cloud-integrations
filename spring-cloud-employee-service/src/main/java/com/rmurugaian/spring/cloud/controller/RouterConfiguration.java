package com.rmurugaian.spring.cloud.controller;

import com.rmurugaian.spring.cloud.handler.EmployeeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    private final EmployeeHandler handler;

    public RouterConfiguration(final EmployeeHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {

        return route()
                .GET("/employees/{id}", accept(APPLICATION_JSON), handler::find)
                .GET("/employees", accept(APPLICATION_JSON), handler::findAll)
                .POST("/employees", handler::save)
                .DELETE("/employees/{id}", handler::delete)
                .build();
    }

}