package com.rmurugaian.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route("car-service", r -> r.path("/cars")
                        .uri("lb://car-service"))
                .route("employee-service", r -> r.path("/employees")
                        .uri("lb://employee-service"))
                .build();
    }
}