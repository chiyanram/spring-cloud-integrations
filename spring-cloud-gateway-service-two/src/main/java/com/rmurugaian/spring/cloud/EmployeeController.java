package com.rmurugaian.spring.cloud;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> addEmployee(@RequestBody final Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees")
    public Flux<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @DeleteMapping("/employees/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployee(@PathVariable("id") final UUID id) {
        return employeeRepository.findById(id)
                .flatMap(employee -> employeeRepository.delete(employee)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}