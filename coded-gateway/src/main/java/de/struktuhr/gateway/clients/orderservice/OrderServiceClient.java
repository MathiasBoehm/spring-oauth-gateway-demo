package de.struktuhr.gateway.clients.orderservice;

import de.struktuhr.gateway.clients.orderservice.entity.Order;
import de.struktuhr.gateway.clients.personservice.PersonServiceClient;
import de.struktuhr.gateway.clients.personservice.entity.Person;
import de.struktuhr.gateway.common.CustomErrorDecoder;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OrderServiceClient {


    public Order getOrder(String id) {
        return orderService().getOrder(id);
    }

    private OrderService orderService() {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .errorDecoder(new CustomErrorDecoder())
                .target(OrderService.class, "http://localhost:9092");
    }


    interface OrderService {
        @RequestLine("GET /orders/{id}")
        Order getOrder(@Param("id") String id);
    }
}
