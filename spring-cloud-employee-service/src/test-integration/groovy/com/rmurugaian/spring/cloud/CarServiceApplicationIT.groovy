package com.rmurugaian.spring.cloud


import com.rmurugaian.spring.cloud.domain.Employee
import com.rmurugaian.spring.cloud.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = EmployeeServiceApplication.class,
        properties = ["spring.cloud.discovery.enabled = false"])
class CarServiceApplicationIT extends Specification {

    @Autowired
    private EmployeeRepository carRepository

    @Autowired
    private WebTestClient webTestClient


    def "add card"() {
        given:
        final Employee buggy = new Employee(UUID.randomUUID(), "ID. BUGGY", LocalDate.of(2022, Month.DECEMBER, 1))

        expect:
        webTestClient.post().uri("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(buggy), Employee.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("id").isNotEmpty()
                .jsonPath("name").isEqualTo("ID. BUGGY")
    }

    def "find all cars"() {
        expect:
        webTestClient.get().uri("/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Employee.class)
    }


    def "delete car"() {
        given:
        final Employee buzzCargo = carRepository.save(new Employee(UUID.randomUUID(), "ID. BUZZ CARGO",
                LocalDate.of(2022, Month.DECEMBER, 2))).block()

        expect:
        webTestClient.delete()
                .uri("/employees/{id}", Collections.singletonMap("id", buzzCargo.getId()))
                .exchange()
                .expectStatus().isOk()
    }
}