package com.rmurugaian.spring.cloud.controller;

import com.rmurugaian.spring.cloud.domain.Car;
import com.rmurugaian.spring.cloud.repository.CarRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Slf4j
public class CarController {

    private final CarRepository carRepository;

    public CarController(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Car> addCar(@RequestBody final Car car) {
        return carRepository.save(car);
    }

    @GetMapping("/cars")
    public Flux<Car> getCars(@RequestHeader("X-Keycloak-Token") final String keyCloakToken) {
        log.info("TOKEN {} ", keyCloakToken);
        return carRepository.findAll();
    }

    @DeleteMapping("/cars/{id}")
    public Mono<ResponseEntity<Void>> deleteCar(@PathVariable("id") final UUID id) {
        return carRepository.findById(id)
                .flatMap(car -> carRepository.delete(car)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/cars/error")
    public Mono<ResponseEntity<ErrorResponse>> error() {

        return Mono.just(ResponseEntity.badRequest().body(new ErrorResponse("TEST-ERROR", "value")));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class ErrorResponse {
        private String errorCode;
        private String desc;
    }
}