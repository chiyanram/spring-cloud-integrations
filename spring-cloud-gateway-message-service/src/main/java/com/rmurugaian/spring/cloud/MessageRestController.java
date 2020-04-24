package com.rmurugaian.spring.cloud;

import com.rmurugaian.spring.cloud.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class MessageRestController {

    @PostMapping("/messages")
    public Mono<ErrorResponse> message(@RequestBody final String response) {
        log.error(">>>>>>>>>>>>>>>>>>>>> {} ", response);
        return Mono.just(new ErrorResponse("Name should not be empty"));
    }

}
