package com.rmurugaian.spring.cloud.spi;

import reactor.core.publisher.Mono;

public interface MessageServiceProvider {
    Mono<String> resolveErrorResponse(final String errorResponse);
}
