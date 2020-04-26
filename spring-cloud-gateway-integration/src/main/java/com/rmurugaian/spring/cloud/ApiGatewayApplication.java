package com.rmurugaian.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(final RouteLocatorBuilder builder) {

        return builder.routes()
                .route("car-service",
                        r -> r.path("/cars/**")
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.addRequestHeader("X-Keycloak-Token", "X-Keycloak-Token-Value"))
                                .uri("lb://car-service")
                )
                .route("employee-service",
                        r -> r.path("/employees/**")
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.addRequestHeader("X-Keycloak-Token", "X-Keycloak-Token-Value"))
                                .uri("lb://employee-service")
                )
                .build();
    }

    @LoadBalanced
    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}