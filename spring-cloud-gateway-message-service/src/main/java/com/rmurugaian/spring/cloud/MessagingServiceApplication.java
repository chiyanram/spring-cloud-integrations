package com.rmurugaian.spring.cloud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@EnableDiscoveryClient
@SpringBootApplication
public class MessagingServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MessagingServiceApplication.class);
    }

}

@Slf4j
@RestController
class MessageRestController {

    @PostMapping("/messages")
    public Mono<ErrorResponse> message(@RequestBody final String response) {
        log.error(">>>>>>>>>>>>>>>>>>>>> {} ", response);
        return Mono.just(new ErrorResponse("Name should not be empty"));
    }

}

@Getter
@AllArgsConstructor
@NoArgsConstructor
class ErrorResponse {

    private String message;

}