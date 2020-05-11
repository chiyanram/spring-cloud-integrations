package com.rmurugaian.spring.cloud.filter;


import com.rmurugaian.spring.cloud.spi.MessageServiceProvider;
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
public class DownstreamErrorResponseFilter implements GlobalFilter, Ordered {

    private final MessageServiceProvider messagingService;

    public DownstreamErrorResponseFilter(final MessageServiceProvider messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        final ServerHttpResponse originalResponse = exchange.getResponse();
        final DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        final ServerHttpResponseDecorator decoratedResponse =
                new ErrorResponseResolveDecorator(originalResponse, bufferFactory, messagingService);
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private static class ErrorResponseResolveDecorator extends ServerHttpResponseDecorator {
        private final ServerHttpResponse originalResponse;
        private final DataBufferFactory bufferFactory;
        private final MessageServiceProvider messagingService;

        ErrorResponseResolveDecorator(
                final ServerHttpResponse delegate,
                final DataBufferFactory bufferFactory,
                final MessageServiceProvider messagingService) {

            super(delegate);
            this.originalResponse = delegate;
            this.bufferFactory = bufferFactory;
            this.messagingService = messagingService;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {
            final HttpStatus statusCode = getStatusCode();
            if (statusCode.isError()) {
                if (body instanceof Flux) {
                    final Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    final Flux<DataBuffer> responseFlux =
                            fluxBody
                                    .map(DownstreamErrorResponseFilter::readAsString)
                                    .flatMap(messagingService::resolveErrorResponse)
                                    .map(String::getBytes)
                                    .map(bufferFactory::wrap);
                    
                    return super.writeWith(responseFlux);
                }
            }
            return originalResponse.writeWith(body); // if body is not a flux. never got there.
        }
    }

    private static String readAsString(final DataBuffer dataBuffer) {
        final byte[] content = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(content);
        return new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
