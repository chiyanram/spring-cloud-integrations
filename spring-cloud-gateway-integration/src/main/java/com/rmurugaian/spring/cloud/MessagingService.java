package com.rmurugaian.spring.cloud;

import reactor.core.publisher.Mono;

public interface MessagingService {
    Mono<String> resolveErrorResponse(final String errorResponse);
}
