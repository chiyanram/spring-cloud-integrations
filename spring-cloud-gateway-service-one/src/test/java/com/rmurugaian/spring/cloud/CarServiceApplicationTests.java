package com.rmurugaian.spring.cloud;

import com.rmurugaian.spring.cloud.domain.Car;
import com.rmurugaian.spring.cloud.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = CarServiceApplication.class,
        properties = {"spring.cloud.discovery.enabled = false"})
public class CarServiceApplicationTests {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testAddCar() {
        final Car buggy = new Car(UUID.randomUUID(), "ID. BUGGY", LocalDate.of(2022, Month.DECEMBER, 1));

        webTestClient.post().uri("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(buggy), Car.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("ID. BUGGY");
    }

    @Test
    public void testGetAllCars() {
        webTestClient.get().uri("/cars")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Car.class);
    }

    @Test
    public void testDeleteCar() {
        final Car buzzCargo = carRepository.save(new Car(UUID.randomUUID(), "ID. BUZZ CARGO",
                LocalDate.of(2022, Month.DECEMBER, 2))).block();

        webTestClient.delete()
                .uri("/cars/{id}", Collections.singletonMap("id", buzzCargo.getId()))
                .exchange()
                .expectStatus().isOk();
    }
}
