package com.rmurugaian.spring.cloud;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class ErrorResponseFilter implements GlobalFilter, Ordered {

    private final MessagingService messagingService;

    public ErrorResponseFilter(final MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        final ServerHttpResponse originalResponse = exchange.getResponse();
        final DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        final ServerHttpResponseDecorator decoratedResponse = errorResponseDecorator(originalResponse, bufferFactory);
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private ServerHttpResponseDecorator errorResponseDecorator(final ServerHttpResponse originalResponse, final DataBufferFactory bufferFactory) {
        return new ServerHttpResponseDecorator(originalResponse) {
            @SuppressWarnings("unchecked")
            @Override
            public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {
                final HttpStatus statusCode = getStatusCode();
                if (statusCode.isError()) {
                    if (body instanceof Flux) {
                        final Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.flatMap(dataBuffer -> {
                            // probably should reuse buffers
                            final byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            return messagingService.resolveErrorResponse(new String(content, StandardCharsets.UTF_8))
                                    .map(String::getBytes)
                                    .map(bufferFactory::wrap);
                        }));
                    }
                }
                return originalResponse.writeWith(body); // if body is not a flux. never got there.
            }
        };
    }

    private String readAsString(final DataBuffer dataBuffer) {
        final byte[] content = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(content);
        return new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
