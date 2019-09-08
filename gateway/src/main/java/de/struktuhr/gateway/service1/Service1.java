package de.struktuhr.gateway.service1;

import de.struktuhr.gateway.common.CustomErrorDecoder;
import de.struktuhr.gateway.service1.entity.Person;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

@Component
public class Service1 {


    public Person getPerson(Long id) {
        return service1Client().getPerson(id);
    }

    private Service1Client service1Client() {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .errorDecoder(new CustomErrorDecoder())
                .target(Service1Client.class, "http://localhost:9091");
    }


    interface Service1Client {
        @RequestLine("GET /persons/{id}")
        Person getPerson(@Param("id") Long id);
    }
}
