package com.rmurugaian.spring.cloud;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, UUID> {
}