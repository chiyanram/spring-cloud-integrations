package com.rmurugaian.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MessagingServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MessagingServiceApplication.class);
    }

}

