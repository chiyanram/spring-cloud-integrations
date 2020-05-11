package com.rmurugaian.spring.cloud.handler;

import com.rmurugaian.spring.cloud.domain.Car;
import com.rmurugaian.spring.cloud.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class CarHandler {

    private final CarRepository repository;

    public CarHandler(final CarRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        final Flux<Car> people = repository.findAll();
        return ok().contentType(APPLICATION_JSON).body(people, Car.class);
    }

    public Mono<ServerResponse> save(final ServerRequest request) {

        return request.bodyToMono(Car.class)
                .flatMap(repository::save)
                .flatMap(it -> ok().body(BodyInserters.fromValue(it)));
    }

    public Mono<ServerResponse> find(final ServerRequest request) {
        final UUID uuid = UUID.fromString(request.pathVariable("id"));
        return repository.findById(uuid)
                .flatMap(person -> ok().contentType(APPLICATION_JSON).bodyValue(person))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(final ServerRequest request) {
        final UUID uuid = UUID.fromString(request.pathVariable("id"));
        return repository.deleteById(uuid)
                .flatMap(aVoid -> ok().build());
    }
}