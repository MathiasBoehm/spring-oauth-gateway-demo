package de.struktuhr.springgatewaydemo.boundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

@RequestMapping("/api")
@RestController
public class HelloController {

    @GetMapping("hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(new HelloResponse("Hello at " + LocalTime.now()));
    }

    public static class HelloResponse {
        public final String message;

        public HelloResponse(String message) {
            this.message = message;
        }
    }
}
