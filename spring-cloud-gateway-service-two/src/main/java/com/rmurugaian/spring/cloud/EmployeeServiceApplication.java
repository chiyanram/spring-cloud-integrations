package com.rmurugaian.spring.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.UUID;

@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveMongoRepositories
@Slf4j
public class EmployeeServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner init(final EmployeeRepository repository) {
        // Electric VWs from https://www.vw.com/electric-concepts/
        // Release dates from https://www.motor1.com/features/346407/volkswagen-id-price-on-sale/
        final Employee ID = new Employee(UUID.randomUUID(), "ID.", LocalDate.of(2019, Month.DECEMBER, 1));
        final Employee ID_CROZZ = new Employee(UUID.randomUUID(), "ID. CROZZ", LocalDate.of(2021, Month.MAY, 1));
        final Employee ID_VIZZION = new Employee(UUID.randomUUID(), "ID. VIZZION", LocalDate.of(2021, Month.DECEMBER, 1));
        final Employee ID_BUZZ = new Employee(UUID.randomUUID(), "ID. BUZZ", LocalDate.of(2021, Month.DECEMBER, 1));
        final Set<Employee> vwConcepts = Set.of(ID, ID_BUZZ, ID_CROZZ, ID_VIZZION);

        return args -> {
            repository
                    .deleteAll()
                    .thenMany(
                            Flux
                                    .just(vwConcepts)
                                    .flatMap(repository::saveAll)
                    )
                    .thenMany(repository.findAll())
                    .subscribe(employee -> log.info("saving " + employee.toString()));
        };
    }
}