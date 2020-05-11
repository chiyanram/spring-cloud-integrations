package com.rmurugaian.spring.cloud

import com.rmurugaian.spring.cloud.domain.Car
import com.rmurugaian.spring.cloud.repository.CarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = CarServiceApplication.class,
        properties = ["spring.cloud.discovery.enabled = false"])
class CarServiceApplicationIT extends Specification {

    @Autowired
    private CarRepository carRepository

    @Autowired
    private WebTestClient webTestClient


    def "add card"() {
        given:
        final Car buggy = new Car(UUID.randomUUID(), "ID. BUGGY", LocalDate.of(2022, Month.DECEMBER, 1))

        expect:
        webTestClient.post().uri("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(buggy), Car.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("id").isNotEmpty()
                .jsonPath("name").isEqualTo("ID. BUGGY")
    }

    def "find all cars"() {
        expect:
        webTestClient.get().uri("/cars")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Car.class)
    }


    def "delete car"() {
        given:
        final Car buzzCargo = carRepository.save(new Car(UUID.randomUUID(), "ID. BUZZ CARGO",
                LocalDate.of(2022, Month.DECEMBER, 2))).block()

        expect:
        webTestClient.delete()
                .uri("/cars/{id}", Collections.singletonMap("id", buzzCargo.getId()))
                .exchange()
                .expectStatus().isOk()
    }
}