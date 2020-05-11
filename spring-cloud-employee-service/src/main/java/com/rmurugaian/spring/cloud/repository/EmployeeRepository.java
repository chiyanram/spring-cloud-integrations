package com.rmurugaian.spring.cloud.repository;

import com.rmurugaian.spring.cloud.domain.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@SuppressWarnings("InterfaceNeverImplemented")
@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, UUID> {
}