package com.rmurugaian.spring.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DefaultMessagingService implements MessagingService {

    private final WebClient.Builder messageServiceClient;

    public DefaultMessagingService(final WebClient.Builder messageServiceClient) {
        this.messageServiceClient = messageServiceClient;
    }

    @Override
    public Mono<String> resolveErrorResponse(final String errorResponse) {
        return messageServiceClient.build()
                .post()
                .uri("lb://message-service/messages")
                .body(BodyInserters.fromValue(errorResponse))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(it -> log.error("Response recieved >>>>>> {} ", it));
    }
}
