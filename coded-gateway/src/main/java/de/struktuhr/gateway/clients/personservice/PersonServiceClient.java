package de.struktuhr.gateway.clients.personservice;

import de.struktuhr.gateway.clients.personservice.entity.Person;
import de.struktuhr.gateway.common.CustomErrorDecoder;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

@Component
public class PersonServiceClient {


    public Person getPerson(Long id) {
        return service1Client().getPerson(id);
    }

    public Person updatePerson(Long id, Person person) {
        return service1Client().updatePerson(person, id);
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

        @RequestLine("PUT /persons/{id}")
        @Headers("Content-Type: application/json")
        Person updatePerson(Person person, @Param("id") Long id);
    }
}
