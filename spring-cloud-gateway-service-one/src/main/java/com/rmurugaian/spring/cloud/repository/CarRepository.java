package com.rmurugaian.spring.cloud.repository;

import com.rmurugaian.spring.cloud.domain.Car;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarRepository extends ReactiveMongoRepository<Car, UUID> {
}