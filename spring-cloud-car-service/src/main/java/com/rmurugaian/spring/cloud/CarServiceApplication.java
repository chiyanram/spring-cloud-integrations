package com.rmurugaian.spring.cloud;

import com.rmurugaian.spring.cloud.domain.Car;
import com.rmurugaian.spring.cloud.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.UUID;

@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class CarServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CarServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner init(final CarRepository repository) {
        final Car ID = new Car(UUID.randomUUID(), "ID.", LocalDate.of(2019, Month.DECEMBER, 1));
        final Car ID_CROZZ = new Car(UUID.randomUUID(), "ID. CROZZ", LocalDate.of(2021, Month.MAY, 1));
        final Car ID_VIZZION = new Car(UUID.randomUUID(), "ID. VIZZION", LocalDate.of(2021, Month.DECEMBER, 1));
        final Car ID_BUZZ = new Car(UUID.randomUUID(), "ID. BUZZ", LocalDate.of(2021, Month.DECEMBER, 1));
        final Set<Car> vwConcepts = Set.of(ID, ID_BUZZ, ID_CROZZ, ID_VIZZION);

        return args -> {
            repository
                    .deleteAll()
                    .thenMany(
                            Flux
                                    .just(vwConcepts)
                                    .flatMap(repository::saveAll)
                    )
                    .thenMany(repository.findAll())
                    .subscribe(car -> log.info("saving " + car.toString()));
        };
    }
}